package org.arbor.extrasounds.mixin.inventory;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * For Creative screen sound.
 */
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    @Unique
    private static final int GROUP_INVENTORY = CreativeModeTab.TAB_INVENTORY.getId();

    @Shadow
    private static int selectedTab;

    @Shadow
    @Nullable
    private Slot destroyItemSlot;

    @Shadow
    protected abstract boolean checkTabClicked(CreativeModeTab group, double mouseX, double mouseY);
    @Shadow
    protected abstract boolean isCreativeSlot(@Nullable Slot slot);

    public CreativeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "slotClicked", at = @At("HEAD"))
    private void extrasounds$creativeInventoryClickEvent(@Nullable Slot slot, int slotId, int button, ClickType actionType, CallbackInfo ci) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }

        final boolean bOnHotbar = slot != null && !this.isCreativeSlot(slot);

        if (actionType == ClickType.THROW && slot != null && slotId >= 0) {
            // CreativeInventory can drop items while holding anything on the cursor
            final ItemStack slotStack = slot.getItem().copy();
            if (button == 1 && selectedTab != GROUP_INVENTORY) {
                if (bOnHotbar) {
                    // Pressed Ctrl + Q on Hotbar to delete the stack only when not in the Inventory tab
                    SoundManager.playSound(Sounds.ITEM_DELETE, SoundType.PICKUP);
                    return;
                }
                // If not, it pressed on the slot in CreativeInventory tab
                slotStack.setCount(slotStack.getMaxStackSize());
            } else if (button == 0) {
                // Pressed a Q key only
                slotStack.setCount(1);
            }
            SoundManager.playThrow(slotStack);
            return;
        }

        if (actionType == ClickType.QUICK_MOVE &&
                selectedTab != GROUP_INVENTORY &&
                bOnHotbar &&
                slot.hasItem()
        ) {
            SoundManager.playSound(Sounds.ITEM_DELETE, SoundType.PICKUP);
            return;
        }

        final ItemStack cursorStack = this.menu.getCarried().copy();
        if (!cursorStack.isEmpty()) {
            if (this.destroyItemSlot != null && slot == this.destroyItemSlot) {
                // Clicked deleteItemSlot
                SoundManager.playSound(Sounds.ITEM_DELETE, SoundType.PICKUP);
                return;
            }

            if (slotId > 0 &&
                    actionType != ClickType.QUICK_CRAFT &&
                    actionType != ClickType.PICKUP_ALL &&
                    !bOnHotbar
            ) {
                // Clicked on the slot in CreativeInventory tab except Hotbar
                SoundManager.playSound(cursorStack, SoundType.PLACE);
                return;
            }
        }

        SoundManager.handleInventorySlot(this.minecraft.player, slot, slotId, cursorStack, actionType, button);
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void extrasounds$tabChange(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button != 0) {
            return;
        }

        final double screenX = mouseX - this.leftPos;
        final double screenY = mouseY - this.topPos;
        for (CreativeModeTab itemGroup : CreativeModeTab.TABS) {
            if (this.checkTabClicked(itemGroup, screenX, screenY) && selectedTab != 6) {
                SoundManager.playSound(itemGroup.getIconItem(), SoundType.PICKUP);
                return;
            }
        }
    }
}
