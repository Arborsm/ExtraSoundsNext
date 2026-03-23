package dev.arbor.extrasoundsnext.mixin.core;

import com.mojang.authlib.GameProfile;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
//? if >=1.21 {
import net.minecraft.core.Holder;
//?}
//? if =1.19.3 {
/*import net.minecraft.client.multiplayer.ProfileKeyPair;
*///?}
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import dev.arbor.extrasoundsnext.sounds.Sounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
@MixinEnvironment()
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer {

    @Unique
    private long extraSoundsNext$lastPlayedTime = 0L;

    @Unique
    private static final long extraSoundsNext$COOLDOWN = 1000L;

    //? if >=1.20 {
    public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }
    //?} elif =1.19.3 {
    /*public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile, ProfileKeyPair profileKeyPair) {
        super(world, profile, profileKeyPair);
    }
    *///?} elif =1.19.2 {
    /*public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile, null);
    }
    *///?} else {
    /*public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }
    *///?}

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

    @Override
    protected void onEffectAdded(@NotNull MobEffectInstance effect, @Nullable Entity source) {
        super.onEffectAdded(effect, source);
        long currentTime = System.currentTimeMillis();
        if (currentTime - extraSoundsNext$lastPlayedTime > extraSoundsNext$COOLDOWN) {
            //? if >=1.21 {
            SoundManager.effectChanged(effect.getEffect().value(), SoundManager.EffectType.ADD);
            //?} else {
            /*SoundManager.effectChanged(effect.getEffect(), SoundManager.EffectType.ADD);
            *///?}
            extraSoundsNext$lastPlayedTime = currentTime;
        }
    }

    @Inject(method = "removeEffectNoUpdate", at = @At("HEAD"))
    //? if >=1.21 {
    private void extrasounds$effectRemoved(Holder<MobEffect> pEffect, CallbackInfoReturnable<MobEffectInstance> cir) {
        long currentTime = System.currentTimeMillis();
        if (this.hasEffect(pEffect) && currentTime - extraSoundsNext$lastPlayedTime > extraSoundsNext$COOLDOWN) {
            SoundManager.effectChanged(pEffect.value(), SoundManager.EffectType.REMOVE);
            extraSoundsNext$lastPlayedTime = currentTime;
        }
    }
    //?} else {
    /*private void extrasounds$effectRemoved(MobEffect pEffect, CallbackInfoReturnable<MobEffectInstance> cir) {
        long currentTime = System.currentTimeMillis();
        if (this.hasEffect(pEffect) && currentTime - extraSoundsNext$lastPlayedTime > extraSoundsNext$COOLDOWN) {
            SoundManager.effectChanged(pEffect, SoundManager.EffectType.REMOVE);
            extraSoundsNext$lastPlayedTime = currentTime;
        }
    }
    *///?}
}
