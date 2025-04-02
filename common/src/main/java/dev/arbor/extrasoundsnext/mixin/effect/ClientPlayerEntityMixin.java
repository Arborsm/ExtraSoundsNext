package dev.arbor.extrasoundsnext.mixin.effect;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For player's effect add/remove sound.
 */
@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer {
    @Unique
    private static Long extraSoundsNext$lastPlayedTimes = 0L;
    @Unique
    private static final long extraSoundsNext$cooldown = 1000;

    public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    protected void onEffectAdded(@NotNull MobEffectInstance effect, @Nullable Entity source) {
        super.onEffectAdded(effect, source);
        long currentTime = System.currentTimeMillis();
        if (currentTime - extraSoundsNext$lastPlayedTimes > extraSoundsNext$cooldown) {
            SoundManager.effectChanged(effect.getEffect().value(), SoundManager.EffectType.ADD);
            extraSoundsNext$lastPlayedTimes = currentTime;
        }
    }

    @Inject(method = "removeEffectNoUpdate", at = @At("HEAD"))
    private void extrasounds$effectRemoved(Holder<MobEffect> pEffect, CallbackInfoReturnable<MobEffectInstance> cir) {
        long currentTime = System.currentTimeMillis();
        if (this.hasEffect(pEffect) && currentTime - extraSoundsNext$lastPlayedTimes > extraSoundsNext$cooldown) {
            SoundManager.effectChanged(pEffect.value(), SoundManager.EffectType.REMOVE);
            extraSoundsNext$lastPlayedTimes = currentTime;
        }
    }
}