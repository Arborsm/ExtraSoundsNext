package org.arbor.extrasounds;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.mapping.SoundPackLoader;
import org.arbor.extrasounds.misc.ESConfig;
import org.arbor.extrasounds.sounds.SoundType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Mod(ExtraSounds.MODID)
public class ExtraSounds {

    public static final String MODID = "extrasounds";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final SoundEvent MISSING = SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "missing"));

    public ExtraSounds() {
        DebugUtils.init();
        SoundPackLoader.init();
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ESConfig.configSpec);
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

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
