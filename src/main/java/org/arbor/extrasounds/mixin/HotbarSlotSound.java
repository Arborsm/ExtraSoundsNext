package org.arbor.extrasounds.mixin;

import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Minecraft.class)
public class HotbarSlotSound
{
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "handleKeybinds", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/entity/player/Inventory;selected*:I"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void hotbarKeyboardSound(CallbackInfo info, int i)
    {
        if (this.player != null && this.player.getInventory().selected != i)
            ExtraSounds.hotbar(i);
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;onHotbarSelected(I)V"))
    private void spectatorHotbarSound(CallbackInfo ci)
    {
        SoundManager.playSound(Sounds.HOTBAR_SCROLL, SoundType.HOTBAR);
    }
}
