package org.arbor.extrasounds.mixin.emi;

import dev.emi.emi.screen.RecipeScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.arbor.extrasounds.sounds.ScrollSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeScreen.class)
@OnlyIn(Dist.CLIENT)
public class RecipeScreenMixin {
    @Unique
    private final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(
            method = "mouseScrolled",
            at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/RecipeScreen;setPage(III)V", shift = At.Shift.AFTER)
    )
    private void extrasounds$recipeScreenInit(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {
        extra_sounds$scrollSound.play();
    }
}
