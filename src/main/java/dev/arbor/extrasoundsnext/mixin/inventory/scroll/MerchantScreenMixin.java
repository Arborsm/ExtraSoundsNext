package dev.arbor.extrasoundsnext.mixin.inventory.scroll;

import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantScreen.class)
@MixinEnvironment()
public class MerchantScreenMixin {
    @Unique
    private final ScrollSound extra_sounds$scrollSound = new ScrollSound();

    @Shadow
    int scrollOff;

    @Inject(method = "mouseScrolled", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;scrollOff:I", opcode = Opcodes.PUTFIELD))
    //? if >= 1.21.1 {
	private void extrasounds$mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
    //?} else {
	/*private void extrasounds$mouseScrolled(double mouseX, double mouseY, double scroll, CallbackInfoReturnable<Boolean> cir) {
    *///?}
        extra_sounds$scrollSound.play(scrollOff);
    }
}
