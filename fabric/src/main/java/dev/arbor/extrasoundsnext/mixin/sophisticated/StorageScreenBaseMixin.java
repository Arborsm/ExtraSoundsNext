package dev.arbor.extrasoundsnext.mixin.sophisticated;

import dev.arbor.extrasoundsnext.sounds.SoundManager;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"unchecked", "rawtypes"})
@Mixin(StorageScreenBase.class)
public abstract class StorageScreenBaseMixin extends AbstractContainerScreen {

    public StorageScreenBaseMixin(AbstractContainerMenu arg, Inventory arg2, Component arg3) {
        super(arg, arg2, arg3);
    }

    @Inject(method = "slotClicked", at = @At("HEAD"))
    public void slotClicked(Slot slot, int slotId, int button, ClickType actionType, CallbackInfo ci) {
        if (this.minecraft == null || this.minecraft.player == null) {
            return;
        }

        ItemStack mouseItem = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
        SoundManager.handleInventorySlot(this.minecraft.player, slot, slotId, mouseItem, actionType, button);
    }
}
