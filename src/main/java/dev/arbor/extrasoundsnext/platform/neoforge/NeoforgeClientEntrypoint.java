package dev.arbor.extrasoundsnext.platform.neoforge;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import net.neoforged.bus.api.IClientEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public final class NeoforgeClientEntrypoint {
    public NeoforgeClientEntrypoint(IClientEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        ExtraSoundsNext.LOGGER.info("ExtraSounds Client initialized on NeoForge");
    }
}
