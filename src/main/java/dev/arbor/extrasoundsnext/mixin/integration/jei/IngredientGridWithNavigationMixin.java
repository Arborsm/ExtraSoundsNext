package dev.arbor.extrasoundsnext.mixin.integration.jei;

//? if 1.18.2 {
/*import mezz.jei.common.input.IUserInputHandler;
*///?} elif >=1.21.1 {
import mezz.jei.library.gui.widgets.AbstractScrollWidget;
//?} else {
/*import mezz.jei.gui.input.IUserInputHandler;
*///?}
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;

import java.util.Optional;

//? if 1.18.2 {
/*@Mixin(targets = "mezz.jei.common.gui.overlay.IngredientGridWithNavigation.UserInputHandler")
*///?} else if >=1.21.1 {
@Mixin(targets = "mezz.jei.gui.overlay.IngredientGridWithNavigation$IngredientGridPaged")
//?} else {
/*@Mixin(targets = "mezz.jei.gui.overlay.IngredientGridWithNavigation$UserInputHandler")
*///?}
@MixinEnvironment
public abstract class IngredientGridWithNavigationMixin {
	@Unique
	private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

	//? if >=1.21.1 {
	@Inject(method = "nextPage", at = @At("HEAD"), remap = false)
	private void nextPage(CallbackInfoReturnable<Boolean> cir) {
		extra_sounds$scrollSound.play();
	}

	@Inject(method = "previousPage", at = @At("HEAD"), remap = false)
	private void previousPage(CallbackInfoReturnable<Boolean> cir) {
		extra_sounds$scrollSound.play();
	}

	@MixinEnvironment
	@Mixin(AbstractScrollWidget.class)
	public abstract static class AbstractScrollWidgetMixin {
		@Inject(method = "handleMouseScrolled", at = @At("HEAD"), remap = false)
		private void handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY, CallbackInfoReturnable<Boolean> cir) {
			extra_sounds$scrollSound.play();
		}
	}
	//?} else {
	/*@Inject(method = "handleMouseScrolled", at = @At("HEAD"), remap = false)
	private void handleMouseScrolled(double mouseX, double mouseY, double scrollDelta, CallbackInfoReturnable<Optional<IUserInputHandler>> cir) {
		extra_sounds$scrollSound.play();
	}
	*///?}
}
