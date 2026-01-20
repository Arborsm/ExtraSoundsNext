package dev.arbor.extrasoundsnext;

import com.mojang.logging.LogUtils;
import dev.arbor.extrasoundsnext.debug.DebugUtils;
import dev.arbor.extrasoundsnext.sounds.VolumeConfig;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import reg.ExHelper;

public final class ExtraSoundsNext {
    public static final String MODID = "extrasounds";
    public static final Logger LOGGER = LogUtils.getLogger();
    //? if >=1.21 {
    /*public static final SoundEvent MISSING = SoundEvent.createVariableRangeEvent(ExHelper.rl(MODID, "missing"));
    *///?} elif >=1.19.3 {
    /*public static final SoundEvent MISSING = SoundEvent.createVariableRangeEvent(ExHelper.id("missing"));
    *///?} else {
    public static final SoundEvent MISSING = new SoundEvent(ExHelper.rl("missing"));
    //?}

    public static void init() {
        DebugUtils.init();
        VolumeConfig.load();
    }

    @Nullable
    public static ResourceLocation getClickId(ResourceLocation id, SoundType type) {
        if (id == null || type == null) {
            return null;
        }
        return ExHelper.id("%s.%s.%s".formatted(type.prefix, id.getNamespace(), id.getPath()));
    }

    public static SoundEvent createEvent(String path) {
        try {
            //? if >=1.21 {
            /*return SoundEvent.createVariableRangeEvent(ExHelper.rl(path));
            *///?} elif >=1.19.3 {
            /*return SoundEvent.createVariableRangeEvent(ExHelper.id(path));
            *///?} else {
            return new SoundEvent(ExHelper.rl(path));
            //?}
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSoundsNext.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static SoundEvent createEvent(ResourceLocation path) {
        try {
            //? if >=1.19.3 {
            /*return SoundEvent.createVariableRangeEvent(path);
            *///?} else {
            return new SoundEvent(path);
            //?}
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSoundsNext.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static String getLoader() {
		//? if fabric {
        /*return "Fabric";
		*///?} neoforge {
        /*return "NeoForge";
		*///?} forge {
        return "Forge";
		//?}
    }

    public static boolean isClient() {
		//? if fabric {
        /*return net.fabricmc.loader.api.FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT;
		*///?} elif neoforge {
        /*return net.neoforged.fml.loading.FMLLoader.getDist().isClient();
		*///?} elif forge {
        return net.minecraftforge.fml.loading.FMLLoader.getDist().isClient();
		//?}
    }

    public static boolean isModLoaded(String modId) {
		//? if fabric {
        /*return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modId);
		*///?} elif neoforge {
        /*return net.neoforged.fml.ModList.get().isLoaded(modId);
		*///?} elif forge {
        return net.minecraftforge.fml.ModList.get().isLoaded(modId);
		//?}
    }

    public static String getMinecraftVersion() {
		//? if fabric {
        /*return net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString();
		*///?} neoforge {
        /*return net.neoforged.fml.ModList.get().getModContainerById("minecraft").get().getModInfo().getVersion().toString();
		*///?} forge {
         return net.minecraftforge.fml.ModList.get().getModContainerById("minecraft").get().getModInfo().getVersion().toString();
		//?}
    }

    public static String getModVersion() {
		//? if fabric {
        /*return net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("extrasounds").get().getMetadata().getVersion().getFriendlyString();
		*///?} neoforge {
         /*return net.neoforged.fml.ModList.get().getModContainerById("extrasounds").get().getModInfo().getVersion().toString();
		*///?} forge {
         return net.minecraftforge.fml.ModList.get().getModContainerById("extrasounds").get().getModInfo().getVersion().toString();
		//?}
    }
}
