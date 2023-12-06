package org.arbor.extrasounds.mixin.inventory;

import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryClickSounds
        extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu>
{

    @Shadow
    private static int selectedTab;

    @Shadow
    @Nullable
    private Slot destroyItemSlot;

    @Shadow
    @Nullable
    private List<Slot> originalSlots;

    public CreativeInventoryClickSounds(CreativeModeInventoryScreen.ItemPickerMenu screenHandler, Inventory playerInventory, Component text)
    {
        super(screenHandler, playerInventory, text);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"), method = "slotClicked")
    void increment(Slot slot, int slotId, int button, ClickType actionType, CallbackInfo ci)
    {
        if (slotId >= 0)
            ExtraSounds.inventoryClick(slot, menu.getCarried(), actionType);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$ItemPickerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V"), method = "slotClicked")
    void click(Slot slot, int slotId, int button, ClickType actionType, CallbackInfo ci)
    {
        if (slot == destroyItemSlot && !menu.getCarried().isEmpty())
            SoundManager.playSound(Sounds.ITEM_DELETE, SoundType.PICKUP);
        else if (slotId >= 0)
            ExtraSounds.inventoryClick(slot, menu.getCarried(), actionType);
    }

    @Inject(at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handleCreativeModeItemAdd(Lnet/minecraft/world/item/ItemStack;I)V"), method = "slotClicked")
    void deleteAll(Slot slot, int slotId, int button, ClickType actionType, CallbackInfo ci)
    {
        if (originalSlots != null && originalSlots.stream().anyMatch(Slot::hasItem))
            SoundManager.playSound(Sounds.ITEM_DELETE, SoundType.PICKUP);
    }

    @Inject(at = @At("HEAD"), method = "selectTab")
    void tabChange(CreativeModeTab group, CallbackInfo ci)
    {
        if (selectedTab != -1 && group.getId() != selectedTab)
            SoundManager.playSound(group.getIconItem(), SoundType.PICKUP);
    }
}

@Mixin(CreativeModeInventoryScreen.ItemPickerMenu.class)
class CreativeScreenHandlerSounds
{
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;set(Lnet/minecraft/world/item/ItemStack;)V"), method = "quickMoveStack")
    void transfer(Player player, int index, CallbackInfoReturnable<ItemStack> cir)
    {
        SoundManager.playSound(Sounds.ITEM_DELETE, SoundType.PICKUP);
    }
}