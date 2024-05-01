package org.arbor.extrasounds.annotation;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.mapping.DefaultAutoGenerator;
import org.arbor.extrasounds.mapping.SoundDefinition;
import org.arbor.extrasounds.mapping.SoundGenerator;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static org.arbor.extrasounds.ExtraSounds.LOGGER;

public class AddonFinder {
    protected static List<Field> cache = null;
    private static final Map<String, Class<?>> CLASS_CACHE = new HashMap<>();

    public static List<Field> getSoundsGenerators() {
        if (cache == null) {
            cache = getAnnotatedFields();
        }
        return cache;
    }

    private static List<Field> getAnnotatedFields() {
        Type annotationType = Type.getType(SoundsGenerator.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<Field> annotatedFields = new LinkedHashSet<>();

        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (annotationType != null && Objects.equals(a.annotationType(), annotationType)) {
                    try {
                        Class<?> asmClass = loadClass(a.clazz().getClassName());
                        for (Field field : asmClass.getDeclaredFields()) {
                            if (field.isAnnotationPresent(SoundsGenerator.class)) {
                                if (!field.canAccess(null)) {
                                    field.setAccessible(true);
                                }
                                annotatedFields.add(field);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("Failed to load class: {}", a.clazz().getClassName(), e);
                        if (!FMLLoader.isProduction()) throw new RuntimeException(e);
                    }
                }
            }
        }
        return new ArrayList<>(annotatedFields);
    }

    private static Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz = CLASS_CACHE.get(className);
        if (clazz == null) {
            clazz = Class.forName(className);
            CLASS_CACHE.put(className, clazz);
        }
        return clazz;
    }


    public static List<SoundGenerator> getSoundGenerators() {
        List<SoundGenerator> list = new ArrayList<>();
        List<String> namespaces = new ArrayList<>();
        Map<String, Function<Item, SoundDefinition>> soundGenerators = new HashMap<>();
        var fields = getSoundsGenerators();
        if (fields.isEmpty()) throw new RuntimeException("No sound generators found!");
        fields.forEach(field -> {
            try {
                if (field.getType().equals(SoundGenerator.class)) {
                    SoundGenerator soundGenerator = (SoundGenerator) field.get(null);
                    soundGenerators.put(soundGenerator.namespace, soundGenerator.itemSoundGenerator);
                    ExtraSounds.LOGGER.info("Loaded sound generator: {}", soundGenerator.namespace);
                }
            } catch (IllegalAccessException e) {
                ExtraSounds.LOGGER.error("Failed to load field: {}", field.getName(), e);
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
