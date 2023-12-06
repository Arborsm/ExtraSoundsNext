package org.arbor.extrasounds;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.mapping.SoundPackLoader;
import org.arbor.extrasounds.sounds.SoundType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.forge.RRPEvent;

@Mod(ExtraSounds.MODID)
public class ExtraSounds {
    public static final String MODID = "extrasounds";
    public static final RuntimeResourcePack pack = RuntimeResourcePack.create(new ResourceLocation(MODID, "sounds_pack"));
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExtraSounds() {
        DebugUtils.init();
        SoundPackLoader.init();
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RRPEvent.BeforeVanilla event) -> event.addPack(pack));
    }

    @Nullable
    public static ResourceLocation getClickId(ResourceLocation id, SoundType type) {
        if (id == null || type == null) {
            return null;
        }
        return new ResourceLocation(MODID, "%s.%s.%s".formatted(type.prefix, id.getNamespace(), id.getPath()));
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
