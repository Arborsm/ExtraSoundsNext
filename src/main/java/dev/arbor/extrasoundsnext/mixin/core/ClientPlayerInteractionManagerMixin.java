package dev.arbor.extrasoundsnext.mixin.core;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import dev.arbor.extrasoundsnext.sounds.Sounds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
@MixinEnvironment()
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow
    private @Final Minecraft minecraft;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "handleInventoryMouseClick", at = @At("HEAD"))
    private void extrasounds$inventoryClickEvent(int syncId, int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci) {
        if (player == null) {
            return;
        }
        AbstractContainerMenu screenHandler = player.containerMenu;
        if (screenHandler == null) {
            return;
        }

        Slot slot = (slotIndex >= 0) ? screenHandler.slots.get(slotIndex) : null;
        SoundManager.handleInventorySlot(player, slot, slotIndex, screenHandler.getCarried(), actionType, button);
    }

    //? if >=1.19 {
    @Inject(method = "performUseItemOn", at = @At(value = "RETURN", ordinal = 2))
    private void extrasounds$repeaterSwitchSound(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.minecraft.level == null) {
            return;
        }

        final BlockPos blockPos = hitResult.getBlockPos();
        final BlockState blockState = this.minecraft.level.getBlockState(blockPos);
        if (!blockState.is(Blocks.REPEATER) || !blockState.hasProperty(RepeaterBlock.DELAY)) {
            return;
        }

        if (cir.getReturnValue().consumesAction()) {
            final SoundEvent sound = blockState.getValue(RepeaterBlock.DELAY) == 1 ? Sounds.Actions.REPEATER_RESET : Sounds.Actions.REPEATER_ADD;
            SoundManager.playSound(sound, SoundType.ACTION, Mixers.ENABLED_FOOTSTEP, blockPos);
        }
    }
    //?}
}
