package dev.arbor.extrasoundsnext.mixin.integration.jei;

import com.mojang.blaze3d.platform.Window;
import dev.arbor.extrasoundsnext.integration.jei.JeiRuntimeSoundHandler;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
//? if >=26.1 {
/*import net.minecraft.client.input.MouseButtonInfo;
*///?}
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
@MixinEnvironment
public abstract class JeiMouseInputMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	//? if >=26.1 {
	/*@Inject(method = "onButton", at = @At("HEAD"))
	private void extrasounds$jeiMouseReleased(long windowPointer, MouseButtonInfo mouseButtonInfo, int action, CallbackInfo ci) {
	*///?} else {
	@Inject(method = "onPress", at = @At("HEAD"))
	private void extrasounds$jeiMouseReleased(long windowPointer, int button, int action, int modifiers, CallbackInfo ci) {
	//?}
		//? if >=26.2 {
		/*if (action != 0 || this.minecraft.gui.screen() == null) {
		*///?} else {
		if (action != 0 || this.minecraft.screen == null) {
		//?}
			return;
		}

		double mouseX = scaledMouseX();
		double mouseY = scaledMouseY();
		//? if >=26.1 {
		/*JeiRuntimeSoundHandler.handleMouseReleased(mouseX, mouseY, mouseButtonInfo.button());
		*///?} else {
		JeiRuntimeSoundHandler.handleMouseReleased(mouseX, mouseY, button);
		//?}
	}

	@Inject(method = "onScroll", at = @At("HEAD"))
	private void extrasounds$jeiMouseScrolled(long windowPointer, double horizontal, double vertical, CallbackInfo ci) {
		//? if >=26.2 {
		/*if (this.minecraft.gui.screen() == null) {
		*///?} else {
		if (this.minecraft.screen == null) {
		//?}
			return;
		}

		double mouseX = scaledMouseX();
		double mouseY = scaledMouseY();
		JeiRuntimeSoundHandler.handleMouseScrolled(mouseX, mouseY, vertical);
	}

	private double scaledMouseX() {
		Window window = this.minecraft.getWindow();
		return this.minecraft.mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth();
	}

	private double scaledMouseY() {
		Window window = this.minecraft.getWindow();
		return this.minecraft.mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight();
	}
}
