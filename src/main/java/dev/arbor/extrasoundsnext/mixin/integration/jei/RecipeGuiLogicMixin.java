package dev.arbor.extrasoundsnext.mixin.integration.jei;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
//? if 1.18.2 {
import mezz.jei.common.gui.recipes.RecipeGuiLogic;
//?} else {
/*import mezz.jei.gui.recipes.RecipeGuiLogic;
*///?}
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//? if >=1.21.1 {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?} else {
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

@Mixin(RecipeGuiLogic.class)
@MixinEnvironment
public class RecipeGuiLogicMixin {
	@Unique
	private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

	@Inject(method = "nextPage", at = @At("HEAD"), remap = false)
	//? if >=1.21 {
	/*private void nextPage(CallbackInfoReturnable<Boolean> cir) {
	*///?} else {
		private void nextPage(CallbackInfo ci) {
	//?}
		extra_sounds$scrollSound.play();
	}

	@Inject(method = "previousPage", at = @At("HEAD"), remap = false)
	//? if >=1.21 {
	/*private void previousPage(CallbackInfoReturnable<Boolean> cir) {
	*///?} else {
	private void previousPage(CallbackInfo ci) {
	//?}
		extra_sounds$scrollSound.play();
	}
}
