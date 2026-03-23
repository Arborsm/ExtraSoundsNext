package dev.arbor.extrasoundsnext.mixin.inventory.scroll;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
//? if >=1.19.3 {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
*///?} else {
import net.minecraft.sounds.SoundEvent;
//?}
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.Sounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.19.3 {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?} else {
import org.spongepowered.asm.mixin.injection.ModifyVariable;
//?}

/**
 * For Creative screen scroll sound.
 */
@Mixin(CreativeModeInventoryScreen.ItemPickerMenu.class)
@MixinEnvironment()
public abstract class CreativeScreenHandlerMixin {
    @Unique
    //? if >=1.19.3 {
    /*private final ScrollSound extra_sounds$scrollSound = new ScrollSound();
    *///?} else {
    private static final SoundEvent extra_sounds$e = Sounds.INVENTORY_SCROLL;
    @Unique
    private static int extra_sounds$lastPos = 0;
    @Unique
    private static long extra_sounds$lastTime = 0L;
    //?}

    //? if >=1.19.3 {
    /*@Shadow
    protected abstract int getRowIndexForScroll(float scroll);

    @Inject(method = "scrollTo", at = @At("HEAD"))
    private void extrasounds$creativeScreenScroll(float position, CallbackInfo ci) {
        final int row = this.getRowIndexForScroll(position);
        extra_sounds$scrollSound.play(row);
    }
    *///?} else {
    @ModifyVariable(method = "scrollTo", at = @At("STORE"), ordinal = 1)
    int scroll(int position) {
        long now = System.currentTimeMillis();
        long timeDiff = now - extra_sounds$lastTime;
        if (timeDiff > 20 && extra_sounds$lastPos != position && !(extra_sounds$lastPos != 1 && position == 0)) {
            SoundManager.playSound(extra_sounds$e, (1f - 0.1f + 0.1f * Math.min(1, 50f / timeDiff)));
            extra_sounds$lastTime = now;
            extra_sounds$lastPos = position;
        }
        return position;
    }
    //?}
}
