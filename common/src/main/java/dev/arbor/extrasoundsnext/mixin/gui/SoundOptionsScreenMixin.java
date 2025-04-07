package dev.arbor.extrasoundsnext.mixin.gui;

import com.google.common.collect.ImmutableMap;
import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.gui.SoundList;
import dev.arbor.extrasoundsnext.sounds.SoundSouceInit;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import dev.arbor.extrasoundsnext.gui.SoundGroupOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(SoundOptionsScreen.class)
public class SoundOptionsScreenMixin extends OptionsSubScreen {
    @Shadow
    private OptionsList list;

    public SoundOptionsScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }

    @Inject(method = "init()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall([Lnet/minecraft/client/OptionInstance;)V", shift = At.Shift.AFTER, ordinal = 1))
    private void redirectToCustomScreen(CallbackInfo ci) {
        for (String key : SoundSouceInit.MASTER_CLASSES) {
            final SoundSource category = SoundSouceInit.MASTERS.get(key);
            if (this.minecraft != null) {
                list.addEntry(new OptionsList.Entry(ImmutableMap.of(
                        OptionInstance.createBoolean("soundCategory." + category.getName(), false),
                        SoundList.createCustomizedOption(this.minecraft, category)
                                .createButton(options, this.width / 2 - 155, 0, 285),
                        OptionInstance.createBoolean("soundCategory.unused." + category.getName(), false),
                        new ImageButton(this.width / 2 + 135, 0, 20, 20, 0, 0, 20,
                                ExtraSoundsNext.SETTINGS_ICON, 20, 40, click ->
                                this.minecraft.setScreen(new SoundGroupOptionsScreen(this, options, category))
                ))));
            }
        }
    }

    @Inject(method = "getAllSoundOptionsExceptMaster", at = @At("RETURN"), cancellable = true)
    private void addCustomSoundOptions(CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        cir.setReturnValue(Arrays.stream(SoundSource.values())
                .filter((soundSource) -> soundSource != SoundSource.MASTER)
                .filter(it -> !SoundSouceInit.PARENTS.containsKey(it) && !SoundSouceInit.MASTERS.containsValue(it))
                .map(this.options::getSoundSourceOptionInstance)
                .toArray(OptionInstance[]::new));
    }
}
