package org.arbor.extrasounds.mapping;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
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
import java.util.*;
import java.util.stream.Collectors;

public class SoundPackLoader {
    private static final int CACHE_VERSION = 1;
    private static final ResourceLocation SOUNDS_JSON_ID = new ResourceLocation(ExtraSounds.MODID, "sounds.json");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CACHE_FNAME = ExtraSounds.MODID + ".cache";
    private static final Path CACHE_PATH = Path.of(System.getProperty("java.io.tmpdir"), ".minecraft_fabric", CACHE_FNAME);

    public static final Map<ResourceLocation, SoundEvent> CUSTOM_SOUND_EVENT = new HashMap<>();

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(SoundEventRegistration.class, new SoundEntrySerializer())
            .registerTypeAdapter(Sound.class, new SoundSerializer())
            .create();

    /**
     * Initialization of customized sound event.<br>
     * The cache file stored at {@link SoundPackLoader#CACHE_PATH} will be used.
     * If it is absent or invalid, the file will be regenerated.<br>
     * If the regeneration time over 1000 milliseconds, it may be needed to refactor.
     */
    public static void init() {
        LOGGER.info(CACHE_PATH.toString());

        final long start = System.currentTimeMillis();
        final Map<String, SoundGenerator> soundGenMappers = new HashMap<>();
        for (SoundGenerator generator : AutoGenerator.getSoundGenerators()) {
            soundGenMappers.put(generator.namespace, generator);
        }
        final CacheInfo currentCacheInfo = CacheInfo.of(new String[]{"blah"});

        // Read from cache.
        try {
            Files.createDirectories(CACHE_PATH.getParent());

            if (!Files.exists(CACHE_PATH)) {
                throw new FileNotFoundException("Cache does not exist.");
            }

            if (DebugUtils.NO_CACHE) {
                throw new RuntimeException("JVM arg '%s' is detected.".formatted(DebugUtils.NO_CACHE_VAR));
            }

            final CacheData cacheData = CacheData.read();
            if (!cacheData.info.equals(currentCacheInfo)) {
                throw new InvalidObjectException("Incorrect cache info.");
            }

            final JsonObject jsonObject = cacheData.asJsonObject();
            jsonObject.keySet().forEach(key -> putSoundEvent(new ResourceLocation(ExtraSounds.MODID, key)));
        } catch (Throwable ex) {
            // If there is an exception, regenerate and write the cache.
            DebugUtils.genericLog(ex.getMessage());
            LOGGER.info("[{}] Regenerating cache...", ExtraSounds.class.getSimpleName());
            final Map<String, SoundEventRegistration> resourceMapper = new HashMap<>();
            processSounds(soundGenMappers, resourceMapper);
            CacheData.create(currentCacheInfo, resourceMapper);
        }

        if (DebugUtils.DEBUG) {
            DebugUtils.exportSoundsJson(CacheData.read().asJsonBytes());
            DebugUtils.exportGenerators(soundGenMappers);
        }

        ExtraSounds.pack.addAsyncResource(PackType.CLIENT_RESOURCES, SOUNDS_JSON_ID, identifier -> CacheData.read().asJsonBytes());
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

        for (Item item : ForgeRegistries.ITEMS) {
            final ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
            final SoundDefinition definition;
            if (soundGenerator.containsKey(Objects.requireNonNull(itemId).getNamespace())) {
                definition = soundGenerator.get(itemId.getNamespace()).itemSoundGenerator.apply(item);
            } else if (item instanceof BlockItem blockItem) {
                SoundDefinition blockSoundDef = SoundDefinition.of(fallbackSoundEntry);
                try {
                    final Block block = blockItem.getBlock();
                    final SoundEvent blockSound = block.getSoundType(block.defaultBlockState()).getPlaceSound();
                    blockSoundDef = SoundDefinition.of(Sounds.aliased(blockSound));
                } catch (Throwable ignored) {
                }
                definition = blockSoundDef;
            } else {
                definition = SoundDefinition.of(fallbackSoundEntry);
            }

            final ResourceLocation pickupSoundId = ExtraSounds.getClickId(itemId, SoundType.PICKUP);
            final SoundEventRegistration pickupSoundEntry = Sounds.aliased(new SoundEvent(pickupSoundId));
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
        CUSTOM_SOUND_EVENT.put(clickId, new SoundEvent(clickId));
    }

    /**
     * Shows the information of the cache.<br>
     * This is used at the first line in the file defined by {@link SoundPackLoader#CACHE_FNAME}.
     *
     * @param version   The cache version.
     * @param itemCount The number of the Item Registry.
     * @param info      The String array of mod ids.
     */
    record CacheInfo(int version, int itemCount, String[] info) {
        private static final String DELIMITER_MOD_INFO = ",";
        private static final String DELIMITER_HEAD = ";";

        /**
         * Creates new cache info from generator version info.
         *
         * @param info The array of String that include mod ids.
         * @return A new instance of {@link CacheInfo}.
         */
        public static CacheInfo of(String[] info) {
            return new CacheInfo(CACHE_VERSION, ForgeRegistries.ITEMS.getEntries().size(), info);
        }

        /**
         * Parses to the {@link CacheInfo} from String.
         *
         * @param string The String.
         * @return A new instance of {@link CacheInfo}.
         */
        public static CacheInfo fromString(String string) {
            try {
                var arr = string.split(DELIMITER_HEAD);
                return new CacheInfo(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), arr[2].split(DELIMITER_MOD_INFO));
            } catch (Throwable ignored) {
                return new CacheInfo(0, 0, new String[0]);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof CacheInfo comp)
                return this.version == comp.version
                        && this.itemCount == comp.itemCount
                        && Arrays.equals(this.info, comp.info);
            return false;
        }

        public String toString() {
            final CharSequence[] data = new CharSequence[]{
                    String.valueOf(version), String.valueOf(itemCount), String.join(DELIMITER_MOD_INFO, info)
            };
            return String.join(DELIMITER_HEAD, data);
        }

    }

    /**
     * Shows the cache data that include {@link CacheInfo} and Json String.
     */
    protected static class CacheData {
        /**
         * The cache info.
         */
        private final CacheInfo info;
        /**
         * The cache data.
         */
        private final CharSequence json;

        private CacheData(CacheInfo info, CharSequence json) {
            this.info = info;
            this.json = json;
        }

        /**
         * Reads the cache data.
         *
         * @return The instance of {@link CacheData}.
         */
        static CacheData read() {
            try (BufferedReader reader = Files.newBufferedReader(CACHE_PATH)) {
                final CacheInfo cacheInfo = CacheInfo.fromString(reader.readLine().trim());
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return new CacheData(cacheInfo, builder);
            } catch (Throwable ex) {
                LOGGER.error("[%s] Failed to load ExtraSounds cache.".formatted(ExtraSounds.class.getSimpleName()), ex);
            }
            return new CacheData(CacheInfo.of(new String[0]), "{}");
        }

        /**
         * Writes to the file.
         *
         * @param info The current cache info.
         * @param map  The cache data that will be converted to json.
         */
        static void create(CacheInfo info, Map<String, SoundEventRegistration> map) {
            try (BufferedWriter writer = Files.newBufferedWriter(CACHE_PATH)) {
                writer.write(info.toString().trim());
                writer.newLine();
                GSON.toJson(map, writer);
                writer.flush();
                DebugUtils.genericLog("Cache saved at %s".formatted(CACHE_PATH.toAbsolutePath()));
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
