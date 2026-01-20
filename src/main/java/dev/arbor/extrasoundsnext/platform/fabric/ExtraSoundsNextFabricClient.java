//? if fabric {
/*package dev.arbor.extrasoundsnext.platform.fabric;

import dev.arbor.extrasoundsnext.mapping.SoundPackLoader;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint(Entrypoint.CLIENT)
public final class ExtraSoundsNextFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SoundPackLoader.init();
    }
}
*///?}
