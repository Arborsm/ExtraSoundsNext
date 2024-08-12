package dev.arbor.extrasoundsnext.mixin.rei;

import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(EntryWidget.class)
public abstract class EntryWidgetMixin {
    @Shadow(remap = false)
    public abstract EntryStack<?> getCurrentEntry();

    @Inject(method = "doAction", at = @At("HEAD"), remap = false)
    private void mouseScrolled(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (getCurrentEntry() != null && getCurrentEntry().getValue() instanceof ItemStack itemStack) {
            SoundManager.playSound(itemStack, SoundType.PICKUP);
        }
    }
}
