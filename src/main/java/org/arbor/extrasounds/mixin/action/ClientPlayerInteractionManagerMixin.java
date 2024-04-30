package org.arbor.extrasounds.mixin.action;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.Mixers;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For Repeater block sound.
 */
@Mixin(MultiPlayerGameMode.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow
    private @Final Minecraft minecraft;

    @Inject(method = "useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "RETURN", ordinal = 2))
    private void extrasounds$repeaterSwitchSound(LocalPlayer p_105263_, ClientLevel p_105264_, InteractionHand p_105265_, BlockHitResult p_105266_, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.minecraft.level == null) {
            return;
        }

        final BlockPos blockPos = p_105266_.getBlockPos();
        final BlockState blockState = this.minecraft.level.getBlockState(blockPos);
        if (!blockState.is(Blocks.REPEATER) || !blockState.hasProperty(RepeaterBlock.DELAY)) {
            return;
        }

        if (cir.getReturnValue().consumesAction()) {
            final SoundEvent sound = blockState.getValue(RepeaterBlock.DELAY) == 1 ? Sounds.Actions.REPEATER_RESET : Sounds.Actions.REPEATER_ADD;
            SoundManager.playSound(sound, SoundType.ACTION, Mixers.ENABLED_FOOTSTEP, blockPos);
        }
    }
}
