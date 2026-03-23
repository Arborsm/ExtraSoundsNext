package dev.arbor.extrasoundsnext.mixin.integration.rei;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import me.shedaniel.rei.impl.client.gui.modules.MenuHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinEnvironment
@Mixin(value = MenuHolder.class,remap = false)
public class MenuHolderMixin {
    @Unique
    private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
	//? if >=1.21.1 {
    private void mouseScrolled(double mouseX, double mouseY, double amountX, double amountY, CallbackInfoReturnable<Boolean> cir) {
	//?} else {
	/*private void mouseScrolled(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {
	*///?}
        extra_sounds$scrollSound.play();
    }
}
