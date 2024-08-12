package dev.arbor.extrasoundsnext.mixin.jei;

import mezz.jei.gui.recipes.RecipeGuiLogic;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeGuiLogic.class)
@OnlyIn(Dist.CLIENT)
public class RecipeGuiLogicMixin {
    @Unique
    private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(method = "nextPage", at = @At("HEAD"), remap = false)
    private void nextPage(CallbackInfo ci) {
        extra_sounds$scrollSound.play();
    }

    @Inject(method = "previousPage", at = @At("HEAD"), remap = false)
    private void previousPage(CallbackInfo ci) {
        extra_sounds$scrollSound.play();
    }
}
