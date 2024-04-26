package org.arbor.extrasounds.mixin.typing;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import org.arbor.extrasounds.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EditBox.class)
public abstract class TextFieldWidgetMixin {
    /**
     * Requires to store the current position to prevent excessive sounds in method <code>extrasounds$setCursor</code>.<br>
     * Injected into <code>keyPressed(III)Z</code>.
     */
    @Unique
    private int extra_sounds$cursorStart = 0;
    @Unique
    private int extra_sounds$cursorEnd = 0;

    @Shadow
    private int cursorPos;
    @Shadow
    private int highlightPos;
    @Shadow
    private int maxLength;

    @Shadow
    public abstract String getHighlighted();

    @Shadow
    public abstract String getValue();

    @Unique
    private void extrasounds$cursorChanged() {
        final boolean bSamePos = this.extra_sounds$cursorStart == this.cursorPos && this.extra_sounds$cursorEnd == this.highlightPos;
        if (bSamePos) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.CURSOR);
        this.extra_sounds$cursorStart = this.cursorPos;
        this.extra_sounds$cursorEnd = this.highlightPos;
    }

    @Inject(method = "deleteText", at = @At("HEAD"))
    private void extrasounds$eraseStrHead(int offset, CallbackInfo ci) {
        final boolean bHeadBackspace = offset < 0 && this.cursorPos <= 0;
        final boolean bTailDelete = offset > 0 && this.highlightPos >= this.getValue().length();
        if ((bHeadBackspace || bTailDelete) && this.cursorPos == this.highlightPos) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.ERASE);
    }
    @Inject(method = "deleteText", at = @At("RETURN"))
    private void extrasounds$eraseStrReturn(int offset, CallbackInfo ci) {
        this.extra_sounds$cursorStart = this.extra_sounds$cursorEnd = this.highlightPos;
    }

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/EditBox;getHighlighted()Ljava/lang/String;",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$cutAction(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!Screen.isCut(keyCode) || this.getHighlighted().isEmpty()) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.CUT);
        this.extra_sounds$cursorStart = this.extra_sounds$cursorEnd = this.highlightPos;
    }

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void extrasounds$appendChar(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || this.getValue().length() >= this.maxLength) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.INSERT);
        this.extra_sounds$cursorStart = this.extra_sounds$cursorEnd = this.highlightPos;
    }

    @Inject(
            method = "keyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyboardHandler;getClipboard()Ljava/lang/String;",
                    shift = At.Shift.AFTER
            )
    )
    private void extrasounds$pasteAction(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!Screen.isPaste(keyCode) || this.getValue().length() >= this.maxLength) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.PASTE);
        this.extra_sounds$cursorStart = this.extra_sounds$cursorEnd = this.highlightPos;
    }

    @Inject(method = "keyPressed",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;getWordPosition(I)I", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;moveCursor(I)V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;moveCursorToStart()V", shift = At.Shift.AFTER),
                    @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;moveCursorToEnd()V", shift = At.Shift.AFTER)
            }
    )
    private void extrasounds$cursorMoveKeyTyped(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        this.extrasounds$cursorChanged();
    }
}
