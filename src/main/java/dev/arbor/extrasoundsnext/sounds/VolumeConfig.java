package dev.arbor.extrasoundsnext.sounds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private static final int CURRENT_VERSION = 2;

    private static final EnumMap<Mixers, Float> volumes = new EnumMap<>(Mixers.class);
    private static final EnumMap<SoundEntry, Boolean> soundEnabled = new EnumMap<>(SoundEntry.class);

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

        // Initialize all sounds as enabled by default
        for (SoundEntry sound : SoundEntry.values()) {
            soundEnabled.put(sound, true);
        }

        Path configPath = getConfigPath();
        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                JsonObject root = JsonParser.parseString(json).getAsJsonObject();

                // Check version
                int version = root.has("version") ? root.get("version").getAsInt() : 1;

                if (version == 1) {
                    // Old format: flat structure, migrate to new format
                    migrateFromV1(root);
                } else if (version == CURRENT_VERSION) {
                    // New format: nested structure
                    loadV2(root);
                }
            } catch (Exception e) {
                ExtraSoundsNext.LOGGER.error("[ExtraSoundsNext] Failed to load volume config", e);
            }
        } else {
            save();
        }
    }

    private static void migrateFromV1(JsonObject root) {
        // Load mixer volumes from flat structure
        for (Mixers cat : Mixers.values()) {
            if (root.has(cat.id)) {
                volumes.put(cat, root.get(cat.id).getAsFloat());
            }
        }
        // All sounds enabled by default (already initialized)
        ExtraSoundsNext.LOGGER.info("[ExtraSoundsNext] Migrated config from v1 to v2");
        save(); // Save in new format
    }

    private static void loadV2(JsonObject root) {
        // Load mixer volumes
        if (root.has("mixers")) {
            JsonObject mixers = root.getAsJsonObject("mixers");
            for (Mixers cat : Mixers.values()) {
                if (mixers.has(cat.id)) {
                    volumes.put(cat, mixers.get(cat.id).getAsFloat());
                }
            }
        }

        // Load sound enabled states
        if (root.has("sounds")) {
            JsonObject sounds = root.getAsJsonObject("sounds");
            for (SoundEntry sound : SoundEntry.values()) {
                if (sounds.has(sound.id)) {
                    soundEnabled.put(sound, sounds.get(sound.id).getAsBoolean());
                }
            }
        }
    }

    public static void save() {
        JsonObject root = new JsonObject();
        root.addProperty("version", CURRENT_VERSION);

        // Save mixer volumes
        JsonObject mixersObj = new JsonObject();
        for (Mixers cat : Mixers.values()) {
            mixersObj.addProperty(cat.id, volumes.get(cat));
        }
        root.add("mixers", mixersObj);

        // Save sound enabled states
        JsonObject soundsObj = new JsonObject();
        for (SoundEntry sound : SoundEntry.values()) {
            soundsObj.addProperty(sound.id, soundEnabled.get(sound));
        }
        root.add("sounds", soundsObj);

        Path configPath = getConfigPath();
        try {
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, GSON.toJson(root));
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

    /**
     * Checks if a specific sound is enabled.
     * @param sound The sound entry to check
     * @return true if enabled, false otherwise
     */
    public static boolean isSoundEnabled(SoundEntry sound) {
        Boolean enabled = soundEnabled.get(sound);
        return enabled != null ? enabled : true;
    }

    /**
     * Sets whether a specific sound is enabled.
     * @param sound The sound entry to modify
     * @param enabled true to enable, false to disable
     */
    public static void setSoundEnabled(SoundEntry sound, boolean enabled) {
        soundEnabled.put(sound, enabled);
    }

    /**
     * Resets all settings to their default values.
     */
    public static void resetToDefaults() {
        // Reset mixer volumes
        for (Mixers cat : Mixers.values()) {
            if (cat.toggle) {
                volumes.put(cat, cat.defaultOn ? 1.0f : 0.0f);
            } else {
                volumes.put(cat, cat.defaultLevel);
            }
        }

        // Enable all sounds
        for (SoundEntry sound : SoundEntry.values()) {
            soundEnabled.put(sound, true);
        }

        save();
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
