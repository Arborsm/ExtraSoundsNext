package org.arbor.extrasounds.mapping;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.json.SoundEntrySerializer;
import org.arbor.extrasounds.json.SoundSerializer;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ExtraSounds.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SoundPackLoader {
    public static JsonObject GENERATED_SOUNDS;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CACHE_FNAME = ExtraSounds.MODID + ".cache";
    private static final Path CACHE_PATH_FILE =
            Path.of(System.getProperty("java.io.tmpdir"), ".minecraft", CACHE_FNAME);

    public static final Map<ResourceLocation, SoundEvent> CUSTOM_SOUND_EVENT = new HashMap<>();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(SoundEventRegistration.class, new SoundEntrySerializer())
            .registerTypeAdapter(Sound.class, new SoundSerializer())
            .create();

    @SubscribeEvent
    static void onRegisterEvent(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE))
            return;
        init();
    }

    /**
     * Initialization of customized sound event.<br>
     * If it is absent or invalid, the file will be regenerated.<br>
     * If the regeneration time over 1000 milliseconds, it may be needed to refactor.
     */
    public static void init() {
        final long start = System.currentTimeMillis();
        final Map<String, SoundGenerator> soundGenMappers = new HashMap<>();
        // soundGenMappers.put(autoGenerator.generator.namespace, autoGenerator.generator);
        for (SoundGenerator generator : AutoGenerator.getSoundGenerators()) {
            soundGenMappers.put(generator.namespace, generator);
        }
        // Deleted once.
        try {
            Files.createDirectories(CACHE_PATH_FILE.getParent());
            Files.deleteIfExists(CACHE_PATH_FILE);
        } catch (Throwable ex) {
            DebugUtils.genericLog(ex.getMessage());
        }
        // Read from cache.
        try {
            if (!Files.exists(CACHE_PATH_FILE)) {
                throw new FileNotFoundException("Cache does not exist.");
            }
            if (DebugUtils.NO_CACHE) {
                throw new RuntimeException("JVM arg '%s' is detected.".formatted(DebugUtils.NO_CACHE_VAR));
            }
        } catch (Throwable ex) {
            // If there is an exception, regenerate and write the cache.
            DebugUtils.genericLog(ex.getMessage());
            LOGGER.info("[{}] Regenerating cache...", ExtraSounds.class.getSimpleName());
            final Map<String, SoundEventRegistration> resourceMapper = new HashMap<>();
            processSounds(soundGenMappers, resourceMapper);
            CacheData.create(resourceMapper);
        }
        // toJson
        try {
            final CacheData cacheData = CacheData.read();
            final JsonObject jsonObject = cacheData.asJsonObject();
            jsonObject.keySet().forEach(key -> putSoundEvent(new ResourceLocation(ExtraSounds.MODID, key)));
            GENERATED_SOUNDS = jsonObject;
        } catch (JsonParseException e) {
            DebugUtils.genericLog(e.getMessage());
        }
        if (DebugUtils.DEBUG) {
            DebugUtils.exportSoundsJson(CacheData.read().asJsonBytes());
            DebugUtils.exportGenerators(soundGenMappers);
        }

        final long tookMillis = System.currentTimeMillis() - start;
        if (tookMillis >= 1000) {
            LOGGER.warn("[{}] init took too long; {}ms.", ExtraSounds.class.getSimpleName(), tookMillis);
        } else {
            DebugUtils.genericLog("%s init finished; took %dms.".formatted(SoundPackLoader.class.getSimpleName(), tookMillis));
        }
        LOGGER.info("[{}] sound pack successfully loaded; {} entries.", ExtraSounds.class.getSimpleName(), CUSTOM_SOUND_EVENT.keySet().size());
    }

    /**
     * Processes for the all items.<br>
     * This method is "Memory Sensitive" as creates 3x {@link SoundEventRegistration}s per item,
     * and avoid using the Stream APIs in non-debug mode as much as possible.
     *
     * @param soundGenerator The information of generator including namespace and {@link SoundGenerator}.
     * @param resource       The {@link Map} of resource that the SoundEntry will be stored.
     */
    private static void processSounds(Map<String, SoundGenerator> soundGenerator, Map<String, SoundEventRegistration> resource) {
        final SoundEventRegistration fallbackSoundEntry = Sounds.aliased(Sounds.ITEM_PICK);
        final List<String> inSoundsJsonIds = Lists.newArrayList();
        final String fallbackSoundJson = GSON.toJson(fallbackSoundEntry);
        if (DebugUtils.SEARCH_UNDEF_SOUND) {
            try (InputStream stream = SoundPackLoader.class.getClassLoader().getResourceAsStream("assets/extrasounds/sounds.json")) {
                Objects.requireNonNull(stream);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                final JsonObject jsonObject = JsonParser.parseString(reader.lines().collect(Collectors.joining())).getAsJsonObject();
                inSoundsJsonIds.addAll(jsonObject.keySet());
            } catch (Throwable ex) {
                LOGGER.warn("cannot open ExtraSounds' sounds.json.", ex);
            }
        }

        for (Item item : BuiltInRegistries.ITEM) {
            final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
            final SoundDefinition definition;
            if (soundGenerator.containsKey(Objects.requireNonNull(itemId).getNamespace())) {
                definition = soundGenerator.get(itemId.getNamespace()).itemSoundGenerator.apply(item);
            } else if (item instanceof BlockItem blockItem) {
                SoundDefinition blockSoundDef = SoundDefinition.of(fallbackSoundEntry);
                try {
                    final Block block = blockItem.getBlock();
                    final SoundEvent blockSound = AutoGenerator.getSoundType(block).getPlaceSound();
                    blockSoundDef = SoundDefinition.of(Sounds.aliased(blockSound));
                } catch (Throwable ignored) {
                }
                definition = blockSoundDef;
            } else {
                definition = SoundDefinition.of(fallbackSoundEntry);
            }

            final ResourceLocation pickupSoundId = ExtraSounds.getClickId(itemId, SoundType.PICKUP);
            final SoundEventRegistration pickupSoundEntry = Sounds.aliased(ExtraSounds.createEvent(pickupSoundId));
            generateSoundEntry(itemId, SoundType.PICKUP, definition.pickup, pickupSoundEntry, resource);
            generateSoundEntry(itemId, SoundType.PLACE, definition.place, pickupSoundEntry, resource);
            generateSoundEntry(itemId, SoundType.HOTBAR, definition.hotbar, pickupSoundEntry, resource);

            if (DebugUtils.SEARCH_UNDEF_SOUND) {
                final boolean isFallbackSoundEntry = Objects.equals(GSON.toJson(definition.pickup), fallbackSoundJson);
                final boolean notIncludeSoundsJson = !inSoundsJsonIds.contains(pickupSoundId.getPath());
                if (isFallbackSoundEntry && notIncludeSoundsJson) {
                    LOGGER.warn("unregistered sound was found: '{}'", itemId);
                }
            }
        }
    }

    /**
     * Generates the resource.
     *
     * @param itemId       Target item id.
     * @param type         The {@link SoundType} which category of volume to play.
     * @param entry        Target {@link SoundEventRegistration}.
     * @param defaultEntry The fallback SoundEntry.
     * @param resource     The {@link Map} of resource that the SoundEntry will be stored.
     */
    private static void generateSoundEntry(ResourceLocation itemId, SoundType type, SoundEventRegistration entry, SoundEventRegistration defaultEntry, Map<String, SoundEventRegistration> resource) {
        final SoundEventRegistration soundEntry = (entry == null) ? defaultEntry : entry;
        final ResourceLocation id = ExtraSounds.getClickId(itemId, type);
        resource.put(id.getPath(), soundEntry);
        putSoundEvent(id);
    }

    /**
     * Creates and Registers the {@link SoundEvent} from specified {@link ResourceLocation}.
     *
     * @param clickId Target id.
     */
    private static void putSoundEvent(ResourceLocation clickId) {
        CUSTOM_SOUND_EVENT.put(clickId, ExtraSounds.createEvent(clickId));
    }

    /**
     * Shows the cache data that include Json String.
     */
    protected static class CacheData {

        /**
         * The cache data.
         */
        private final CharSequence json;

        private CacheData(CharSequence json) {
            this.json = json;
        }

        /**
         * Reads the cache data.
         *
         * @return The instance of {@link CacheData}.
         */
        static CacheData read() {
            try (BufferedReader reader = Files.newBufferedReader(CACHE_PATH_FILE)) {
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return new CacheData(builder);
            } catch (Throwable ex) {
                LOGGER.error("[%s] Failed to load ExtraSounds cache.".formatted(ExtraSounds.class.getSimpleName()), ex);
            }
            return new CacheData("{}");
        }

        /**
         * Writes to the file.
         *
         * @param map The cache data that will be converted to json.
         */
        static void create(Map<String, SoundEventRegistration> map) {
            try (BufferedWriter writer = Files.newBufferedWriter(CACHE_PATH_FILE)) {
                GSON.toJson(map, writer);
                writer.flush();
                DebugUtils.genericLog("Cache saved at %s".formatted(CACHE_PATH_FILE.toAbsolutePath()));
            } catch (Throwable ex) {
                LOGGER.error("[%s] Failed to save the cache.".formatted(ExtraSounds.class.getSimpleName()), ex);
            }
        }

        public JsonObject asJsonObject() throws JsonParseException {
            return JsonParser.parseString(this.json.toString()).getAsJsonObject();
        }

        public byte[] asJsonBytes() {
            return this.json.toString().getBytes();
        }
    }
}
