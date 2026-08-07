package dev.arbor.extrasoundsnext.mixin.gui;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.Sounds;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.2 {
/*// 26.2 moved the current screen from Minecraft to Gui (gui.screen()/gui.setScreen()).
@Mixin(Gui.class)
@MixinEnvironment()
public abstract class GuiScreenMixin {
    @Shadow
    private Screen screen;

    @Inject(at = @At("HEAD"), method = "setScreen")
    private void extrasounds$screenChange(@Nullable Screen screen1, CallbackInfo ci) {
        if (screen != screen1 && screen1 instanceof AbstractContainerScreen && !(screen1 instanceof CreativeModeInventoryScreen)) {
            SoundManager.playSound(Sounds.INVENTORY_OPEN, 1f);
        } else if (screen1 == null && screen instanceof AbstractContainerScreen) {
            SoundManager.playSound(Sounds.INVENTORY_CLOSE, 1f);
        }
    }
}
*///?}
