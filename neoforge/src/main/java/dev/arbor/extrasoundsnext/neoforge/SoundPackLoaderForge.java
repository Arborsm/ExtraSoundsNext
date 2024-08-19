package dev.arbor.extrasoundsnext.neoforge;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.mapping.SoundPackLoader;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = ExtraSoundsNext.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SoundPackLoaderForge {
    @SubscribeEvent
    static void onRegisterEvent(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE))
            return;
        SoundPackLoader.init();
    }
}
