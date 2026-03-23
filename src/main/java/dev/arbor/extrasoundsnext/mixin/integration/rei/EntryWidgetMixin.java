package dev.arbor.extrasoundsnext.mixin.integration.rei;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.world.item.ItemStack;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@MixinEnvironment
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
