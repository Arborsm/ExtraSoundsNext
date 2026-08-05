package dev.arbor.extrasoundsnext.mixin.integration.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import dev.arbor.extrasoundsnext.integration.jei.JeiRuntimeSoundHandler;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
@MixinEnvironment
public abstract class JeiKeyboardInputMixin {
	@Inject(method = "keyPress", at = @At("HEAD"))
	private void extrasounds$jeiKeyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
		if (action != 1) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.screen == null) {
			return;
		}

		Window window = minecraft.getWindow();
		double mouseX = minecraft.mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth();
		double mouseY = minecraft.mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight();
		JeiRuntimeSoundHandler.handleKeyPressed(mouseX, mouseY, InputConstants.getKey(key, scanCode));
	}
}
