package org.arbor.extrasounds.mapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.json.SoundEntrySerializer;
import org.arbor.extrasounds.json.SoundSerializer;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import pers.solid.brrp.v1.RRPEventHelper;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class SoundPackLoader
{
    private static final int CACHE_VERSION = 1;
    private static final RuntimeResourcePack genericPack = RuntimeResourcePack.create(ResourceLocation.tryParse("extrasounds"));
    private static final ResourceLocation soundsJsonId = new ResourceLocation("extrasounds:sounds.json");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CACHE_FNAME = ExtraSounds.MODID + ".cache";
    private static final Path cachePath =  Path.of(System.getProperty("java.io.tmpdir"), ".minecraft_fabric", CACHE_FNAME);

    public static List<RuntimeResourcePack> packs = Collections.emptyList();
    public static Map<String, SoundGenerator> mappers = new HashMap<>();

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SoundEventRegistration.class, new SoundEntrySerializer())
            .registerTypeAdapter(Sound.class, new SoundSerializer())
            .create();

    public static void init()
    {

        String json = getCache();
        if (json == null)
        {
            json = gson.toJson(processSounds());
            writeCache(json);
        }
        var jsonBytes = json.getBytes();

        DebugUtils.exportSoundsJson(jsonBytes);
        DebugUtils.exportGenerators();

        genericPack.addResource(PackType.CLIENT_RESOURCES, soundsJsonId, jsonBytes);
        RRPEventHelper.BEFORE_VANILLA.registerPack(genericPack);
    }

    private static Map<String, SoundEventRegistration> processSounds()
    {
        return Registry.ITEM.stream().flatMap(item -> {
            var itemId = Registry.ITEM.getKey(item);
            SoundDefinition def = new SoundDefinition(Sounds.aliased(Sounds.ITEM_PICK));

            if (mappers.containsKey(itemId.getNamespace()))
                def = mappers.get(itemId.getNamespace()).itemSoundGenerator().apply(item);
            else if (item instanceof BlockItem b)
                try
                {
                    var blockSound =
                            b.getBlock().getSoundType(b.getBlock().defaultBlockState()).getPlaceSound();
                    def = SoundDefinition.of(Sounds.aliased(blockSound));
                }
                catch (Exception ignored) {}

            List<Tuple<SoundEvent, SoundEventRegistration>> entries = new ArrayList<>();
            var pickupSound = registerOrDefault(itemId, SoundType.PICKUP, def.pickup, Sounds.aliased(Sounds.ITEM_PICK));
            entries.add(pickupSound);
            entries.add(registerOrDefault(itemId, SoundType.PLACE, def.place, Sounds.aliased(pickupSound.getA())));
            entries.add(registerOrDefault(itemId, SoundType.HOTBAR, def.hotbar, Sounds.aliased(pickupSound.getA())));
            return entries.stream();
        }).collect(Collectors.toMap(key -> key.getA().getLocation().getPath(), Tuple::getB));
    }

    private static Tuple<SoundEvent, SoundEventRegistration> registerOrDefault(ResourceLocation itemId, SoundType type, SoundEventRegistration entry, SoundEventRegistration defaultEntry)
    {
        var soundEntry = entry == null ? defaultEntry : entry;
        return new Tuple<>(registerIfNotExists(itemId, type), soundEntry);
    }

    private static SoundEvent registerIfNotExists(ResourceLocation itemId, SoundType type)
    {
        var soundId = new ResourceLocation(ExtraSounds.MODID, ExtraSounds.getClickId(itemId, type, false));
        var event = new SoundEvent(soundId);
        if (!Registry.SOUND_EVENT.containsKey(soundId))
            Registry.register(Registry.SOUND_EVENT, soundId, event);
        return event;
    }

    private static String getCache()
    {
        if (Files.exists(cachePath) && !DebugUtils.noCache)
            try
            {
                var lines = Files.readAllLines(cachePath);
                if (CacheInfo.fromString(lines.get(0)).equals(CacheInfo.getCurrent()))
                {
                    var cache = lines.get(1);
                    var jsonObj = JsonParser.parseString(cache).getAsJsonObject();
                    jsonObj.keySet().forEach((it) -> {
                        var identifier = new ResourceLocation(ExtraSounds.MODID, it);
                        if (!Registry.SOUND_EVENT.containsKey(identifier))
                            Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
                    });
                    return cache;
                }
                else
                {
                    DebugUtils.genericLog("Invalidating ExtraSounds cache.");
                    DebugUtils.genericLog("Previous: " + lines.get(0));
                    DebugUtils.genericLog("Current: " + CacheInfo.getCurrent());
                }
            }
            catch (Exception e)
            {
                System.err.println("Failed to load ExtraSounds cache.");
                e.printStackTrace();
            }
        else DebugUtils.genericLog("Cache not found - generating...");
        return null;
    }

    private static void writeCache(String json)
    {
        try
        {
            Files.write(cachePath, (CacheInfo.getCurrent() + "\n" + json).getBytes(),
                        StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            DebugUtils.genericLog("Cache saved.");
        }
        catch (IOException e)
        {
            System.err.println("Failed to save ExtraSounds cache.");
            e.printStackTrace();
        }
    }

    record CacheInfo(int version, int itemCount, String[] mappers)
    {
        public static CacheInfo getCurrent()
        {
            String[] versionInfos = new String[]{"1", "2"};
            return new CacheInfo(CACHE_VERSION, Registry.ITEM.size(), versionInfos);
        }

        public static CacheInfo fromString(String s)
        {
            try
            {
                var arr = s.split(";");
                return new CacheInfo(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), arr[2].split(","));
            }
            catch (Exception e)
            {
                return new CacheInfo(0, 0, new String[0]);
            }
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof CacheInfo comp)
                return this.version == comp.version
                        && this.itemCount == comp.itemCount
                        && Arrays.equals(this.mappers, comp.mappers);
            return false;
        }

        public String toString()
        {
            return "%d;%d;%s".formatted(version, itemCount, String.join(",", mappers));
        }
    }
}
