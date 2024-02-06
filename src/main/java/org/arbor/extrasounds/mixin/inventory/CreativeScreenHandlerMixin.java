package org.arbor.extrasounds.mixin.inventory;

import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.misc.ESConfig;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Creative screen scroll sound.
 */
@Mixin(CreativeModeInventoryScreen.ItemPickerMenu.class)
public abstract class CreativeScreenHandlerMixin {
    @Unique
    private int extra_sounds$lastPos = 0;
    @Unique
    private long extra_sounds$lastTime = 0L;

    @Shadow
    protected abstract int getRowIndexForScroll(float scroll);

    @Inject(method = "scrollTo", at = @At("HEAD"))
    private void extrasounds$creativeScreenScroll(float position, CallbackInfo ci) {
        final long now = System.currentTimeMillis();
        final long timeDiff = now - extra_sounds$lastTime;
        final int row = this.getRowIndexForScroll(position);
        if (timeDiff > 20 && extra_sounds$lastPos != row && !(extra_sounds$lastPos != 1 && row == 0)) {
            SoundManager.playSound(
                    Sounds.INVENTORY_SCROLL,
                    (1f - 0.1f + 0.1f * Math.min(1, 50f / timeDiff)),
                    SoundSource.PLAYERS, ESConfig.CONFIG.INVENTORY.get().floatValue());
            extra_sounds$lastTime = now;
            extra_sounds$lastPos = row;
        }
    }
}
