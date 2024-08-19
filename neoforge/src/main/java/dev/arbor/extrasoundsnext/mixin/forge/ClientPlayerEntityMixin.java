package dev.arbor.extrasoundsnext.mixin.forge;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.item.ItemStack;
import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * For Hotbar drop action.
 */
@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin {
    @Inject(method = "drop", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void extrasounds$hotbarItemDrop(boolean bl, CallbackInfoReturnable<Boolean> cir, ServerboundPlayerActionPacket.Action action, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            SoundManager.playThrow(itemStack, Mixers.HOTBAR);
        }
    }
}
