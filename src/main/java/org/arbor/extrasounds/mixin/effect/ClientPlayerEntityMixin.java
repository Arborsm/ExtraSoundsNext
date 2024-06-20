package org.arbor.extrasounds.mixin.effect;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import org.arbor.extrasounds.sounds.SoundManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For player's effect add/remove sound.
 */
@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer {
    public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    protected void onEffectAdded(@NotNull MobEffectInstance effect, @Nullable Entity source) {
        super.onEffectAdded(effect, source);
        SoundManager.effectChanged(effect.getEffect().value(), SoundManager.EffectType.ADD);
    }

    @Inject(method = "removeEffectNoUpdate", at = @At("HEAD"))
    private void extrasounds$effectRemoved(Holder<MobEffect> pEffect, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (this.hasEffect(pEffect)) {
            SoundManager.effectChanged(pEffect.value(), SoundManager.EffectType.REMOVE);
        }
    }
}