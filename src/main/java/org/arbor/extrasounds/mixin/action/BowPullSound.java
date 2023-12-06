package org.arbor.extrasounds.mixin.action;

import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public class BowPullSound
{
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResultHolder;consume(Ljava/lang/Object;)Lnet/minecraft/world/InteractionResultHolder;"), method = "use")
    void pull(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir)
    {
        SoundManager.playSound(Sounds.Actions.BOW_PULL, SoundType.ACTION);
    }

    @Inject(at = @At(value = "RETURN"), method = "releaseUsing")
    void shoot(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks, CallbackInfo ci)
    {
        SoundManager.stopSound(Sounds.Actions.BOW_PULL, SoundType.ACTION);
    }
}
