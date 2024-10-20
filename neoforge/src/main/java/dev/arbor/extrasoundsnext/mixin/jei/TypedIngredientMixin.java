package dev.arbor.extrasoundsnext.mixin.jei;

import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.library.focus.Focus;
import mezz.jei.library.ingredients.TypedIngredient;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Focus.class)
@OnlyIn(Dist.CLIENT)
public class TypedIngredientMixin {
    @Inject(method = "createFromApi(Lmezz/jei/api/runtime/IIngredientManager;Lmezz/jei/api/recipe/RecipeIngredientRole;Lmezz/jei/api/ingredients/ITypedIngredient;)Lmezz/jei/library/focus/Focus;", at = @At("RETURN"), remap = false)
    private static <V> void deepCopy(IIngredientManager ingredientManager, RecipeIngredientRole role, ITypedIngredient<V> typedIngredient, CallbackInfoReturnable<Focus<V>> cir) {
        @Nullable ITypedIngredient<V> typedIngredientCopy = TypedIngredient.defensivelyCopyTypedIngredientFromApi(ingredientManager, typedIngredient);
        if (typedIngredientCopy != null) {
            SoundManager.playSound(typedIngredientCopy.getItemStack().orElse(ItemStack.EMPTY), SoundType.PICKUP);
        }
    }
}
