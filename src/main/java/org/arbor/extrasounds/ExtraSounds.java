package org.arbor.extrasounds;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.mapping.SoundPackLoader;
import org.arbor.extrasounds.sounds.CategoryLoader;
import org.arbor.extrasounds.sounds.Mixers;
import org.arbor.extrasounds.sounds.SoundType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.forge.RRPEvent;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Field;
import java.util.*;

@Mod(ExtraSounds.MODID)
public class ExtraSounds {
    public static final String MODID = "extrasounds";
    public static final RuntimeResourcePack pack = RuntimeResourcePack.create(new ResourceLocation(MODID, "sounds_pack"));
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

    public static ResourceLocation id(String id) {
        return new ResourceLocation(MODID, id);
    }

    public ExtraSounds() {
        DebugUtils.init();
        SoundPackLoader.init();
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RRPEvent.BeforeVanilla event) -> event.addPack(pack));
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
                    TOOLTIPS.put(category, new TranslatableComponent(annotation.tooltip()));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("[%s] Unexpected error has caught".formatted(ExtraSounds.class.getSimpleName()), ex);
        }

        // Cleanup.
        SUPPRESSED_NAMES.clear();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            DebugUtils.init();
        }
    }
}
