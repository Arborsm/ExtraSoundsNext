package org.arbor.extrasounds.mixin.inventory;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.Mixers;
import org.arbor.extrasounds.sounds.Sounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * For Creative screen scroll sound.
 */
@Mixin(CreativeModeInventoryScreen.ItemPickerMenu.class)
public abstract class CreativeScreenHandlerMixin {
    @Unique
    private static final SoundEvent extra_sounds$e = Sounds.INVENTORY_SCROLL;
    @Unique
    private static int extra_sounds$lastPos = 0;
    @Unique
    private static long extra_sounds$lastTime = 0L;

    @ModifyVariable(method = "scrollTo", at = @At("STORE"), ordinal = 1)
    int scroll(int position) {
        long now = System.currentTimeMillis();
        long timeDiff = now - extra_sounds$lastTime;
        if (timeDiff > 20 && extra_sounds$lastPos != position && !(extra_sounds$lastPos != 1 && position == 0)) {
            SoundManager.playSound(
                    extra_sounds$e,
                    (1f - 0.1f + 0.1f * Math.min(1, 50f / timeDiff)),
                    SoundSource.PLAYERS, Mixers.INVENTORY);
            extra_sounds$lastTime = now;
            extra_sounds$lastPos = position;
        }
        return position;
    }
}
