package dev.arbor.extrasoundsnext.integration.jei;

import dev.arbor.extrasoundsnext.reg.ExHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class ExtraSoundsJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ExHelper.id("extrasounds");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		JeiRuntimeSoundHandler.setRuntime(jeiRuntime);
	}

	//? if >1.18.2 {
	@Override
	public void onRuntimeUnavailable() {
		JeiRuntimeSoundHandler.clearRuntime();
	}
	//?}
}
