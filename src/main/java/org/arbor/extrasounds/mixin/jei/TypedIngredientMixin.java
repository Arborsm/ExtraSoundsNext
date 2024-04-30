package org.arbor.extrasounds.mixin.jei;

import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.common.ingredients.RegisteredIngredients;
import mezz.jei.common.ingredients.TypedIngredient;
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

@Mixin(TypedIngredient.class)
@OnlyIn(Dist.CLIENT)
public class TypedIngredientMixin {
    @Inject(method = "deepCopy", at = @At("TAIL"), remap = false)
    private static <T> void deepCopy(RegisteredIngredients registeredIngredients, ITypedIngredient<T> value, CallbackInfoReturnable<Optional<ITypedIngredient<T>>> cir) {
        SoundManager.playSound(value.getItemStack().orElse(ItemStack.EMPTY), SoundType.PICKUP);
    }
}
