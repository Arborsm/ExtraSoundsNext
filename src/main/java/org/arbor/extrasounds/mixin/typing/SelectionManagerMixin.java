package org.arbor.extrasounds.mixin.typing;

import org.arbor.extrasounds.misc.SoundManager;
import net.minecraft.client.gui.font.TextFieldHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(TextFieldHelper.class)
public abstract class SelectionManagerMixin {
    /**
     * Requires to store the current position to prevent excessive sounds in method <code>extrasounds$moveCursor</code>.<br>
     * Injected into <code>updateSelectionRange(Z)V</code>.
     *
     * @see TextFieldHelper
     */
    @Unique
    private int extra_sounds$cursorStart = 0;
    @Unique
    private int extra_sounds$cursorEnd = 0;
    @Unique
    private boolean extra_sounds$bPasteAction = false;

    @Unique
    private static final String METHOD_SIGN_DELETE = "removeFromCursor(ILnet/minecraft/client/gui/font/TextFieldHelper$CursorStep;)V";

    @Shadow
    private int cursorPos;
    @Shadow
    private int selectionPos;
    @Shadow
    private @Final Supplier<String> getMessageFn;

    /**
     * Check the current position was updated.
     *
     * @return <code>true</code> if the position has changed.
     */
    @Unique
    private boolean extrasounds$isPosUpdated() {
        return this.extra_sounds$cursorStart == this.cursorPos && this.extra_sounds$cursorEnd == this.selectionPos;
    }

    @Inject(method = METHOD_SIGN_DELETE, at = @At("HEAD"))
    private void extrasounds$beforeDelete(int offset, TextFieldHelper.CursorStep selectionType, CallbackInfo ci) {
        final String text = this.getMessageFn.get();
        final boolean bHeadBackspace = offset < 0 && this.cursorPos <= 0;
        final boolean bTailDelete = offset > 0 && this.selectionPos >= text.length();
        if ((bHeadBackspace || bTailDelete) && this.cursorPos == this.selectionPos) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.ERASE);
    }
    @Inject(method = METHOD_SIGN_DELETE, at = @At("RETURN"))
    private void extrasounds$afterDelete(int offset, TextFieldHelper.CursorStep selectionType, CallbackInfo ci) {
        this.extra_sounds$cursorStart = this.extra_sounds$cursorEnd = this.selectionPos;
    }

    @Inject(method = "cut", at = @At("HEAD"))
    private void extrasounds$cutAction(CallbackInfo ci) {
        if (this.cursorPos == this.selectionPos) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.CUT);
    }

    @Inject(method = "cut", at = @At("RETURN"))
    private void extrasounds$afterCut(CallbackInfo ci) {
        this.extra_sounds$cursorStart = this.extra_sounds$cursorEnd = this.selectionPos;
    }

    @Inject(method = "insertText(Ljava/lang/String;Ljava/lang/String;)V", at = @At("RETURN"))
    private void extrasounds$appendChar(String string, String insertion, CallbackInfo ci) {
        if (this.extrasounds$isPosUpdated()) {
            return;
        }
        if (this.extra_sounds$bPasteAction) {
            SoundManager.keyboard(SoundManager.KeyType.PASTE);
            this.extra_sounds$bPasteAction = false;
        } else if (insertion.equals("\n")) {
            SoundManager.keyboard(SoundManager.KeyType.RETURN);
        } else {
            SoundManager.keyboard(SoundManager.KeyType.INSERT);
        }
        this.extra_sounds$cursorStart = this.extra_sounds$cursorEnd = this.selectionPos;
    }

    @Inject(method = "paste", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/TextFieldHelper;insertText(Ljava/lang/String;Ljava/lang/String;)V"))
    private void extrasounds$pasteAction(CallbackInfo ci) {
        this.extra_sounds$bPasteAction = true;
    }

    @Inject(method = "resetSelectionIfNeeded(Z)V", at = @At("RETURN"))
    private void extrasounds$moveCursor(boolean shiftDown, CallbackInfo ci) {
        if (this.extrasounds$isPosUpdated()) {
            return;
        }
        SoundManager.keyboard(SoundManager.KeyType.CURSOR);
        this.extra_sounds$cursorStart = this.cursorPos;
        this.extra_sounds$cursorEnd = this.selectionPos;
    }
}
