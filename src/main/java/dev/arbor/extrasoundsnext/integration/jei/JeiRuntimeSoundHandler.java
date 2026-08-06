package dev.arbor.extrasoundsnext.integration.jei;

import com.mojang.blaze3d.platform.InputConstants;
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
//? if >1.18.2 {
import mezz.jei.api.runtime.IClickableIngredient;
import mezz.jei.api.runtime.IJeiKeyMapping;
//?}
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
//? if neoforge {
import net.neoforged.neoforge.fluids.FluidStack;
//?} elif forge {
/*import net.minecraftforge.fluids.FluidStack;
*///?}

import java.util.Optional;

public final class JeiRuntimeSoundHandler {
	private static final ScrollSound SCROLL_SOUND = new ScrollSound();
	private static IJeiRuntime runtime;

	private JeiRuntimeSoundHandler() {
	}

	public static void setRuntime(IJeiRuntime jeiRuntime) {
		runtime = jeiRuntime;
	}

	public static void clearRuntime() {
		runtime = null;
	}

	public static void playJeiNavigationSound() {
		SCROLL_SOUND.play();
	}

	public static void handleMouseReleased(double mouseX, double mouseY, int button) {
		//? if >1.18.2 {
		IJeiRuntime jeiRuntime = runtime;
		if (jeiRuntime == null) {
			return;
		}

		InputConstants.Key key = InputConstants.Type.MOUSE.getOrCreate(button);
		if (matches(jeiRuntime.getKeyMappings().getShowRecipe(), key) ||
				matches(jeiRuntime.getKeyMappings().getShowUses(), key)) {
			playHoveredIngredient(mouseX, mouseY, false);
		}
		//?}
	}

	public static void handleKeyPressed(double mouseX, double mouseY, InputConstants.Key key) {
		//? if >1.18.2 {
		IJeiRuntime jeiRuntime = runtime;
		if (jeiRuntime == null) {
			return;
		}

		if (matches(jeiRuntime.getKeyMappings().getShowRecipe(), key) ||
				matches(jeiRuntime.getKeyMappings().getShowUses(), key)) {
			playHoveredIngredient(mouseX, mouseY, true);
		}
		//?}
	}

	public static void handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaY) {
		if (scrollDeltaY != 0 && findHoveredItemStack(mouseX, mouseY, false).isPresent()) {
			playJeiNavigationSound();
		}
	}

	//? if >1.18.2 {
	private static boolean matches(IJeiKeyMapping keyMapping, InputConstants.Key key) {
		return !keyMapping.isUnbound() && keyMapping.isActiveAndMatches(key);
	}
	//?}

	private static void playHoveredIngredient(double mouseX, double mouseY, boolean includeScreenHelper) {
		findHoveredItemStack(mouseX, mouseY, includeScreenHelper)
				.ifPresent(itemStack -> SoundManager.playSound(itemStack, SoundType.PICKUP));
	}

	private static Optional<ItemStack> findHoveredItemStack(double mouseX, double mouseY, boolean includeScreenHelper) {
		IJeiRuntime jeiRuntime = runtime;
		if (jeiRuntime == null) {
			return Optional.empty();
		}

		Optional<ItemStack> itemStack = jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse()
				.flatMap(JeiRuntimeSoundHandler::toItemStack);
		if (itemStack.isPresent()) {
			return itemStack;
		}

		itemStack = jeiRuntime.getBookmarkOverlay().getIngredientUnderMouse()
				.flatMap(JeiRuntimeSoundHandler::toItemStack);
		if (itemStack.isPresent()) {
			return itemStack;
		}

		//? if 1.18.2 {
		/*ItemStack recipeItemStack = jeiRuntime.getRecipesGui().getIngredientUnderMouse(VanillaTypes.ITEM_STACK);
		if (recipeItemStack != null && !recipeItemStack.isEmpty()) {
			return Optional.of(recipeItemStack);
		}
		*///?} else {
		itemStack = jeiRuntime.getRecipesGui().getIngredientUnderMouse(VanillaTypes.ITEM_STACK)
				.filter(stack -> !stack.isEmpty());
		if (itemStack.isPresent()) {
			return itemStack;
		}
		//?}

		//? if >1.18.2 {
		if (includeScreenHelper) {
			//? if >=26.2 {
			/*Screen screen = Minecraft.getInstance().gui.screen();
			*///?} else {
			Screen screen = Minecraft.getInstance().screen;
			//?}
			if (screen != null) {
				return jeiRuntime.getScreenHelper()
						.getClickableIngredientUnderMouse(screen, mouseX, mouseY)
						.map(JeiRuntimeSoundHandler::toItemStack)
						.flatMap(Optional::stream)
						.findFirst();
			}
		}
		//?}

		return Optional.empty();
	}

	private static Optional<ItemStack> toItemStack(ITypedIngredient<?> typedIngredient) {
		Optional<ItemStack> itemStack = typedIngredient.getItemStack()
				.filter(stack -> !stack.isEmpty());
		if (itemStack.isPresent()) {
			return itemStack;
		}

		return toItemStack(typedIngredient.getIngredient());
	}

	//? if >1.18.2 {
	private static Optional<ItemStack> toItemStack(IClickableIngredient<?> clickableIngredient) {
		//? if 1.19.2 {
		/*return toItemStack(clickableIngredient.getIngredient());
		*///?} else {
		return toItemStack(clickableIngredient.getTypedIngredient());
		//?}
	}
	//?}

	private static Optional<ItemStack> toItemStack(Object ingredient) {
		if (ingredient instanceof ItemStack stack && !stack.isEmpty()) {
			return Optional.of(stack);
		}
		//? if neoforge || forge {
		if (ingredient instanceof FluidStack fluidStack) {
			ItemStack bucket = fluidStack.getFluid().getBucket().getDefaultInstance();
			if (!bucket.isEmpty()) {
				return Optional.of(bucket);
			}
		}
		//?}

		return Optional.empty();
	}
}
