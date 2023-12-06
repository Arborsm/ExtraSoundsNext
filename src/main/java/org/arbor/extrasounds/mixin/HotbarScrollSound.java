package org.arbor.extrasounds.mixin;

import org.arbor.extrasounds.ExtraSounds;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class HotbarScrollSound
{
    @Shadow
    public int selected;

    @Inject(at = @At("RETURN"), method = "swapPaint")
    private void hotbarSound(CallbackInfo info)
    {
        ExtraSounds.hotbar(selected);
    }
}
