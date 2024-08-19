package dev.arbor.extrasoundsnext.mixin.jei;

import mezz.jei.gui.input.IUserInputHandler;
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(targets = "mezz.jei.gui.overlay.IngredientGridWithNavigation$UserInputHandler")
@OnlyIn(Dist.CLIENT)
public abstract class IngredientGridWithNavigationMixin {
    @Unique
    private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(method = "handleMouseScrolled", at = @At("HEAD"), remap = false)
    private void handleMouseScrolled(double mouseX, double mouseY, double amountX, double amountY, CallbackInfoReturnable<Optional<IUserInputHandler>> cir) {
        extra_sounds$scrollSound.play();
    }
}
