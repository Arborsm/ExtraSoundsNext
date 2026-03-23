package dev.arbor.extrasoundsnext.reg;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Textures {
	public static final ResourceLocation SETTINGS_ICON = id("gui/settings");

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

	// Component helper methods for version compatibility
	//? if <1.19 {
	public static Component translatable(String key) {
		return new net.minecraft.network.chat.TranslatableComponent(key);
	}

	public static Component literal(String text) {
		return new net.minecraft.network.chat.TextComponent(text);
	}

	public static Component empty() {
		return net.minecraft.network.chat.TextComponent.EMPTY;
	}
	//?}
 }

