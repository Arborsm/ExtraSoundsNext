package dev.arbor.extrasoundsnext.gui.components;

//? if >=26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///?} else {
//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}
//?}
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Toggle button with smooth animation.
 * Renders as a rounded rectangle with a sliding circle indicator.
 */
public class ToggleButton extends AbstractWidget {
    private boolean toggled;
    private final Consumer<Boolean> onToggle;
    private float animationProgress = 0f;

    public ToggleButton(int x, int y, int width, int height, boolean initialState, Consumer<Boolean> onToggle) {
        //? if >=1.19 {
        super(x, y, width, height, Component.empty());
        //?} else {
        /*super(x, y, width, height, new net.minecraft.network.chat.TextComponent(""));
        *///?}
        this.toggled = initialState;
        this.onToggle = onToggle;
        this.animationProgress = initialState ? 1f : 0f;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    //? if >=26.1 {
    /*@Override
    public void onClick(net.minecraft.client.input.MouseButtonEvent event, boolean bl) {
        this.toggled = !this.toggled;
        if (this.onToggle != null) {
            this.onToggle.accept(this.toggled);
        }
    }
    *///?} else {
    @Override
    public void onClick(double mouseX, double mouseY) {
        this.toggled = !this.toggled;
        if (this.onToggle != null) {
            this.onToggle.accept(this.toggled);
        }
    }
    //?}

	//? if >=26.1 {
    /*@Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        // Update animation (fixed speed)
        if (this.toggled && this.animationProgress < 1f) {
            this.animationProgress = Math.min(1f, this.animationProgress + 0.15f);
        } else if (!this.toggled && this.animationProgress > 0f) {
            this.animationProgress = Math.max(0f, this.animationProgress - 0.15f);
        }

        // Background color (gray when off, green when on)
        int bgColor = interpolateColor(0xFF3C3C3C, 0xFF4CAF50, this.animationProgress);

        // Draw rounded background with border
        int x1 = this.getX();
        int y1 = this.getY();
        int x2 = x1 + this.width;
        int y2 = y1 + this.height;

        // Draw background
        context.fill(x1, y1, x2, y2, bgColor);

        // Draw border
        context.fill(x1, y1, x2, y1 + 1, 0xFF000000); // Top
        context.fill(x1, y2 - 1, x2, y2, 0xFF000000); // Bottom
        context.fill(x1, y1, x1 + 1, y2, 0xFF000000); // Left
        context.fill(x2 - 1, y1, x2, y2, 0xFF000000); // Right

        // Draw sliding circle
        int circleSize = this.height - 4;
        int circleX = x1 + 2 + (int)((this.width - circleSize - 4) * this.animationProgress);
        int circleY = y1 + 2;
        context.fill(circleX, circleY, circleX + circleSize, circleY + circleSize, 0xFFFFFFFF);
    }
    *///?} elif >=1.20 {
    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        // Update animation (fixed speed)
        if (this.toggled && this.animationProgress < 1f) {
            this.animationProgress = Math.min(1f, this.animationProgress + 0.15f);
        } else if (!this.toggled && this.animationProgress > 0f) {
            this.animationProgress = Math.max(0f, this.animationProgress - 0.15f);
        }

        // Background color (gray when off, green when on)
        int bgColor = interpolateColor(0xFF3C3C3C, 0xFF4CAF50, this.animationProgress);

        // Draw rounded background with border
        int x1 = this.getX();
        int y1 = this.getY();
        int x2 = x1 + this.width;
        int y2 = y1 + this.height;

        // Draw background
        context.fill(x1, y1, x2, y2, bgColor);

        // Draw border
        context.fill(x1, y1, x2, y1 + 1, 0xFF000000); // Top
        context.fill(x1, y2 - 1, x2, y2, 0xFF000000); // Bottom
        context.fill(x1, y1, x1 + 1, y2, 0xFF000000); // Left
        context.fill(x2 - 1, y1, x2, y2, 0xFF000000); // Right

        // Draw sliding circle
        int circleSize = this.height - 4;
        int circleX = x1 + 2 + (int)((this.width - circleSize - 4) * this.animationProgress);
        int circleY = y1 + 2;
        context.fill(circleX, circleY, circleX + circleSize, circleY + circleSize, 0xFFFFFFFF);
    }
    //?} elif =1.19.4 {
    /*@Override
    public void renderWidget(@NotNull PoseStack context, int mouseX, int mouseY, float delta) {
        // Update animation (fixed speed)
        if (this.toggled && this.animationProgress < 1f) {
            this.animationProgress = Math.min(1f, this.animationProgress + 0.15f);
        } else if (!this.toggled && this.animationProgress > 0f) {
            this.animationProgress = Math.max(0f, this.animationProgress - 0.15f);
        }

        // Background color (gray when off, green when on)
        int bgColor = interpolateColor(0xFF3C3C3C, 0xFF4CAF50, this.animationProgress);

        // Draw rounded background with border
        int x1 = getX();
        int y1 = getY();
        int x2 = x1 + this.width;
        int y2 = y1 + this.height;

        // Draw background
        fill(context, x1, y1, x2, y2, bgColor);

        // Draw border
        fill(context, x1, y1, x2, y1 + 1, 0xFF000000); // Top
        fill(context, x1, y2 - 1, x2, y2, 0xFF000000); // Bottom
        fill(context, x1, y1, x1 + 1, y2, 0xFF000000); // Left
        fill(context, x2 - 1, y1, x2, y2, 0xFF000000); // Right

        // Draw sliding circle
        int circleSize = this.height - 4;
        int circleX = x1 + 2 + (int)((this.width - circleSize - 4) * this.animationProgress);
        int circleY = y1 + 2;
        fill(context, circleX, circleY, circleX + circleSize, circleY + circleSize, 0xFFFFFFFF);
    }
    *///?} else {
    /*@Override
    public void renderButton(@NotNull PoseStack context, int mouseX, int mouseY, float delta) {
        // Update animation (fixed speed)
        if (this.toggled && this.animationProgress < 1f) {
            this.animationProgress = Math.min(1f, this.animationProgress + 0.15f);
        } else if (!this.toggled && this.animationProgress > 0f) {
            this.animationProgress = Math.max(0f, this.animationProgress - 0.15f);
        }

        // Background color (gray when off, green when on)
        int bgColor = interpolateColor(0xFF3C3C3C, 0xFF4CAF50, this.animationProgress);

        // Draw rounded background with border
        int x1 = this.x;
        int y1 = this.y;
        int x2 = x1 + this.width;
        int y2 = y1 + this.height;

        // Draw background
        fill(context, x1, y1, x2, y2, bgColor);

        // Draw border
        fill(context, x1, y1, x2, y1 + 1, 0xFF000000); // Top
        fill(context, x1, y2 - 1, x2, y2, 0xFF000000); // Bottom
        fill(context, x1, y1, x1 + 1, y2, 0xFF000000); // Left
        fill(context, x2 - 1, y1, x2, y2, 0xFF000000); // Right

        // Draw sliding circle
        int circleSize = this.height - 4;
        int circleX = x1 + 2 + (int)((this.width - circleSize - 4) * this.animationProgress);
        int circleY = y1 + 2;
        fill(context, circleX, circleY, circleX + circleSize, circleY + circleSize, 0xFFFFFFFF);
    }
    *///?}

    private int interpolateColor(int color1, int color2, float progress) {
        int a1 = (color1 >> 24) & 0xFF;
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >> 24) & 0xFF;
        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = (int)(a1 + (a2 - a1) * progress);
        int r = (int)(r1 + (r2 - r1) * progress);
        int g = (int)(g1 + (g2 - g1) * progress);
        int b = (int)(b1 + (b2 - b1) * progress);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    //? if >= 1.19.4 {
    @Override
    public void updateWidgetNarration(NarrationElementOutput builder) {
        this.defaultButtonNarrationText(builder);
    }
    //?} else {
    /*@Override
    public void updateNarration(@NotNull NarrationElementOutput builder) {
        this.defaultButtonNarrationText(builder);
    }
    *///?}

    //? if <1.20 {
    /*private void fillRect(PoseStack context, int x1, int y1, int x2, int y2, int color) {
        net.minecraft.client.gui.screens.Screen.fill(context, x1, y1, x2, y2, color);
    }
	*///?}
}
