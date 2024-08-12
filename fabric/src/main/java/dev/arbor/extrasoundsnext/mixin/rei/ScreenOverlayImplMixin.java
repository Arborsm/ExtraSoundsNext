package dev.arbor.extrasoundsnext.mixin.rei;

import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import me.shedaniel.rei.impl.client.gui.ScreenOverlayImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnstableApiUsage")
@Environment(EnvType.CLIENT)
@Mixin(ScreenOverlayImpl.class)
public class ScreenOverlayImplMixin {
    @Unique
    private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void mouseScrolled(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {
        extra_sounds$scrollSound.play();
    }
}
