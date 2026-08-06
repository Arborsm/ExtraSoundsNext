package dev.arbor.extrasoundsnext.mixin.core;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import dev.arbor.extrasoundsnext.sounds.Sounds;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Minecraft.class)
@MixinEnvironment()
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public Screen screen;

    @Inject(method = "handleKeybinds", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/world/entity/player/Inventory;selected:I"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void extrasounds$hotbarKeySound(CallbackInfo ci, int i) {
        //? if >=26.1 {
        /*if (this.player != null && this.player.getInventory().getSelectedSlot() != i) {
        *///?} else {
        if (this.player != null && this.player.getInventory().selected != i) {
        //?}
            SoundManager.hotbar(i);
        }
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;onHotbarSelected(I)V"))
    private void extrasounds$spectatorHotbarSound(CallbackInfo ci) {
        SoundManager.playSound(Sounds.HOTBAR_SCROLL, SoundType.HOTBAR);
    }

    @Inject(at = @At("HEAD"), method = "setScreen")
    private void extrasounds$screenChange(@Nullable Screen screen1, CallbackInfo ci) {
        if (screen != screen1 && screen1 instanceof AbstractContainerScreen && !(screen1 instanceof CreativeModeInventoryScreen)) {
            SoundManager.playSound(Sounds.INVENTORY_OPEN, 1f);
        } else if (screen1 == null && screen instanceof AbstractContainerScreen) {
            SoundManager.playSound(Sounds.INVENTORY_CLOSE, 1f);
        }
    }
}
