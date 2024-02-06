package org.arbor.extrasounds.mixin.action;

import com.mojang.authlib.GameProfile;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Bow pull sound.
 */
@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer {
    public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "startUsingItem", at = @At("HEAD"))
    private void extrasounds$bowPullSound(InteractionHand hand, CallbackInfo ci) {
        if (!this.getItemInHand(hand).is(Items.BOW)) {
            return;
        }

        SoundManager.playSound(Sounds.Actions.BOW_PULL, SoundType.ACTION);
    }

    @Inject(method = "stopUsingItem", at = @At(value = "HEAD"))
    private void extrasounds$cancelPullSound(CallbackInfo ci) {
        if (!this.useItem.is(Items.BOW)) {
            return;
        }

        SoundManager.stopSound(Sounds.Actions.BOW_PULL, SoundType.ACTION);
    }
}
