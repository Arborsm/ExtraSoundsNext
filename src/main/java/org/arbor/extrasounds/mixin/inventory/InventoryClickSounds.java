package org.arbor.extrasounds.mixin.inventory;

import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractContainerMenu.class)
public abstract class InventoryClickSounds
{
    @Shadow
    @Final
    public NonNullList<Slot> slots;

    @Shadow
    public abstract ItemStack getCarried();

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V"), method = "ifPresent")
    void pickup(Slot slot, Player playerEntity, ItemStack stack, CallbackInfo ci)
    {
        if (!stack.isEmpty())
            SoundManager.playSound(stack, SoundType.PICKUP);
    }

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V"), method = "doClick")
    void click(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci)
    {
        if (slotIndex >= 0)
            ExtraSounds.inventoryClick(slots.get(slotIndex), getCarried(), actionType);
    }

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"), method = "doClick")
    void transferAll(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci)
    {
        if (slotIndex >= 0)
            ExtraSounds.inventoryClick(slots.get(slotIndex), getCarried(), actionType);
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", ordinal = 0, target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;quickMoveStack(Lnet/minecraft/world/entity/player/Player;I)Lnet/minecraft/world/item/ItemStack;"), method = "doClick", locals = LocalCapture.CAPTURE_FAILSOFT)
    void transfer(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci, Inventory playerInventory, ClickAction clickType, Slot slot, ItemStack itemStack)
    {
        if (!itemStack.isEmpty())
            SoundManager.playSound(itemStack, SoundType.PLACE);
    }
}

@Mixin(AbstractContainerScreen.class)
abstract
class InventoryKeyPressSound<T extends AbstractContainerMenu>
{
    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    @Inject(at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ClickType;)V"), method = "checkHotbarKeyPressed")
    void handSwap(int keyCode, int scanCode, CallbackInfoReturnable<Boolean> cir)
    {
        if (hoveredSlot != null && hoveredSlot.hasItem())
            SoundManager.playSound(hoveredSlot.getItem(), SoundType.PICKUP);
    }

    @Inject(at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ClickType;)V"), method = "checkHotbarKeyPressed", locals = LocalCapture.CAPTURE_FAILSOFT)
    void slotSwap(int keyCode, int scanCode, CallbackInfoReturnable<Boolean> cir, int i)
    {
        if (hoveredSlot != null && hoveredSlot.hasItem())
            SoundManager.playSound(hoveredSlot.getItem(), SoundType.PICKUP);
        else if (Minecraft.getInstance().player != null)
        {
            var stack = Minecraft.getInstance().player.getInventory().items.get(i);
            if (!stack.isEmpty())
                SoundManager.playSound(stack, SoundType.PICKUP);
        }
    }
}
