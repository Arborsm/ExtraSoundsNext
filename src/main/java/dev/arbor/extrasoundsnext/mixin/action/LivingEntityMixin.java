package dev.arbor.extrasoundsnext.mixin.action;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import dev.arbor.extrasoundsnext.sounds.Sounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} elif neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
//?} elif forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
*///?}

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} elif neoforge {
@OnlyIn(Dist.CLIENT)
//?} elif forge {
/*@OnlyIn(Dist.CLIENT)
*///?}
@Mixin(LivingEntity.class)
@MixinEnvironment()
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "makePoofParticles", at = @At("HEAD"))
    private void extrasounds$poofSound(CallbackInfo ci) {
        //? if >=26.1 {
        /*if (!this.level().isClientSide()) {
        *///?} else if >=1.20 {
        if (!this.level().isClientSide) {
        //?} else {
        /*if (!this.getCommandSenderWorld().isClientSide) {
        *///?}
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final float pitch = (float) Mth.clampedLerp(2f, 0.25f,  Math.sqrt(this.getBbHeight() * this.getBbWidth()) / 2.5f);
        SoundManager.playSound(Sounds.Entities.POOF, SoundType.ACTION, .7f, pitch, this.blockPosition(), Mixers.ENABLED_POOF);
    }
}
