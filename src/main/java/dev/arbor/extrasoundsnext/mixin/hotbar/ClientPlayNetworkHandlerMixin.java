package dev.arbor.extrasoundsnext.mixin.hotbar;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
//? if >=1.20.2 {
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
//?} else {
/*import net.minecraft.client.multiplayer.ClientPacketListener;
*///?}
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.item.ItemStack;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Swap with Off-hand action.
 */
//? if >=1.20.2 {
@Mixin(ClientCommonPacketListenerImpl.class)
//?} else {
/*@Mixin(ClientPacketListener.class)
*///?}
@MixinEnvironment()
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow @Final
	private Minecraft minecraft;

    //? if >=1.20.2 {
    @Inject(method = "send", at = @At("HEAD"))
    //?} else {
    /*@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
    *///?}
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
