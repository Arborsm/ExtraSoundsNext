package dev.arbor.extrasoundsnext.sounds;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.annotation.CategoryLoader;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Field;
import java.util.*;

public final class SoundSouceInit {
    /**
     * The Map of {@link SoundSource} including to which group the category belongs.<br>
     * <code>Unique category -> Group category</code>
     */
    public static final Map<SoundSource, SoundSource> PARENTS = new HashMap<>();
    /**
     * The Map of {@link String} -> {@link SoundSource} showing which a master category the class has.<br>
     * <code>Class name -> Master category</code>
     */
    public static final Map<String, SoundSource> MASTERS = new HashMap<>();
    public static final Map<SoundSource, Float> DEFAULT_LEVELS = new HashMap<>();
    public static final Map<SoundSource, Pair<Boolean, Boolean>> TOGGLEABLE_CATS = new HashMap<>();
    public static final Map<SoundSource, Component> TOOLTIPS = new HashMap<>();
    public static final List<String> SUPPRESSED_NAMES = new ArrayList<>();
    public static String[] MASTER_CLASSES;

    public static Pair<CategoryLoader, List<Field>> getCategories() {
        return Pair.of(ExtraSoundsNext.MIXERS, getRegistrations());
    }

    public static List<Field> getRegistrations() {
        return Arrays.stream(ExtraSoundsNext.MIXERS.getClass().getDeclaredFields()).filter(it ->
                it.isAnnotationPresent(CategoryLoader.Register.class)).toList();
    }

    public static String generateFieldClassName(Class<?> clazz, Field field) {
        return "%s#%s".formatted(clazz.getCanonicalName(), field.getName());
    }

    public static void initCategoryLoader() {
        //required so that the new categories are actually created, not used
        SoundSource.MASTER.getClass().getClassLoader();

        try {
            final Pair<CategoryLoader, List<Field>> allAnnotations = getCategories();

            // First fetch for the MASTER categories.
            for (Field field : allAnnotations.getSecond()) {
                final CategoryLoader categoryLoader = allAnnotations.getFirst();
                final CategoryLoader.Register annotation = field.getAnnotation(CategoryLoader.Register.class);
                final String className = categoryLoader.getClass().getCanonicalName();
                if (!(field.get(categoryLoader) instanceof final SoundSource source)) {
                    final String fieldClassName = generateFieldClassName(categoryLoader.getClass(), field);
                    if (!SUPPRESSED_NAMES.contains(fieldClassName)) {
                        ExtraSoundsNext.LOGGER.error(
                                "[%s] Cast check failed for the member '%s'.".formatted(ExtraSoundsNext.class.getSimpleName(), fieldClassName),
                                new ClassCastException("Can not cast %s to SoundCategory".formatted(field.get(categoryLoader).getClass().getCanonicalName())));
                        SUPPRESSED_NAMES.add(fieldClassName);
                    }
                    continue;
                }

                if (!annotation.master()) {
                    continue;
                }

                if (MASTERS.containsKey(className)) {
                    // The MASTER already registered.
                    if (!SUPPRESSED_NAMES.contains(className)) {
                        ExtraSoundsNext.LOGGER.warn(
                                "[%s] Unexpected annotation was found.".formatted(ExtraSoundsNext.class.getSimpleName()),
                                new AnnotationFormatError("Class '%s' has a duplicate member with annotation value 'master'!".formatted(className)));
                        SUPPRESSED_NAMES.add(className);
                    }
                    PARENTS.put(source, MASTERS.get(className));
                }
                MASTERS.putIfAbsent(className, source);
            }

            MASTER_CLASSES = MASTERS.keySet().toArray(String[]::new);
            Arrays.sort(MASTER_CLASSES);

            // Put all the customized SoundCategories.
            for (Field field : allAnnotations.getSecond()) {
                final CategoryLoader categoryLoader = allAnnotations.getFirst();
                final CategoryLoader.Register annotation = field.getAnnotation(CategoryLoader.Register.class);
                final String className = categoryLoader.getClass().getCanonicalName();
                if (!(field.get(categoryLoader) instanceof final SoundSource category)) {
                    continue;
                }

                if (!annotation.master()) {
                    if (MASTERS.containsKey(className)) {
                        PARENTS.put(category, MASTERS.get(className));
                    } else {
                        // The 'orphan' category was found, will be grouped together with Vanilla volume options.
                        // This is deprecated as it causes confusion for users.
                        if (!SUPPRESSED_NAMES.contains(className)) {
                            ExtraSoundsNext.LOGGER.warn("[{}] Missing annotation value 'master' in class '{}'. This is deprecated.",
                                    ExtraSoundsNext.class.getSimpleName(), className);
                            ExtraSoundsNext.LOGGER.warn("[{}] To avoid this message, please specify \"master = true\" in one of the @Register annotation in your class.",
                                    ExtraSoundsNext.class.getSimpleName());
                            SUPPRESSED_NAMES.add(className);
                        }
                    }
                }

                if (annotation.defaultLevel() != 1f) {
                    DEFAULT_LEVELS.put(category, annotation.defaultLevel());
                }

                if (annotation.toggle()) {
                    TOGGLEABLE_CATS.put(category, Pair.of(true, annotation.defaultOn()));
                }

                if (!annotation.tooltip().isEmpty()) {
                    TOOLTIPS.put(category, Component.translatable(annotation.tooltip()));
                }
            }
        } catch (Exception ex) {
            ExtraSoundsNext.LOGGER.error("[%s] Unexpected error has caught".formatted(ExtraSoundsNext.class.getSimpleName()), ex);
        }

        // Cleanup.
        SUPPRESSED_NAMES.clear();
    }
}
