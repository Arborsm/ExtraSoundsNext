package dev.arbor.extrasoundsnext.annotation;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.arbor.extrasoundsnext.mapping.SoundGenerator;

import java.lang.reflect.Field;
import java.util.*;

public class AddonFinder {
    protected static List<Field> cache = null;
    private static final Map<String, Class<?>> CLASS_CACHE = new HashMap<>();

    public static List<Field> getSoundsGenerators() {
        if (cache == null) {
            cache = getAnnotatedFields();
        }
        return cache;
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz = CLASS_CACHE.get(className);
        if (clazz == null) {
            clazz = Class.forName(className);
            CLASS_CACHE.put(className, clazz);
        }
        return clazz;
    }

    @ExpectPlatform
    private static List<Field> getAnnotatedFields() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static List<SoundGenerator> getSoundGenerators() {
        throw new AssertionError();
    }
}
