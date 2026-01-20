package dev.arbor.extrasoundsnext.mixin.accessors;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
//? if <1.19.4 {
import net.minecraft.server.packs.resources.ResourceManager;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SoundManager.Preparations.class)
@MixinEnvironment()
public interface PreparationsInvoker {
	//? if >=1.19.4 {
	/*@Invoker("handleRegistration")
	void extrasounds$handleRegistration(ResourceLocation rl, SoundEventRegistration soundEventRegistration);
	*///?} else {
	@Invoker("handleRegistration")
	void extrasounds$handleRegistration(ResourceLocation rl, SoundEventRegistration soundEventRegistration, ResourceManager resourceManager);
	//?}
}

