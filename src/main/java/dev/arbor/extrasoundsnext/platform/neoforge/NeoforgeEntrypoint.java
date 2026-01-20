package dev.arbor.extrasoundsnext.platform.neoforge;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import net.neoforged.fml.common.Mod;

@Mod(ExtraSoundsNext.MODID)
public final class NeoforgeEntrypoint {
    public NeoforgeEntrypoint() {
        ExtraSoundsNext.init();
        //? if >=1.21.1 {
        /*SoundSourceInit.initCategoryLoader();
        *///?}
    }
}
