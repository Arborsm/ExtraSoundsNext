package dev.arbor.extrasoundsnext.annotation.forge;

import com.mojang.text2speech.Narrator;
import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.annotation.AddonFinder;
import dev.arbor.extrasoundsnext.annotation.SoundsGenerator;
import dev.arbor.extrasoundsnext.mapping.DefaultAutoGenerator;
import dev.arbor.extrasoundsnext.mapping.SoundDefinition;
import dev.arbor.extrasoundsnext.mapping.SoundGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class AddonFinderImpl {
    public static List<Field> getAnnotatedFields() {
        Type annotationType = Type.getType(SoundsGenerator.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<Field> annotatedFields = new LinkedHashSet<>();

        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (annotationType != null && Objects.equals(a.annotationType(), annotationType)) {
                    try {
                        Class<?> asmClass = AddonFinder.loadClass(a.clazz().getClassName());
                        for (Field field : asmClass.getDeclaredFields()) {
                            if (field.isAnnotationPresent(SoundsGenerator.class)) {
                                if (!field.canAccess(null)) {
                                    field.setAccessible(true);
                                }
                                annotatedFields.add(field);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        Narrator.LOGGER.error("Failed to load class: {}", a.clazz().getClassName(), e);
                        if (!FMLLoader.isProduction()) throw new RuntimeException(e);
                    }
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
                if (!FMLLoader.isProduction()) throw new RuntimeException(e);
            }
        });
        ModList.get().getMods().forEach(iModInfo -> namespaces.add(iModInfo.getModId()));
        for (String namespace : namespaces) {
            list.add(SoundGenerator.of(namespace, soundGenerators.getOrDefault(namespace, DefaultAutoGenerator::autoGenerator)));
        }
        return list;
    }
}
