package org.arbor.extrasounds.mixin.inventory;

import org.arbor.extrasounds.SoundManager;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For Inventory screen sounds.
 */
@Mixin(MultiPlayerGameMode.class)
public abstract class ClientPlayerInteractionManagerMixin {
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
}
