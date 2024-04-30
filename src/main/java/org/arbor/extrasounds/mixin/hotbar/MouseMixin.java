package org.arbor.extrasounds.mixin.hotbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.arbor.extrasounds.misc.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Hotbar scroll action.
 */
@Mixin(MouseHandler.class)
public abstract class MouseMixin {
    @Shadow
    private @Final Minecraft minecraft;

    @Inject(
            method = "onScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$hotbarScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        final LocalPlayer player = this.minecraft.player;
        if (player == null) {
            return;
        }

        SoundManager.hotbar(player.getInventory().selected);
    }
}
