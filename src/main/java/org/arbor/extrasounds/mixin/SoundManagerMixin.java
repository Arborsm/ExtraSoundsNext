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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    @Inject(
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/sounds/SoundManager$Preparations;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager$Preparations;listResources(Lnet/minecraft/server/packs/resources/ResourceManager;)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;getResourceStack(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;")
            )
    )
    private void injected(ResourceManager p_120356_, ProfilerFiller p_120357_, CallbackInfoReturnable<SoundManager.Preparations> cir, @Local SoundManager.Preparations preparations) {
        p_120357_.push(ExtraSounds.MODID);
        Reader reader = new StringReader(SoundPackLoader.getGeneratedSounds());
        try {
            p_120357_.push("parse");
            Map<String, SoundEventRegistration> ExtraSoundsMap = GsonHelper.fromJson(SoundManager.GSON, reader, SoundManager.SOUND_EVENT_REGISTRATION_TYPE);
            p_120357_.popPush("register");
            for(Map.Entry<String, SoundEventRegistration> entry : ExtraSoundsMap.entrySet()) {
                preparations.handleRegistration(new ResourceLocation(ExtraSounds.MODID, entry.getKey()), entry.getValue());
            }
            p_120357_.pop();
        }catch (Throwable var14){
            try {
                reader.close();
            } catch (Throwable var13) {
                var14.addSuppressed(var13);
            }
            throw var14;
        }
        p_120357_.pop();
        p_120357_.pop();
    }
}
