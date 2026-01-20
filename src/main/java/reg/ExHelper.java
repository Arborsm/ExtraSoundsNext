package reg;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import net.minecraft.resources.ResourceLocation;

public class ExHelper {
	public static ResourceLocation id(String path) {
		//? if >=1.21 || (1.19.2 && forge) {
		/*return ResourceLocation.fromNamespaceAndPath(ExtraSoundsNext.MODID, path);
		*///?} else {
		return new ResourceLocation(ExtraSoundsNext.MODID, path);
		//?}
	}

	public static ResourceLocation rl(String path) {
		return id(path);
	}

	public static ResourceLocation rl(String namespace, String path) {
		//? if >=1.21 || (1.19.2 && forge) {
		/*return ResourceLocation.fromNamespaceAndPath(namespace, path);
		*///?} else {
		return new ResourceLocation(namespace, path);
		//?}
	}
}
