package dev.arbor.extrasoundsnext.mixin.integration.jei;

//? if 1.18.2 {
/*import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.common.focus.Focus;
import mezz.jei.common.ingredients.RegisteredIngredients;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Focus.class)
@MixinEnvironment
public class TypedIngredientMixin {
	@Inject(method = "checkOne", at = @At("HEAD"), remap = false)
	private static <V> void checkOne(IFocus<V> focus, RegisteredIngredients registeredIngredients, CallbackInfoReturnable<Focus<V>> cir) {
		if (focus instanceof Focus<V> && focus.getTypedValue().getIngredient() instanceof ItemStack itemStack) {
			SoundManager.playSound(Optional.of(itemStack).orElse(ItemStack.EMPTY), SoundType.PICKUP);
		}
	}
}
*///?} elif >= 1.21.1 {
import com.llamalad7.mixinextras.sugar.Local;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.library.focus.Focus;
import net.minecraft.world.item.ItemStack;
//? if neoforge {
import net.neoforged.neoforge.fluids.FluidStack;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Focus.class)
@MixinEnvironment()
public class TypedIngredientMixin {
	@Inject(method = "createFromApi(Lmezz/jei/api/runtime/IIngredientManager;Lmezz/jei/api/recipe/RecipeIngredientRole;Lmezz/jei/api/ingredients/ITypedIngredient;)Lmezz/jei/library/focus/Focus;", at = @At("HEAD"), remap = false)
	private static <V> void createFromApi(IIngredientManager ingredientManager, RecipeIngredientRole role, ITypedIngredient<V> typedIngredient, CallbackInfoReturnable<Focus<V>> cir, @Local() ITypedIngredient<V> typedIngredientCopy) {
		ItemStack itemStack;
		if (typedIngredientCopy.getIngredient() instanceof ItemStack itemStackCopy) {
			itemStack = itemStackCopy;
		//? if neoforge {
		} else if (typedIngredientCopy.getIngredient() instanceof FluidStack fluidStack) {
			itemStack = fluidStack.getFluid().getBucket().getDefaultInstance();
		//?}
		} else {
			return;
		}
		SoundManager.playSound(itemStack, SoundType.PICKUP);
	}
}
//?} else {
/*import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.library.ingredients.TypedIngredient;
import net.minecraft.world.item.ItemStack;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(TypedIngredient.class)
@MixinEnvironment
public class TypedIngredientMixin {
	@Inject(method = "deepCopy", at = @At("TAIL"), remap = false)
	private static <T> void deepCopy(IIngredientManager ingredientManager, ITypedIngredient<T> value, CallbackInfoReturnable<Optional<ITypedIngredient<T>>> cir) {
		SoundManager.playSound(value.getItemStack().orElse(ItemStack.EMPTY), SoundType.PICKUP);
	}
}
*///?}
