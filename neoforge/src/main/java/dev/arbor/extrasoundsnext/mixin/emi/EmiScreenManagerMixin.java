package dev.arbor.extrasoundsnext.mixin.emi;

import dev.emi.emi.screen.EmiScreenManager;
import dev.arbor.extrasoundsnext.sounds.ScrollSound;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiScreenManager.SidebarPanel.class)
@OnlyIn(Dist.CLIENT)
public class EmiScreenManagerMixin {
    @Unique
    private static final ScrollSound extra_sounds$scrollSound = new ScrollSound();
    @Shadow(remap = false)
    public EmiScreenManager.ScreenSpace space;

    @Inject(method = "scroll", at = @At("HEAD"), remap = false)
    private void extrasounds$scroll(int delta, CallbackInfo ci) {
        int totalPages = (space.getStacks().size() - 1) / space.pageSize + 1;
        if (totalPages > 1) {
            extra_sounds$scrollSound.play();
        }
    }
}
