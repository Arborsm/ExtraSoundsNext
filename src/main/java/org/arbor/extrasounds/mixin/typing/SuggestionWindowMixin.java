package org.arbor.extrasounds.mixin.typing;

import org.arbor.extrasounds.SoundManager;
import net.minecraft.client.gui.components.CommandSuggestions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandSuggestions.SuggestionsList.class)
public abstract class SuggestionWindowMixin {
    @Shadow
    private int current;
    @Shadow
    private boolean tabCycles;

    @Unique
    private int extra_sounds$currentPos;

    @Inject(method = "select", at = @At("RETURN"))
    private void extrasounds$suggestionSelect(int index, CallbackInfo ci) {
        if (this.current != this.extra_sounds$currentPos) {
            SoundManager.keyboard(SoundManager.KeyType.CURSOR);
            this.extra_sounds$currentPos = this.current;
        }
    }

    @Inject(method = "useSuggestion", at = @At("HEAD"))
    private void extrasounds$suggestionComplete(CallbackInfo ci) {
        if (this.tabCycles) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.INSERT);
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CommandSuggestions$SuggestionsList;hide()V"))
    private void extrasounds$closeWindow(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        SoundManager.keyboard(SoundManager.KeyType.CURSOR);
    }
}
