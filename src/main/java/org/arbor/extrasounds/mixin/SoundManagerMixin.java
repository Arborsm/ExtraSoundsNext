package org.arbor.extrasounds.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.mapping.SoundPackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    @Unique
    private static void extra_sounds$extracted(ProfilerFiller profilerFiller, SoundManager.Preparations preparations, ResourceManager resourceManager) {
        profilerFiller.push(ExtraSounds.MODID);
        Reader reader = new StringReader(SoundPackLoader.GENERATED_SOUNDS.toString());
        try {
            profilerFiller.push("parse");
            Map<String, SoundEventRegistration> ExtraSoundsMap = GsonHelper.fromJson(SoundManager.GSON, reader, SoundManager.SOUND_EVENT_REGISTRATION_TYPE);
            profilerFiller.popPush("register");
            for(Map.Entry<String, SoundEventRegistration> entry : ExtraSoundsMap.entrySet()) {
                preparations.handleRegistration(new ResourceLocation(ExtraSounds.MODID, entry.getKey()), entry.getValue(), resourceManager);
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

    @Inject(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/sounds/SoundManager$Preparations;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;startTick()V", shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;startTick()V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;getResourceStack(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;")
            )
    )
    private void injected(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<SoundManager.Preparations> cir, @Local SoundManager.Preparations preparations) {
        if (SoundPackLoader.GENERATED_SOUNDS != null) {
            extra_sounds$extracted(profilerFiller, preparations, resourceManager);
        }
    }
}
