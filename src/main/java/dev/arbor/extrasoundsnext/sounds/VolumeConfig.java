package dev.arbor.extrasoundsnext.sounds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.arbor.extrasoundsnext.ExtraSoundsNext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;

public final class VolumeConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<HashMap<String, Float>>() {}.getType();
    private static final EnumMap<Mixers, Float> volumes = new EnumMap<>(Mixers.class);

    private VolumeConfig() {}

    public static void load() {
        // Initialize defaults
        for (Mixers cat : Mixers.values()) {
            if (cat.toggle) {
                volumes.put(cat, cat.defaultOn ? 1.0f : 0.0f);
            } else {
                volumes.put(cat, cat.defaultLevel);
            }
        }

        Path configPath = getConfigPath();
        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                HashMap<String, Float> loaded = GSON.fromJson(json, MAP_TYPE);
                if (loaded != null) {
                    for (Mixers cat : Mixers.values()) {
                        Float val = loaded.get(cat.id);
                        if (val != null) {
                            volumes.put(cat, val);
                        }
                    }
                }
            } catch (Exception e) {
                ExtraSoundsNext.LOGGER.error("[ExtraSoundsNext] Failed to load volume config", e);
            }
        }
    }

    public static void save() {
        HashMap<String, Float> map = new HashMap<>();
        for (Mixers cat : Mixers.values()) {
            map.put(cat.id, volumes.get(cat));
        }

        Path configPath = getConfigPath();
        try {
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, GSON.toJson(map));
        } catch (IOException e) {
            ExtraSoundsNext.LOGGER.error("[ExtraSoundsNext] Failed to save volume config", e);
        }
    }

    public static float getVolume(Mixers category) {
        Float val = volumes.get(category);
        return val != null ? val : category.defaultLevel;
    }

    public static void setVolume(Mixers category, float value) {
        volumes.put(category, value);
    }

    private static Path getConfigPath() {
        //? if fabric {
        /*return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("extrasounds.json");
        *///?} elif neoforge {
        /*return net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get().resolve("extrasounds.json");
        *///?} elif forge {
        return net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get().resolve("extrasounds.json");
        //?}
    }
}
