package dev.arbor.extrasoundsnext.platform.fabric;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.core.sounds.SoundSourceInit;
import net.fabricmc.api.ModInitializer;

public final class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtraSoundsNext.init();
        SoundSourceInit.initCategoryLoader();
    }
}
