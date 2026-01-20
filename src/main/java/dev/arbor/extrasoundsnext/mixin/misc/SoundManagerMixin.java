package dev.arbor.extrasoundsnext.mixin.misc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.llamalad7.mixinextras.sugar.Local;
import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.mixin.accessors.PreparationsInvoker;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import dev.arbor.extrasoundsnext.mapping.SoundPackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reg.ExHelper;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

@Mixin(SoundManager.class)
@MixinEnvironment()
public class SoundManagerMixin {
	@Accessor("GSON")
	public static Gson getGSON() {
		throw new AssertionError();
	}

	@Accessor("SOUND_EVENT_REGISTRATION_TYPE")
	public static TypeToken<Map<String, SoundEventRegistration>> getSOUND_EVENT_REGISTRATION_TYPE() {
		throw new AssertionError();
	}

	//? if >=1.19.3 {
	/*@Inject(
			method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/sounds/SoundManager$Preparations;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.AFTER),
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager$Preparations;listResources(Lnet/minecraft/server/packs/resources/ResourceManager;)V"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;getResourceStack(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;")
			)
	)
	private void injected(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<SoundManager.Preparations> cir, @Local SoundManager.Preparations preparations) {
	*///?} elif =1.19.2 {
	/*@Inject(
			method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/sounds/SoundManager$Preparations;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;startTick()V", shift = At.Shift.AFTER),
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;startTick()V"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;getResourceStack(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;")
			)
	)
	private void injected(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<SoundManager.Preparations> cir, @Local SoundManager.Preparations preparations) {
	*///?} else {
	@Inject(
			method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/sounds/SoundManager$Preparations;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;startTick()V", shift = At.Shift.AFTER),
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;startTick()V")
			)
	)
	private void injected(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<SoundManager.Preparations> cir, @Local SoundManager.Preparations preparations) {
	//?}
		if (SoundPackLoader.GENERATED_SOUNDS != null) {
			profilerFiller.push(ExtraSoundsNext.MODID);
			Reader reader = new StringReader(SoundPackLoader.GENERATED_SOUNDS.toString());
			try {
				profilerFiller.push("parse");
				Map<String, SoundEventRegistration> ExtraSoundsMap = GsonHelper.fromJson(getGSON(), reader,getSOUND_EVENT_REGISTRATION_TYPE());
				profilerFiller.popPush("register");
				for(Map.Entry<String, SoundEventRegistration> entry : ExtraSoundsMap.entrySet()) {
					//? if >=1.19.4 {
					/*((PreparationsInvoker)preparations).extrasounds$handleRegistration(ExHelper.id(entry.getKey()), entry.getValue());
					 *///?} else {
					((PreparationsInvoker)preparations).extrasounds$handleRegistration(ExHelper.id(entry.getKey()), entry.getValue(), resourceManager);
					//?}
				}
				profilerFiller.pop();
			} catch (Throwable throwable) {
				try {
					reader.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
				throw throwable;
			}
			profilerFiller.pop();
			profilerFiller.pop();
		}
	}
}
