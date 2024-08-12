package dev.arbor.extrasoundsnext.fabric;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.sounds.SoundSouceInit;
import net.fabricmc.api.ModInitializer;

public final class ExtraSoundsNextFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtraSoundsNext.init();
        SoundSouceInit.initCategoryLoader();
    }
}
