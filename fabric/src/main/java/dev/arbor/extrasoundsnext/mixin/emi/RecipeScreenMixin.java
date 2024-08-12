package dev.arbor.extrasoundsnext.mixin.emi;

import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import dev.emi.emi.screen.RecipeScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeScreen.class)
@Environment(EnvType.CLIENT)
public class RecipeScreenMixin {
    @Unique
    private final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(method = "setPage", at = @At("HEAD"), remap = false)
    private void extrasounds$recipeScreenInit(int tp, int t, int p, CallbackInfo ci) {
        extra_sounds$scrollSound.play();
    }
}
