/*
 * Copyright 2021 stashymane
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arbor.extrasounds;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.sounds.CategoryLoader;
import org.arbor.extrasounds.sounds.Mixers;
import org.arbor.extrasounds.sounds.SoundType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Field;
import java.util.*;

@Mod(ExtraSounds.MODID)
public class ExtraSounds {
    public static final String MODID = "extrasounds";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Mixers MIXERS = new Mixers();
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
    public static final ResourceLocation SETTINGS_ICON = new ResourceLocation(MODID, "textures/gui/settings.png");
    private static final List<String> SUPPRESSED_NAMES = new ArrayList<>();
    public static String[] MASTER_CLASSES;
    public static final SoundEvent MISSING = SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "missing"));

    public ExtraSounds() {
        initCategoryLoader();
    }

    public static Pair<CategoryLoader, List<Field>> getCategories() {
        return Pair.of(MIXERS, getRegistrations());
    }

    private static List<Field> getRegistrations() {
        return Arrays.stream(ExtraSounds.MIXERS.getClass().getDeclaredFields()).filter(it ->
                it.isAnnotationPresent(CategoryLoader.Register.class)).toList();
    }

    @Nullable
    public static ResourceLocation getClickId(ResourceLocation id, SoundType type) {
        if (id == null || type == null) {
            return null;
        }
        return new ResourceLocation(MODID, "%s.%s.%s".formatted(type.prefix, id.getNamespace(), id.getPath()));
    }

    public static SoundEvent createEvent(String path) {
        try {
            return SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, path));
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSounds.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static SoundEvent createEvent(ResourceLocation path) {
        try {
            return SoundEvent.createVariableRangeEvent(path);
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSounds.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static ResourceLocation id(String id) {
        return new ResourceLocation(ExtraSounds.MODID, id);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            DebugUtils.init();
        }
    }

    private static String generateFieldClassName(Class<?> clazz, Field field) {
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
                        LOGGER.error(
                                "[%s] Cast check failed for the member '%s'.".formatted(ExtraSounds.class.getSimpleName(), fieldClassName),
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
                        LOGGER.warn(
                                "[%s] Unexpected annotation was found.".formatted(ExtraSounds.class.getSimpleName()),
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
                            LOGGER.warn("[{}] Missing annotation value 'master' in class '{}'. This is deprecated.",
                                    ExtraSounds.class.getSimpleName(), className);
                            LOGGER.warn("[{}] To avoid this message, please specify \"master = true\" in one of the @Register annotation in your class.",
                                    ExtraSounds.class.getSimpleName());
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
            LOGGER.error("[%s] Unexpected error has caught".formatted(ExtraSounds.class.getSimpleName()), ex);
        }

        // Cleanup.
        SUPPRESSED_NAMES.clear();
    }
}
