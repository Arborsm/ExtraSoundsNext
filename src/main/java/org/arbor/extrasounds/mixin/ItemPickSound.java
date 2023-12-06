package org.arbor.extrasounds.mixin;

import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class ItemPickSound
{
    @Shadow
    @Final
    public Player player;

    @Inject(method = "setPickedItem", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"),
            @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;pickSlot(I)V"),
            @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I")
    })
    void pickSound(ItemStack stack, CallbackInfo ci)
    {
        if (!player.getMainHandItem().getItem().equals(stack.getItem()))
            SoundManager.playSound(stack, SoundType.PICKUP);
    }
}
