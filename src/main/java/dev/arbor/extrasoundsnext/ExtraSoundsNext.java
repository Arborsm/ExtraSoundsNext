package dev.arbor.extrasoundsnext;

import com.mojang.logging.LogUtils;
import dev.arbor.extrasoundsnext.api.PlatformHelper;
import dev.arbor.extrasoundsnext.core.sounds.Mixers;
import dev.arbor.extrasoundsnext.core.sounds.SoundType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

//? if fabric {
import dev.arbor.extrasoundsnext.platform.fabric.FabricPlatformHelper;
//?} neoforge {
/*import dev.arbor.extrasoundsnext.platform.neoforge.NeoForgePlatformHelper;
 *///?}

public final class ExtraSoundsNext {
    public static final String MODID = "extrasounds";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Mixers MIXERS = new Mixers();
    public static final ResourceLocation SETTINGS_ICON = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/settings.png");
    public static final SoundEvent MISSING = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "missing"));

    //? if fabric {
    private static final PlatformHelper PLATFORM = new FabricPlatformHelper();
    //?} neoforge {
    /*private static final PlatformHelper PLATFORM = new NeoForgePlatformHelper();
    *///?}

    public static void init() {
        DebugUtils.init();
        //? if neoforge {
        /*SoundSourceInit.initCategoryLoader();
        *///?}
    }

    @Nullable
    public static ResourceLocation getClickId(ResourceLocation id, SoundType type) {
        if (id == null || type == null) {
            return null;
        }
        return ResourceLocation.fromNamespaceAndPath(MODID, "%s.%s.%s".formatted(type.prefix, id.getNamespace(), id.getPath()));
    }

    public static SoundEvent createEvent(String path) {
        try {
            return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, path));
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSoundsNext.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static SoundEvent createEvent(ResourceLocation path) {
        try {
            return SoundEvent.createVariableRangeEvent(path);
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSoundsNext.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(ExtraSoundsNext.MODID, id);
    }

    public static PlatformHelper xplat() {
        return PLATFORM;
    }
}
