package org.arbor.extrasounds.mixin.jei;

import mezz.jei.common.input.IUserInputHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.arbor.extrasounds.sounds.ScrollSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(targets = "mezz.jei.common.gui.overlay.IngredientGridWithNavigation$UserInputHandler")
@OnlyIn(Dist.CLIENT)
public abstract class IngredientGridWithNavigationMixin {
    @Unique
    private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(method = "handleMouseScrolled", at = @At("HEAD"), remap = false)
    private void handleMouseScrolled(double mouseX, double mouseY, double scrollDelta, CallbackInfoReturnable<Optional<IUserInputHandler>> cir) {
        extra_sounds$scrollSound.play();
    }
}
