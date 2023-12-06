package org.arbor.extrasounds.mixin.inventory;

import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

@Mixin(AbstractContainerScreen.class)
public class InventoryDragSound
{
    @Shadow
    @Final
    protected Set<Slot> quickCraftSlots;

    @Inject(method = "mouseDragged", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void dragSound(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir, Slot slot)
    {
        if (!quickCraftSlots.contains(slot) && quickCraftSlots.size() > 0)
            SoundManager.playSound(Sounds.ITEM_DRAG, SoundType.PLACE);
    }
}
