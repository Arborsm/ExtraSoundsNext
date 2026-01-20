package dev.arbor.extrasoundsnext.mixin.accessors;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
@MixinEnvironment()
public abstract class AbstractContainerScreenAccessor {
	@Accessor("draggingItem")
	public abstract ItemStack getDraggingItem();
}
