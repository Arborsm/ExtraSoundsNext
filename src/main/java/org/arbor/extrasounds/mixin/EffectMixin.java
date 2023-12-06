package org.arbor.extrasounds.mixin;

import com.mojang.authlib.GameProfile;
import org.arbor.extrasounds.SoundManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class EffectMixin extends AbstractClientPlayer
{
    public EffectMixin(ClientLevel world, GameProfile profile, @Nullable ProfilePublicKey publicKey)
    {
        super(world, profile, publicKey);
    }

    @Override
    public boolean addEffect(@NotNull MobEffectInstance effect, @Nullable Entity source)
    {
        var added = super.addEffect(effect, source);
        if (added && !EffectMixin$hasEffect(effect.getEffect()))
            SoundManager.playSound(effect.getEffect(), true);
        return added;
    }

    @Override
    public void forceAddEffect(MobEffectInstance effect, @Nullable Entity source)
    {
        if (!EffectMixin$hasEffect(effect.getEffect()))
            SoundManager.playSound(effect.getEffect(), true);
        super.forceAddEffect(effect, source);
    }

    @Override
    protected void onEffectRemoved(MobEffectInstance effect)
    {
        if (EffectMixin$hasEffect(effect.getEffect())) SoundManager.playSound(effect.getEffect(), false);
        super.onEffectRemoved(effect);
    }

    @Inject(at = @At("HEAD"), method = "removeEffectNoUpdate")
    public void removeStatusEffectInternal(MobEffect type, CallbackInfoReturnable<MobEffectInstance> cir)
    {
        if (EffectMixin$hasEffect(type))
            SoundManager.playSound(type, false);
    }

    @Unique
    private boolean EffectMixin$hasEffect(MobEffect e)
    {
        return getActiveEffectsMap().containsKey(e) && getActiveEffectsMap().get(e).getDuration() > 1;
    }
}