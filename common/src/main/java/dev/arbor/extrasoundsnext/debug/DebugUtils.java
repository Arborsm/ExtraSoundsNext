package dev.arbor.extrasoundsnext.debug;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.effect.MobEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.arbor.extrasoundsnext.mapping.SoundGenerator;
import dev.arbor.extrasoundsnext.sounds.SoundManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.stream.Collectors;

public class DebugUtils {
    public static final String DEBUG_VAR = "extrasounds.debug";
    public static final String DEBUG_PATH_VAR = "extrasounds.debug.path";
    public static final String NO_CACHE_VAR = "extrasounds.nocache";
    private static final String JVM_ARG_SEARCH_UNDEF_SND = "extrasounds.searchundef";

    public static final boolean DEBUG = System.getProperties().containsKey(DEBUG_VAR)
            && System.getProperties().get(DEBUG_VAR).equals("true");
    public static final String DEBUG_PATH = System.getProperties().containsKey(DEBUG_PATH_VAR)
            ? System.getProperty(DEBUG_PATH_VAR) : "debug/";
    public static final boolean NO_CACHE = System.getProperties().containsKey(NO_CACHE_VAR)
            && System.getProperties().get(NO_CACHE_VAR).equals("true");
    /**
     * For debugging.<br>
     * When run with a JVM argument {@link DebugUtils#JVM_ARG_SEARCH_UNDEF_SND}, the log shows a SoundEntry that plays
     * the default {@link dev.arbor.extrasoundsnext.sounds.Sounds#ITEM_PICK}.<br>
     * To ensure that the debugging statements are executed, it is recommended that you also run with the
     * {@link DebugUtils#NO_CACHE_VAR} JVM argument.
     */
    public static final boolean SEARCH_UNDEF_SOUND = System.getProperties().containsKey(JVM_ARG_SEARCH_UNDEF_SND)
            && System.getProperties().get(JVM_ARG_SEARCH_UNDEF_SND).equals("true");

    private static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        if (!DEBUG) return;
        LOGGER.info("ExtraSounds: DEBUG mode enabled.");
        LOGGER.info("Debug path: " + Path.of(DEBUG_PATH).toAbsolutePath());
        if (NO_CACHE) {
            LOGGER.info("ExtraSounds: No cache mode enabled.");
        }
        if (SEARCH_UNDEF_SOUND) {
            LOGGER.info("ExtraSounds: Searching for undefined sounds.");
        }
    }

    public static void exportSoundsJson(byte[] jsonData) {
        if (!DEBUG) return;
        Path p = null;
        try {
            p = Path.of(DEBUG_PATH).resolve("sounds.json");
            createFile(p);
            Files.write(p, jsonData, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            ExtraSoundsNext.LOGGER.error("Failed to write to file: {}", p.toAbsolutePath(), e);
        }
    }

    public static void exportGenerators(Map<String, SoundGenerator> generator) {
        if (!DEBUG) return;
        Path p = Path.of(DEBUG_PATH).resolve("generators.txt");
        createFile(p);
        try {
            Files.write(p, generator.keySet().stream()
                    .map(it -> {
                        var clazz = generator.get(it).itemSoundGenerator.getClass();
                        return "namespace: " + it + "; class: " + clazz.getName();
                    })
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            ExtraSoundsNext.LOGGER.error("Failed to write to file: {}", p.toAbsolutePath(), e);
        }
    }

    public static void soundLog(SoundInstance instance) {
        if (!DEBUG) return;
        LOGGER.info("Playing sound: {}", instance.getLocation());
    }

    public static void effectLog(MobEffect effect, SoundManager.EffectType type) {
        if (!DEBUG) return;
        LOGGER.info("EffectType = {}, Effect = {}", type, effect.getDisplayName().getString());
    }

    public static void genericLog(String message) {
        if (!DEBUG) return;
        LOGGER.info(message);
    }

    private static void createFile(Path p) {
        try {
            final Path debugPath = Path.of(DebugUtils.DEBUG_PATH);
            if (!Files.isDirectory(debugPath))
                Files.createDirectory(debugPath);
            if (!Files.exists(p))
                Files.createFile(p);
        } catch (IOException e) {
            ExtraSoundsNext.LOGGER.error("Failed to create file: {}", p.toAbsolutePath(), e);
        }
    }
}
