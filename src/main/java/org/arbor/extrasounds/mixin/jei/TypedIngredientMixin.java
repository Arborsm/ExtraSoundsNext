package org.arbor.extrasounds.mixin.jei;

import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.library.ingredients.TypedIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.arbor.extrasounds.sounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(TypedIngredient.class)
@OnlyIn(Dist.CLIENT)
public class TypedIngredientMixin {
    @Inject(method = "deepCopy", at = @At("TAIL"))
    private static <T> void deepCopy(IIngredientManager ingredientManager, ITypedIngredient<T> value, CallbackInfoReturnable<Optional<ITypedIngredient<T>>> cir) {
        SoundManager.playSound(value.getItemStack().orElse(ItemStack.EMPTY), SoundType.PICKUP);
    }
}
