package dev.arbor.extrasoundsnext.annotation.fabric;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.annotation.AddonFinder;
import dev.arbor.extrasoundsnext.annotation.SoundsGenerator;
import dev.arbor.extrasoundsnext.annotation.SoundsGeneratorFabic;
import dev.arbor.extrasoundsnext.mapping.DefaultAutoGenerator;
import dev.arbor.extrasoundsnext.mapping.SoundDefinition;
import dev.arbor.extrasoundsnext.mapping.SoundGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.world.item.Item;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class AddonFinderImpl {
    public static List<Field> getAnnotatedFields() {
        List<EntrypointContainer<SoundsGeneratorFabic>> entrypointContainerList = FabricLoader.getInstance().getEntrypointContainers("sounds_generator", SoundsGeneratorFabic.class);
        Set<Field> annotatedFields = new LinkedHashSet<>();

        for (EntrypointContainer<SoundsGeneratorFabic> entrypointContainer : entrypointContainerList) {
            var entrypoint = entrypointContainer.getEntrypoint();
            for (Field field : entrypoint.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(SoundsGenerator.class)) {
                    if (!field.canAccess(null)) {
                        field.setAccessible(true);
                    }
                    annotatedFields.add(field);
                }
            }
        }
        return new ArrayList<>(annotatedFields);
    }

    public static List<SoundGenerator> getSoundGenerators() {
        List<SoundGenerator> list = new ArrayList<>();
        List<String> namespaces = new ArrayList<>();
        Map<String, Function<Item, SoundDefinition>> soundGenerators = new HashMap<>();
        var fields = AddonFinder.getSoundsGenerators();
        if (fields.isEmpty()) throw new RuntimeException("No sound generators found!");
        fields.forEach(field -> {
            try {
                if (field.getType().equals(SoundGenerator.class)) {
                    SoundGenerator soundGenerator = (SoundGenerator) field.get(null);
                    soundGenerators.put(soundGenerator.namespace, soundGenerator.itemSoundGenerator);
                    ExtraSoundsNext.LOGGER.info("Loaded sound generator: {}", soundGenerator.namespace);
                }
            } catch (IllegalAccessException e) {
                ExtraSoundsNext.LOGGER.error("Failed to load field: {}", field.getName(), e);
            }
        });
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> namespaces.add(modContainer.getMetadata().getId()));
        for (String namespace : namespaces) {
            list.add(SoundGenerator.of(namespace, soundGenerators.getOrDefault(namespace, DefaultAutoGenerator::autoGenerator)));
        }
        return list;
    }

    public static List<String> getModList() {
        return FabricLoader.getInstance().getAllMods().stream()
                .map(modContainer -> modContainer.getMetadata().getId())
                .filter(s -> !s.contains("generated"))
                .sorted()
                .toList();
    }
}
