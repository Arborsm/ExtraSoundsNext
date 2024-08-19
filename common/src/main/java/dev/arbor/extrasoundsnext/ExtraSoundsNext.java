package dev.arbor.extrasoundsnext;

import com.mojang.logging.LogUtils;
import dev.arbor.extrasoundsnext.debug.DebugUtils;
import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class ExtraSoundsNext {
    public static final String MODID = "extrasounds";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Mixers MIXERS = new Mixers();
    public static final ResourceLocation SETTINGS_ICON = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/settings.png");
    public static final SoundEvent MISSING = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "missing"));
    public static void init() {
        DebugUtils.init();
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
}
