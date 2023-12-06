package org.arbor.extrasounds.mixin.inventory;

import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class InventoryDropSound
{
    @Inject(at = @At("TAIL"), method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;")
    private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir)
    {
        if (retainOwnership && !stack.isEmpty())
        {
            float range = 0.1f;
            float n = 1f + range *
                    (1f * stack.getItem().getMaxStackSize() / stack.getCount()) - range / 2;
            SoundManager.playSound(Sounds.ITEM_DROP, n, SoundSource.PLAYERS);
        }
    }
}
