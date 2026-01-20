package dev.arbor.extrasoundsnext.mixin.gui;

import dev.arbor.extrasoundsnext.gui.VolumeScreen;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
//? if >=1.19.4 {
/*import net.minecraft.client.gui.components.ImageButton;
*///?}
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >=1.21 {
/*import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.function.Supplier;
*///?} else {
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reg.Textures;
//?}

//? if >=1.21 {
/*@Mixin(OptionsScreen.class)
@MixinEnvironment()
public abstract class SoundSettingsMixin extends Screen {
    @Shadow
    private @Final Options options;

    protected SoundSettingsMixin(Component title) {
        super(title);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/options/OptionsScreen;openScreenButton(Lnet/minecraft/network/chat/Component;Ljava/util/function/Supplier;)Lnet/minecraft/client/gui/components/Button;", ordinal = 1))
    private Button redirectToCustomScreen(OptionsScreen instance, Component component, Supplier<Screen> unused) {
        return Button.builder(component, button -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(new VolumeScreen(this));
            }
        }).width(150).build();
    }
}
*///?} else {
@Mixin(SoundOptionsScreen.class)
@MixinEnvironment()
public class SoundSettingsMixin extends OptionsSubScreen {
    public SoundSettingsMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }

	@Inject(method = "init()V", at = @At("TAIL"))
	private void extrasounds$addVolumeButton(CallbackInfo ci) {
		//? if >=1.21 {
        /*WidgetSprites sprites = new WidgetSprites(
                ResourceLocation.fromNamespaceAndPath("extrasounds", "settings/button"),
                ResourceLocation.fromNamespaceAndPath("extrasounds", "settings/disabled"),
                ResourceLocation.fromNamespaceAndPath("extrasounds", "settings/hover")
        );
        this.addRenderableWidget(
                new ImageButton(this.width / 2 + 105, this.height - 27, 20, 20, sprites, button -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new VolumeScreen(this));
                    }
                }, Component.translatable("extrasounds.volume.title"))
        );
        *///?} elif >=1.20.1 {
		/*this.addRenderableWidget(new ImageButton(this.width / 2 + 105, this.height - 27, 20, 20, 0, 0, 20, Textures.SETTINGS_ICON, 20, 40, button -> {
			if (this.minecraft != null) {
				this.minecraft.setScreen(new VolumeScreen(this));
			}
		}, Component.translatable("extrasounds.volume.title")));
		*///?} elif >=1.19.4 {
		/*this.addRenderableWidget(new ImageButton(this.width / 2 + 105, this.height - 27, 20, 20, 0, 0, 20, Textures.SETTINGS_ICON, 20, 40, button -> {
			if (this.minecraft != null) {
				this.minecraft.setScreen(new VolumeScreen(this));
			}
		}, Component.translatable("extrasounds.volume.title")));
		*///?} elif >=1.20 {
        /*this.addRenderableWidget(
                Button.builder(Component.literal("⚙"), button -> {
                            if (this.minecraft != null) {
                                this.minecraft.setScreen(new VolumeScreen(this));
                            }
                        })
                        .bounds(this.width / 2 + 105, this.height - 27, 20, 20)
                        .tooltip(net.minecraft.client.gui.components.Tooltip.create(Component.translatable("extrasounds.volume.title")))
                        .build()
        );
        *///?} elif >=1.19 {
        /*this.addRenderableWidget(
                new Button(this.width / 2 + 105, this.height - 27, 20, 20,
                        Component.literal("⚙"), button -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new VolumeScreen(this));
                    }
                })
        );
        *///?} else {
        this.addRenderableWidget(
                new Button(this.width / 2 + 105, this.height - 27, 20, 20,
                        new net.minecraft.network.chat.TextComponent("⚙"), button -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new VolumeScreen(this));
                    }
                })
        );
        //?}
	}
}
//?}
