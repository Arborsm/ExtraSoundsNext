package dev.arbor.extrasoundsnext.mixin.typing;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
//? if >=1.19.4 {
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
//?} else {
/*import net.minecraft.client.gui.screens.inventory.SignEditScreen;
*///?}
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.19.4 {
@Mixin(AbstractSignEditScreen.class)
//?} else {
/*@Mixin(SignEditScreen.class)
*///?}
@MixinEnvironment()
public abstract class AbstractSignEditScreenMixin {
    @Unique
    private int extra_sounds$previousRow;

    @Shadow
    private int line;

    @Inject(method = "keyPressed", at = @At("RETURN"))
    //? if >=26.1 {
    /*private void extrasounds$moveRow(net.minecraft.client.input.KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
    private void extrasounds$moveRow(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
    //?}
        if (this.line != this.extra_sounds$previousRow) {
            SoundManager.keyboard(SoundManager.KeyType.CURSOR);
            this.extra_sounds$previousRow = this.line;
        }
    }
}
