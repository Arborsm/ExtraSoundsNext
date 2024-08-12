package dev.arbor.extrasoundsnext.fabric.client;

import dev.arbor.extrasoundsnext.mapping.SoundPackLoader;
import net.fabricmc.api.ClientModInitializer;

public final class ExtraSoundsNextFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SoundPackLoader.init();
    }
}
