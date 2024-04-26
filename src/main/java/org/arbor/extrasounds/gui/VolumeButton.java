package org.arbor.extrasounds.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VolumeButton extends Button {
    protected final Options options;
    private final SoundSource source;

    public VolumeButton(Minecraft minecraft, int x, int y, SoundSource source, int width) {
        super(x, y, width, 20, CommonComponents.EMPTY, null);
        this.options = minecraft.options;
        this.source = source;
        this.updateMessage();
    }

    protected void updateMessage() {
        Component component = this.options.getSoundSourceVolume(this.source) == 1 ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF;
        this.setMessage(Component.translatable("soundCategory." + this.source.getName()).append(": ").append(component));
    }

    @Override
    public void onPress() {
        this.options.setSoundCategoryVolume(this.source, this.options.getSoundSourceVolume(this.source) == 1 ? 0 : 1);
        this.options.save();
        this.updateMessage();
    }
}
