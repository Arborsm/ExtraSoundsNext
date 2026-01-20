package dev.arbor.extrasoundsnext.gui;

import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.VolumeConfig;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class VolumeSlider extends AbstractSliderButton {
    private final Mixers category;

    public VolumeSlider(int x, int y, int width, int height, Mixers category) {
        //? if >=1.19 {
        /*super(x, y, width, height, Component.empty(), VolumeConfig.getVolume(category));
        *///?} else {
        super(x, y, width, height, net.minecraft.network.chat.TextComponent.EMPTY, VolumeConfig.getVolume(category));
        //?}
        this.category = category;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        int percent = (int) (this.value * 100);
        //? if >=1.19 {
        /*this.setMessage(Component.translatable(category.getTranslationKey())
                .append(": " + percent + "%"));
        *///?} else {
        this.setMessage(new net.minecraft.network.chat.TranslatableComponent(category.getTranslationKey())
                .append(": " + percent + "%"));
        //?}
    }

    @Override
    protected void applyValue() {
        VolumeConfig.setVolume(category, (float) this.value);
    }
}
