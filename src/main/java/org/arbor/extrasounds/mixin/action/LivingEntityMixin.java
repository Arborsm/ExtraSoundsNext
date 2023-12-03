package org.arbor.extrasounds.mixin.action;

import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "makePoofParticles", at = @At("HEAD"))
    private void extrasounds$poofSound(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final float pitch = (float) Mth.clampedLerp(2f, 0.25f,  Math.sqrt(this.getBbHeight() * this.getBbWidth()) / 2.5f);
        SoundManager.playSound(Sounds.Entities.POOF, SoundType.ACTION, .7f, pitch, this.blockPosition());
    }
}
