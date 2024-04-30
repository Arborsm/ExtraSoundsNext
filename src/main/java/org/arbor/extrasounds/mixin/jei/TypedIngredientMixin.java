package org.arbor.extrasounds.mixin.jei;

import mezz.jei.api.recipe.IFocus;
import mezz.jei.common.focus.Focus;
import mezz.jei.common.ingredients.RegisteredIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Focus.class)
@OnlyIn(Dist.CLIENT)
public class TypedIngredientMixin {
    @Inject(method = "checkOne", at = @At("HEAD"), remap = false)
    private static <V> void checkOne(IFocus<V> focus, RegisteredIngredients registeredIngredients, CallbackInfoReturnable<Focus<V>> cir) {
        if (focus instanceof Focus<V> && focus.getTypedValue().getIngredient() instanceof ItemStack itemStack) {
            SoundManager.playSound(Optional.of(itemStack).orElse(ItemStack.EMPTY), SoundType.PICKUP);
        }
    }
}
