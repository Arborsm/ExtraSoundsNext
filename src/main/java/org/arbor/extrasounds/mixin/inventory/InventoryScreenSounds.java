package org.arbor.extrasounds.mixin.inventory;

import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.SoundManager;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class InventoryScreenSounds
{

    @Shadow
    @Nullable
    public Screen screen;

    @Inject(at = @At("HEAD"), method = "setScreen")
    void open(@Nullable Screen screen1, CallbackInfo ci)
    {
        if (screen != screen1 && screen1 instanceof AbstractContainerScreen)
            SoundManager.playSound(Sounds.INVENTORY_OPEN, 1f, SoundSource.PLAYERS);
        else if (screen1 == null && screen instanceof AbstractContainerScreen)
            SoundManager.playSound(Sounds.INVENTORY_CLOSE, 1f, SoundSource.PLAYERS);
    }
}
