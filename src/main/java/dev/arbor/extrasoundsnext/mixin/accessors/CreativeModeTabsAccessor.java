package dev.arbor.extrasoundsnext.mixin.accessors;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
//? if >=1.20 {
/*import net.minecraft.resources.ResourceKey;
*///?}
//? if >=1.19.3 {
/*import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeTabs.class)
@MixinEnvironment()
public class CreativeModeTabsAccessor {
	@Accessor("INVENTORY")
	//? if >=1.20 {
	/^public static ResourceKey<CreativeModeTab> getInventoryKey() {
		return CreativeModeTabs.INVENTORY;
	}
	^///?} else {
	public static CreativeModeTab getInventoryKey() {
		return CreativeModeTabs.INVENTORY;
	}
	//?}
}
*///?} else {
//?}

