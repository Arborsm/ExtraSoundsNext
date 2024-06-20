package org.arbor.extrasounds.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.arbor.extrasounds.ExtraSounds;
import org.jetbrains.annotations.NotNull;

public class ImageButton extends Button {
    private static final ResourceLocation ALTS = ResourceLocation.fromNamespaceAndPath(ExtraSounds.MODID, "textures/gui/settings.png");

    public ImageButton(int pX, int pY, int pWidth, int pHeight, Button.OnPress pOnPress) {
        this(pX, pY, pWidth, pHeight, pOnPress, CommonComponents.EMPTY);
    }

    public ImageButton(int pX, int pY, int pWidth, int pHeight, Button.OnPress pOnPress, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, DEFAULT_NARRATION);
    }

    public ImageButton(int pWidth, int pHeight, Button.OnPress pOnPress, Component pMessage) {
        this(0, 0, pWidth, pHeight, pOnPress, pMessage);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (!this.isActive()) {
            guiGraphics.blit(ALTS, getX(), getY(), 20, 0, 20, 20);
            return;
        }
        int textureX = this.isHovered() ? 40 : 0;
        guiGraphics.blit(ALTS, this.getX(), this.getY(), textureX, 0, 20, 20);
    }

}
