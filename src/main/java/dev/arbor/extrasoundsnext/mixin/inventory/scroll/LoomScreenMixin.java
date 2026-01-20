package dev.arbor.extrasoundsnext.mixin.inventory.scroll;

import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >=1.19 {
/*@Mixin(LoomScreen.class)
@MixinEnvironment()
public class LoomScreenMixin {
    @Unique
    private final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Shadow
    private int startRow;

    @Inject(method = "mouseScrolled", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/LoomScreen;startRow:I"))
    private void extrasounds$mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
        extra_sounds$scrollSound.play(startRow);
    }
}
*///?} else {
@Mixin(LoomScreen.class)
@MixinEnvironment()
public class LoomScreenMixin {
    // LoomScreen.startRow field does not exist or is not accessible in 1.18.2
    // This mixin is disabled for this version
}
//?}