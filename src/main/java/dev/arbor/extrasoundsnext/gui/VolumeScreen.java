package dev.arbor.extrasoundsnext.gui;

import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.VolumeConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
//? if >=1.20 {
/*import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
*///?} else {
import com.mojang.blaze3d.vertex.PoseStack;
//?}

public class VolumeScreen extends Screen {
    private final Screen parent;
    private double scrollOffset = 0;
    private int contentHeight = 0;
    private Button doneButton;

    public VolumeScreen(Screen parent) {
        //? if >=1.19 {
        /*super(Component.translatable("extrasounds.volume.title"));
        *///?} else {
        super(new net.minecraft.network.chat.TranslatableComponent("extrasounds.volume.title"));
        //?}
        this.parent = parent;
    }

    @Override
    protected void init() {
        int y = 32 + (int) scrollOffset;
        int centerX = this.width / 2;

        // Top spacing
        y += 4;

        // MASTER slider at top
        addVolumeSlider(centerX - 155, y, Mixers.MASTER, true);

        // Sub-category sliders (2 columns)
        Mixers[] sliderCats = {
                Mixers.INVENTORY, Mixers.ACTION,
                Mixers.CHAT, Mixers.CHAT_MENTION,
                Mixers.EFFECTS, Mixers.HOTBAR,
                Mixers.TYPING
        };
        for (int i = 0; i < sliderCats.length; i += 2) {
			y += 24;
            addVolumeSlider(centerX - 155, y, sliderCats[i], false);
            if (i + 1 < sliderCats.length) {
                addVolumeSlider(centerX + 5, y, sliderCats[i + 1], false);
            }
        }

        // Toggle buttons (2 columns)
        Mixers[] toggleCats = {
                Mixers.ITEM_DROP, Mixers.EMPTY_HOTBAR,
                Mixers.ENABLED_FOOTSTEP, Mixers.ENABLED_EFFECTS,
                Mixers.ENABLED_POOF
        };
        for (int i = 0; i < toggleCats.length; i += 2) {
			y += 24;
            addToggleButton(centerX - 155, y, toggleCats[i]);
            if (i + 1 < toggleCats.length) {
                addToggleButton(centerX + 5, y, toggleCats[i + 1]);
            }
        }
		y -= 6;

        // Save content height
        contentHeight = y - (int) scrollOffset;

        // Done button
        //? if >=1.19.4 {
        /*doneButton = Button.builder(CommonComponents.GUI_DONE, button -> onClose())
				.bounds(this.width / 2 - 100, this.height - 27, 200, 20)
                .build();
        this.addRenderableWidget(doneButton);
        *///?} else {
        doneButton = new Button(centerX - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE, button -> onClose());
        this.addRenderableWidget(doneButton);
        //?}
    }

    private void addVolumeSlider(int x, int y, Mixers category, boolean isMaster) {
        int w = isMaster ? 310 : 150;
        VolumeSlider slider = new VolumeSlider(x, y, w, 20, category);
        this.addRenderableWidget(slider);
    }

    private void addToggleButton(int x, int y, Mixers category) {
        boolean on = VolumeConfig.getVolume(category) >= 0.5f;
        Component text = getToggleText(category, on);
        //? if >=1.19.4 {
        /*this.addRenderableWidget(
                Button.builder(text, button -> {
                            boolean current = VolumeConfig.getVolume(category) >= 0.5f;
                            boolean next = !current;
                            VolumeConfig.setVolume(category, next ? 1.0f : 0.0f);
                            button.setMessage(getToggleText(category, next));
                        })
                        .bounds(x, y, 150, 20)
                        .build()
        );
        *///?} else {
        this.addRenderableWidget(
                new Button(x, y, 150, 20, text, button -> {
                    boolean current = VolumeConfig.getVolume(category) >= 0.5f;
                    boolean next = !current;
                    VolumeConfig.setVolume(category, next ? 1.0f : 0.0f);
                    button.setMessage(getToggleText(category, next));
                })
        );
        //?}
    }

    private Component getToggleText(Mixers category, boolean on) {
        //? if >=1.19 {
        /*return Component.translatable(category.getTranslationKey())
                .append(": ")
                .append(on ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
        *///?} else {
        return new net.minecraft.network.chat.TranslatableComponent(category.getTranslationKey())
                .append(": ")
                .append(on ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
        //?}
    }

    @Override
    public void onClose() {
        VolumeConfig.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    //? if >=1.20.3 {
    /*public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
    *///?} else {
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
    //?}
        int maxScroll = Math.max(0, contentHeight - (this.height - 64));
        scrollOffset = Math.max(-maxScroll, Math.min(0, scrollOffset + scrollY * 15));
        //? if >=1.19 {
        /*this.rebuildWidgets();
        *///?} else {
        this.init(); // rebuildWidgets doesn't exist in 1.18.2
        //?}
        return true;
    }

    @Override
    //? if >=1.20 {
    /*public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        //? if >=1.21 {
        /^super.render(context, mouseX, mouseY, delta);
        ^///?} else {
        this.renderBackground(context);
        //?}

        context.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // Draw scrollable area background
        context.fill(0, 32, this.width, this.height - 32, 0xA0000000);

        // Enable scissor for scrollable content
        context.enableScissor(0, 32, this.width, this.height - 32);

        // Render scrollable widgets (exclude Done button)
        for (var widget : this.children()) {
            //? if >=1.20 {
            /^if (widget instanceof net.minecraft.client.gui.components.Renderable renderable && widget != doneButton) {
                renderable.render(context, mouseX, mouseY, delta);
            }
            ^///?} else {
            if (widget instanceof net.minecraft.client.gui.components.AbstractWidget abstractWidget && widget != doneButton) {
                abstractWidget.render(context, mouseX, mouseY, delta);
            }
            //?}
        }

        context.disableScissor();

        // Render Done button outside scissor
        if (doneButton != null) {
            doneButton.render(context, mouseX, mouseY, delta);
        }

        // Draw scrollbar
        int maxScroll = Math.max(0, contentHeight - (this.height - 64));
        if (maxScroll > 0) {
            int scrollbarHeight = Math.max(20, (this.height - 64) * (this.height - 64) / contentHeight);
            int scrollbarY = 32 + (int)((-scrollOffset / maxScroll) * (this.height - 64 - scrollbarHeight));
            context.fill(this.width - 8, scrollbarY, this.width - 2, scrollbarY + scrollbarHeight, 0xFFC0C0C0);
        }
    }
    *///?} else {
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(poseStack, mouseX, mouseY, delta);
    }
    //?}
}
