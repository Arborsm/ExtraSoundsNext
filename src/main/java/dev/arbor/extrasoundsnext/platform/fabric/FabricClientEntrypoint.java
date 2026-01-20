package dev.arbor.extrasoundsnext.platform.fabric;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClientEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExtraSoundsNext.LOGGER.info("ExtraSounds Client initialized on Fabric");
    }
}
