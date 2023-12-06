package org.arbor.extrasounds.mixin.effect;

import com.mojang.authlib.GameProfile;
import org.arbor.extrasounds.SoundManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
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
        super(world, profile, null);
    }

    @Override
    protected void onEffectAdded(@NotNull MobEffectInstance effect, @Nullable Entity source) {
        super.onEffectAdded(effect, source);
        SoundManager.effectChanged(effect.getEffect(), SoundManager.EffectType.ADD);
    }

    @Inject(method = "removeEffectNoUpdate", at = @At("HEAD"))
    private void extrasounds$effectRemoved(MobEffect type, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (this.hasEffect(type)) {
            SoundManager.effectChanged(type, SoundManager.EffectType.REMOVE);
        }
    }
}