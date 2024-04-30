package org.arbor.extrasounds.mixin.hotbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.arbor.extrasounds.misc.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * For Hotbar action includes keyboard, item pick.
 */
@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "handleKeybinds", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/entity/player/Inventory;selected:I"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void extrasounds$hotbarKeySound(CallbackInfo ci, int i) {
        if (this.player != null && this.player.getInventory().selected != i) {
            SoundManager.hotbar(i);
        }
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;onHotbarSelected(I)V"))
    private void extrasounds$spectatorHotbarSound(CallbackInfo ci) {
        SoundManager.playSound(Sounds.HOTBAR_SCROLL, SoundType.HOTBAR);
    }
}
