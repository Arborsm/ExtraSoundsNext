package dev.arbor.extrasoundsnext.mixin.inventory;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import dev.arbor.extrasoundsnext.sounds.Sounds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

/**
 * For {@link net.minecraft.world.inventory.ClickType#QUICK_CRAFT} sound on Inventory.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin {
    @Shadow
    protected @Final Set<Slot> quickCraftSlots;

    @SuppressWarnings("all")
    @Inject(method = "mouseDragged", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void extrasounds$quickCraftSound(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir, Slot slot) {
        if (!quickCraftSlots.contains(slot) && !quickCraftSlots.isEmpty()) {
            SoundManager.playSound(Sounds.ITEM_DRAG, SoundType.PLACE);
        }
    }
}
