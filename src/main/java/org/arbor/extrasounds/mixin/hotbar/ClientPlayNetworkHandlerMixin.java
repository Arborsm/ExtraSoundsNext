package org.arbor.extrasounds.mixin.hotbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.item.ItemStack;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Swap with Off-hand action.
 */
@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
    private void extrasounds$hotbarSwapEvent(Packet<?> packet, CallbackInfo ci) {
        if (this.minecraft.player == null) {
            return;
        }
        if (!(packet instanceof ServerboundPlayerActionPacket actionC2SPacket)) {
            return;
        }
        if (actionC2SPacket.getAction() != ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
            return;
        }

        ItemStack itemStack = this.minecraft.player.getOffhandItem();
        if (itemStack.isEmpty()) {
            itemStack = this.minecraft.player.getMainHandItem();
        }
        SoundManager.playSound(itemStack, SoundType.PICKUP);
    }
}
