package dev.arbor.extrasoundsnext.gui.components;

import dev.arbor.extrasoundsnext.sounds.SoundEntry;
import dev.arbor.extrasoundsnext.sounds.SoundManager;
import dev.arbor.extrasoundsnext.sounds.VolumeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
//? if >=26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
*///?} else {
//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}
//?}

/**
 * Individual sound entry row with toggle and preview buttons.
 */
public class SoundEntryRow {
    private final SoundEntry sound;
    private final int x;
    private final int y;
    private final int width;
    private ToggleButton toggleButton;
    private ToggleButton previewButton;
    private boolean isPlaying = false;
    private long lastPlayTime = 0;
    private static final long PLAY_INTERVAL = 1000; // 1 second between plays

    // Static field to track currently playing sound
    private static SoundEntryRow currentlyPlaying = null;

    public SoundEntryRow(SoundEntry sound, int x, int y, int width) {
        this.sound = sound;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void init(Minecraft minecraft) {
        // Create toggle button (ON/OFF)
        boolean enabled = VolumeConfig.isSoundEnabled(sound);

        this.toggleButton = new ToggleButton(
            x + width - 50,
            y,
            40,
            16,
            enabled,
            (newState) -> {
                VolumeConfig.setSoundEnabled(sound, newState);
                VolumeConfig.save();
            }
        );

        // Create preview toggle button
        this.previewButton = new ToggleButton(
            x + width - 70,
            y,
            16,
            16,
            false,
            (newState) -> {
                if (newState) {
                    // Stop any currently playing sound
                    if (currentlyPlaying != null && currentlyPlaying != this) {
                        currentlyPlaying.stopPlaying();
                    }
                    // Start playing this sound
                    isPlaying = true;
                    currentlyPlaying = this;
                    SoundManager.playSound(sound.soundEvent, sound.soundType);
                    lastPlayTime = System.currentTimeMillis();
                } else {
                    // Stop playing
                    stopPlaying();
                }
            }
        );
    }

    private void togglePreview() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            // Stop any currently playing sound
            if (currentlyPlaying != null && currentlyPlaying != this) {
                currentlyPlaying.stopPlaying();
            }
            // Start playing this sound
            currentlyPlaying = this;
            SoundManager.playSound(sound.soundEvent, sound.soundType);
            lastPlayTime = System.currentTimeMillis();
        } else {
            stopPlaying();
        }
        updatePreviewButtonText();
    }

    private void stopPlaying() {
        isPlaying = false;
        if (currentlyPlaying == this) {
            currentlyPlaying = null;
        }
        updatePreviewButtonText();
    }

    private void updatePreviewButtonText() {
        if (previewButton != null) {
            Component text;
            //? if >=1.19 {
            text = Component.literal(isPlaying ? "■" : "▶");
            //?} else {
            /*text = new net.minecraft.network.chat.TextComponent(isPlaying ? "■" : "▶");
            *///?}
            previewButton.setMessage(text);
        }
    }

    //? if >=26.1 {
    /*public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        Font font = Minecraft.getInstance().font;

        // Handle continuous playing
        if (isPlaying) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayTime >= PLAY_INTERVAL) {
                SoundManager.playSound(sound.soundEvent, sound.soundType);
                lastPlayTime = currentTime;
            }
        }

        // Draw sound name
        Component name = Component.translatable(sound.getTranslationKey());
        context.text(font, name, x + 10, y + 4, 0xFFAAAAAA);

        // Render preview button
        if (previewButton != null) {
            int btnX = x + width - 70;
            int btnY = y;
            boolean hovered = mouseX >= btnX && mouseX < btnX + 16 && mouseY >= btnY && mouseY < btnY + 16;
            int bgColor = isPlaying ? 0xFF4CAF50 : (hovered ? 0xFF555555 : 0xFF3C3C3C);
            context.fill(btnX, btnY, btnX + 16, btnY + 16, bgColor);

            // Draw border
            context.fill(btnX, btnY, btnX + 16, btnY + 1, 0xFF000000);
            context.fill(btnX, btnY + 15, btnX + 16, btnY + 16, 0xFF000000);
            context.fill(btnX, btnY, btnX + 1, btnY + 16, 0xFF000000);
            context.fill(btnX + 15, btnY, btnX + 16, btnY + 16, 0xFF000000);

            // Draw play/stop icon (pixel art, centered in 16x16)
            if (isPlaying) {
                // Stop: 6x6 square centered
                context.fill(btnX + 5, btnY + 5, btnX + 11, btnY + 11, 0xFFFFFFFF);
            } else {
                // Play: filled circle, centered in 16x16
                context.fill(btnX + 6, btnY + 5, btnX + 10, btnY + 6, 0xFFFFFFFF);
                context.fill(btnX + 5, btnY + 6, btnX + 11, btnY + 10, 0xFFFFFFFF);
                context.fill(btnX + 6, btnY + 10, btnX + 10, btnY + 11, 0xFFFFFFFF);
            }
        }

        // Render toggle button
        if (toggleButton != null) {
            toggleButton.extractRenderState(context, mouseX, mouseY, delta);
        }
    }
    *///?} else {
    //? if >=1.20 {
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Font font = Minecraft.getInstance().font;

        // Handle continuous playing
        if (isPlaying) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayTime >= PLAY_INTERVAL) {
                SoundManager.playSound(sound.soundEvent, sound.soundType);
                lastPlayTime = currentTime;
            }
        }

        // Draw sound name
        Component name = Component.translatable(sound.getTranslationKey());
        context.drawString(font, name, x + 10, y + 4, 0xFFAAAAAA);

        // Render preview button
        if (previewButton != null) {
            int btnX = x + width - 70;
            int btnY = y;
            boolean hovered = mouseX >= btnX && mouseX < btnX + 16 && mouseY >= btnY && mouseY < btnY + 16;
            int bgColor = isPlaying ? 0xFF4CAF50 : (hovered ? 0xFF555555 : 0xFF3C3C3C);
            context.fill(btnX, btnY, btnX + 16, btnY + 16, bgColor);

            // Draw border
            context.fill(btnX, btnY, btnX + 16, btnY + 1, 0xFF000000);
            context.fill(btnX, btnY + 15, btnX + 16, btnY + 16, 0xFF000000);
            context.fill(btnX, btnY, btnX + 1, btnY + 16, 0xFF000000);
            context.fill(btnX + 15, btnY, btnX + 16, btnY + 16, 0xFF000000);

            // Draw play/stop icon (pixel art, centered in 16x16)
            if (isPlaying) {
                // Stop: 6x6 square centered
                context.fill(btnX + 5, btnY + 5, btnX + 11, btnY + 11, 0xFFFFFFFF);
            } else {
                // Play: filled circle, centered in 16x16
                context.fill(btnX + 6, btnY + 5, btnX + 10, btnY + 6, 0xFFFFFFFF);
                context.fill(btnX + 5, btnY + 6, btnX + 11, btnY + 10, 0xFFFFFFFF);
                context.fill(btnX + 6, btnY + 10, btnX + 10, btnY + 11, 0xFFFFFFFF);
            }
        }

        // Render toggle button
        if (toggleButton != null) {
            toggleButton.render(context, mouseX, mouseY, delta);
        }
    }
    //?} else {
    /*public void render(PoseStack context, int mouseX, int mouseY, float delta) {
        Font font = Minecraft.getInstance().font;

        // Handle continuous playing
        if (isPlaying) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPlayTime >= PLAY_INTERVAL) {
                SoundManager.playSound(sound.soundEvent, sound.soundType);
                lastPlayTime = currentTime;
            }
        }

        // Draw sound name
        Component name;
        //? if >=1.19 {
        name = Component.translatable(sound.getTranslationKey());
        //?} else {
        /^name = new net.minecraft.network.chat.TranslatableComponent(sound.getTranslationKey());
        ^///?}
        font.draw(context, name, x + 10, y + 4, 0xFFAAAAAA);

        // Render preview button
        if (previewButton != null) {
            int btnX = x + width - 70;
            int btnY = y;
            boolean hovered = mouseX >= btnX && mouseX < btnX + 16 && mouseY >= btnY && mouseY < btnY + 16;
            int bgColor = isPlaying ? 0xFF4CAF50 : (hovered ? 0xFF555555 : 0xFF3C3C3C);
            fill(context, btnX, btnY, btnX + 16, btnY + 16, bgColor);

            // Draw border
            fill(context, btnX, btnY, btnX + 16, btnY + 1, 0xFF000000);
            fill(context, btnX, btnY + 15, btnX + 16, btnY + 16, 0xFF000000);
            fill(context, btnX, btnY, btnX + 1, btnY + 16, 0xFF000000);
            fill(context, btnX + 15, btnY, btnX + 16, btnY + 16, 0xFF000000);

            // Draw play/stop icon (pixel art, centered in 16x16)
            if (isPlaying) {
                // Stop: 6x6 square centered
                fill(context, btnX + 5, btnY + 5, btnX + 11, btnY + 11, 0xFFFFFFFF);
            } else {
                // Play: filled circle, centered in 16x16
                fill(context, btnX + 6, btnY + 5, btnX + 10, btnY + 6, 0xFFFFFFFF);
                fill(context, btnX + 5, btnY + 6, btnX + 11, btnY + 10, 0xFFFFFFFF);
                fill(context, btnX + 6, btnY + 10, btnX + 10, btnY + 11, 0xFFFFFFFF);
            }
        }

        // Render toggle button
        if (toggleButton != null) {
            toggleButton.render(context, mouseX, mouseY, delta);
        }
    }

    private void fill(PoseStack context, int x1, int y1, int x2, int y2, int color) {
        net.minecraft.client.gui.screens.Screen.fill(context, x1, y1, x2, y2, color);
    }
    *///?}
    //?}

    //? if >=26.1 {
    /*public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return mouseClicked(new net.minecraft.client.input.MouseButtonEvent(mouseX, mouseY, new net.minecraft.client.input.MouseButtonInfo(button, 0)), true);
    }

    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean bl) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();
    *///?} else {
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
    //?}
        // Check preview button first (manual bounds check)
        int previewX = x + width - 70;
        int previewY = y;
        if (mouseX >= previewX && mouseX < previewX + 18 && mouseY >= previewY && mouseY < previewY + 18) {
            togglePreview();
            return true;
        }

        // Check toggle button
        //? if >=26.1 {
		/*return toggleButton != null && toggleButton.mouseClicked(event, bl);
	}

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return mouseDragged(new net.minecraft.client.input.MouseButtonEvent(mouseX, mouseY, new net.minecraft.client.input.MouseButtonInfo(button, 0)), dragX, dragY);
    }

    public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double dragX, double dragY) {
		return toggleButton != null && toggleButton.mouseDragged(event, dragX, dragY);
	}

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return mouseReleased(new net.minecraft.client.input.MouseButtonEvent(mouseX, mouseY, new net.minecraft.client.input.MouseButtonInfo(button, 0)));
    }

    public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent event) {
		return toggleButton != null && toggleButton.mouseReleased(event);
	}
    *///?} else {
    		return toggleButton != null && toggleButton.mouseClicked(mouseX, mouseY, button);
	}

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		return toggleButton != null && toggleButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return toggleButton != null && toggleButton.mouseReleased(mouseX, mouseY, button);
	}
    //?}
}
