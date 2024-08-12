package dev.arbor.extrasoundsnext.mixin.jei;

import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import mezz.jei.gui.input.IUserInputHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(targets = "mezz.jei.gui.overlay.IngredientGridWithNavigation$UserInputHandler")
@Environment(EnvType.CLIENT)
public abstract class IngredientGridWithNavigationMixin {
    @Unique
    private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(method = "handleMouseScrolled", at = @At("HEAD"), remap = false)
    private void handleMouseScrolled(double mouseX, double mouseY, double scrollDelta, CallbackInfoReturnable<Optional<IUserInputHandler>> cir) {
        extra_sounds$scrollSound.play();
    }
}
