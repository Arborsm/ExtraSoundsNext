package dev.arbor.extrasoundsnext.mixin.inventory.scroll;

import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StonecutterScreen.class)
public class StonecutterScreenMixin {
    @Unique
    private final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Shadow
    private int startIndex;

    @Inject(method = "mouseScrolled", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/StonecutterScreen;startIndex:I"))
    private void extrasounds$mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
        extra_sounds$scrollSound.play(startIndex);
    }
}
