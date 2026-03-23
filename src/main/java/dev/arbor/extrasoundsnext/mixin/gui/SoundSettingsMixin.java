package dev.arbor.extrasoundsnext.mixin.gui;

import dev.arbor.extrasoundsnext.gui.VolumeScreen;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.Options;
//? if >=1.21 {
/*import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.components.Tooltip;
*///?} else {
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
//?}
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
@MixinEnvironment()
public abstract class SoundSettingsMixin extends OptionsSubScreen {
	public SoundSettingsMixin(Screen lastScreen, Options options, Component title) {
		super(lastScreen, options, title);
	}
	@Unique
	private Button extrasounds$volumeButton;

	//? if >=1.21.1 {
	/*@Override
	protected void repositionElements() {
		super.repositionElements();
		if (extrasounds$volumeButton != null) {
			extrasounds$volumeButton.setX(this.width / 2 - 178);
			extrasounds$volumeButton.setY(this.height - 27);
		}
	}
	*///?}

	//? if >=1.21 {
	/*@Inject(method = "addOptions", at = @At("TAIL"))
	*///?} else {
	@Inject(method = "init()V", at = @At("TAIL"))
	 //?}
	private void extrasounds$addVolumeButton(CallbackInfo ci) {
		//? if >=1.19.4 {
		/*extrasounds$volumeButton = Button.builder(Component.literal("ExtraSounds"), this::extrasounds$showVolumeScreen)
				.bounds(this.width / 2 - 178, this.height - 27, 75, 20)
				.tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("extrasounds.volume.title")))
				.build();
		this.addRenderableWidget(extrasounds$volumeButton);
		*///?} elif >=1.19 {
        /*this.addRenderableWidget(
                new Button(this.width / 2 - 178, this.height - 27, 75, 20,
                        Component.literal("ExtraSounds"), this::extrasounds$showVolumeScreen)
        );
        *///?} else {
        this.addRenderableWidget(
                new Button(this.width / 2 - 178, this.height - 27, 75, 20,
                        new net.minecraft.network.chat.TextComponent("ExtraSounds"), this::extrasounds$showVolumeScreen)
        );
        //?}
	}

	@Unique
	private void extrasounds$showVolumeScreen(Button button) {
		if (this.minecraft != null) {
			this.minecraft.setScreen(new VolumeScreen(this));
		}
	}
}
