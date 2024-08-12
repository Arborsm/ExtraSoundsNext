package dev.arbor.extrasoundsnext.mixin.typing;

import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin {
    @Unique
    private int extra_sounds$previousRow;

    @Shadow
    private int line;

    @Inject(method = "keyPressed", at = @At("RETURN"))
    private void extrasounds$moveRow(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.line != this.extra_sounds$previousRow) {
            SoundManager.keyboard(SoundManager.KeyType.CURSOR);
            this.extra_sounds$previousRow = this.line;
        }
    }
}
