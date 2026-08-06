package dev.arbor.extrasoundsnext.gui.components;

import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.SoundEntry;
import dev.arbor.extrasoundsnext.sounds.VolumeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractSliderButton;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Collapsible category panel that shows mixer volume slider and individual sound entries.
 */
public class CategoryPanel {
    private final Mixers mixer;
    private boolean expanded;
    private final List<SoundEntry> sounds;
    private final int x;
    private int y;
    private final int width;
    private VolumeSlider volumeSlider;
    private final List<SoundEntryRow> soundRows = new ArrayList<>();
    private Consumer<CategoryPanel> onExpandToggle;

    public CategoryPanel(Mixers mixer, int x, int y, int width) {
        this.mixer = mixer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.expanded = false;

        // Find all sounds belonging to this mixer's category
        this.sounds = new ArrayList<>();
        String groupName = getMixerGroupName(mixer);
        if (groupName != null) {
            for (SoundEntry entry : SoundEntry.values()) {
                if (entry.group.equals(groupName)) {
                    sounds.add(entry);
                }
            }
        }
    }

    private String getMixerGroupName(Mixers mixer) {
        return switch (mixer) {
            case CHAT -> "chat";
            case INVENTORY -> "inventory";
            case ACTION -> "actions";
            case EFFECTS -> "effects";
            case HOTBAR -> "hotbar";
            case TYPING -> "keyboard";
            default -> null;
        };
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setOnExpandToggle(Consumer<CategoryPanel> onExpandToggle) {
        this.onExpandToggle = onExpandToggle;
    }

    public int getHeight() {
        int height = 44; // Header (20px) + slider (24px)
        if (expanded && !sounds.isEmpty()) {
            height += sounds.size() * 20; // Each sound row is 20px
        }
        return height;
    }

    public void init(Minecraft minecraft) {
        // Create volume slider (below the header)
        this.volumeSlider = new VolumeSlider(x + 40, y + 20, width - 80, 20, mixer);

        // Create sound entry rows if expanded
        if (expanded) {
            soundRows.clear();
            int rowY = y + 44;
            for (SoundEntry sound : sounds) {
                SoundEntryRow row = new SoundEntryRow(sound, x + 30, rowY, width - 60);
                row.init(minecraft);
                soundRows.add(row);
                rowY += 20;
            }
        }
    }

    //? if >=26.1 {
    /*public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        Font font = Minecraft.getInstance().font;

        // Draw background
        context.fill(x, y, x + width, y + getHeight(), 0x80000000);

        // Draw header background (darker)
        context.fill(x, y, x + width, y + 20, 0xA0000000);

        // Draw expand/collapse button (only if has sounds)
        if (!sounds.isEmpty()) {
            String arrow = expanded ? "▼" : "▶";
            context.text(font, arrow, x + 4, y + 6, 0xFFFFFFFF);
        }

        // Draw category name
        Component name = Component.translatable(mixer.getTranslationKey());
        context.text(font, name, x + 20, y + 6, 0xFFFFFFFF);

        // Render volume slider (below header)
        if (volumeSlider != null) {
            volumeSlider.extractRenderState(context, mouseX, mouseY, delta);
        }

        // Render sound rows if expanded
        if (expanded) {
            for (SoundEntryRow row : soundRows) {
                row.render(context, mouseX, mouseY, delta);
            }
        }
    }
    *///?} else {
    //? if >=1.20 {
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Font font = Minecraft.getInstance().font;

        // Draw background
        context.fill(x, y, x + width, y + getHeight(), 0x80000000);

        // Draw header background (darker)
        context.fill(x, y, x + width, y + 20, 0xA0000000);

        // Draw expand/collapse button (only if has sounds)
        if (!sounds.isEmpty()) {
            String arrow = expanded ? "▼" : "▶";
            context.drawString(font, arrow, x + 4, y + 6, 0xFFFFFFFF);
        }

        // Draw category name
        Component name = Component.translatable(mixer.getTranslationKey());
        context.drawString(font, name, x + 20, y + 6, 0xFFFFFFFF);

        // Render volume slider (below header)
        if (volumeSlider != null) {
            volumeSlider.render(context, mouseX, mouseY, delta);
        }

        // Render sound rows if expanded
        if (expanded) {
            for (SoundEntryRow row : soundRows) {
                row.render(context, mouseX, mouseY, delta);
            }
        }
    }
    //?} else {
    /*public void render(PoseStack context, int mouseX, int mouseY, float delta) {
        Font font = Minecraft.getInstance().font;

        // Draw background
        fill(context, x, y, x + width, y + getHeight(), 0x80000000);

        // Draw header background (darker)
        fill(context, x, y, x + width, y + 20, 0xA0000000);

        // Draw expand/collapse button (only if has sounds)
        if (!sounds.isEmpty()) {
            String arrow = expanded ? "▼" : "▶";
            font.draw(context, arrow, x + 4, y + 6, 0xFFFFFFFF);
        }

        // Draw category name
        Component name;
        //? if >=1.19 {
        name = Component.translatable(mixer.getTranslationKey());
        //?} else {
        /^name = new net.minecraft.network.chat.TranslatableComponent(mixer.getTranslationKey());
        ^///?}
        font.draw(context, name, x + 20, y + 6, 0xFFFFFFFF);

        // Render volume slider (below header)
        if (volumeSlider != null) {
            volumeSlider.render(context, mouseX, mouseY, delta);
        }

        // Render sound rows if expanded
        if (expanded) {
            for (SoundEntryRow row : soundRows) {
                row.render(context, mouseX, mouseY, delta);
            }
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
        // Check if click is within panel bounds
        if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + getHeight()) {
            return false;
        }

        // Priority 1: Check volume slider first (it's below the header)
        if (volumeSlider != null) {
            int sliderX = x + 40;
            int sliderY = y + 20;
            int sliderWidth = width - 80;
            int sliderHeight = 20;

            // Check if mouse is within slider bounds
            if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth &&
                mouseY >= sliderY && mouseY <= sliderY + sliderHeight) {
                //? if >=26.1 {
                /*if (volumeSlider.mouseClicked(event, bl)) {
                *///?} else {
                if (volumeSlider.mouseClicked(mouseX, mouseY, button)) {
                //?}
                    return true;
                }
            }
        }

        // Priority 2: Check sound rows if expanded
        if (expanded && mouseY >= y + 44) {
            for (SoundEntryRow row : soundRows) {
                //? if >=26.1 {
                /*if (row.mouseClicked(event, bl)) {
                *///?} else {
                if (row.mouseClicked(mouseX, mouseY, button)) {
                //?}
                    return true;
                }
            }
        }

        // Priority 3: Check if clicked on header (expand/collapse) - only if has sounds
        if (!sounds.isEmpty() && mouseY >= y && mouseY < y + 20) {
            System.out.println("[CategoryPanel] Toggling " + mixer.id + " from " + expanded + " to " + !expanded);
            expanded = !expanded;
            if (onExpandToggle != null) {
                onExpandToggle.accept(this);
            }
            return true;
        }

        return false;
    }

    //? if >=26.1 {
    /*public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return mouseDragged(new net.minecraft.client.input.MouseButtonEvent(mouseX, mouseY, new net.minecraft.client.input.MouseButtonInfo(button, 0)), dragX, dragY);
    }

    public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent event, double dragX, double dragY) {
        double mouseX = event.x();
        double mouseY = event.y();
    *///?} else {
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
    //?}
        // Only handle slider dragging if it's active
        if (volumeSlider != null && volumeSlider.isMouseOver(mouseX, mouseY)) {
            //? if >=26.1 {
            /*return volumeSlider.mouseDragged(event, dragX, dragY);
            *///?} else {
            return volumeSlider.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            //?}
        }
        return false;
	}

    //? if >=26.1 {
    /*public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return mouseReleased(new net.minecraft.client.input.MouseButtonEvent(mouseX, mouseY, new net.minecraft.client.input.MouseButtonInfo(button, 0)));
    }

    public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent event) {
        double mouseX = event.x();
        double mouseY = event.y();
    *///?} else {
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
    //?}
        // Only handle slider release if it was clicked
        if (volumeSlider != null) {
            //? if >=26.1 {
            /*return volumeSlider.mouseReleased(event);
            *///?} else {
            return volumeSlider.mouseReleased(mouseX, mouseY, button);
            //?}
        }
        return false;
	}

    /**
     * Custom volume slider for mixer categories.
     */
    private static class VolumeSlider extends AbstractSliderButton {
        private final Mixers mixer;

        public VolumeSlider(int x, int y, int width, int height, Mixers mixer) {
            //? if >=1.19 {
            super(x, y, width, height, Component.empty(), VolumeConfig.getVolume(mixer));
            //?} else {
            /*super(x, y, width, height, new net.minecraft.network.chat.TextComponent(""), VolumeConfig.getVolume(mixer));
            *///?}
            this.mixer = mixer;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int percent = (int)(this.value * 100);
            //? if >=1.19 {
            this.setMessage(Component.literal(percent + "%"));
            //?} else {
            /*this.setMessage(new net.minecraft.network.chat.TextComponent(percent + "%"));
            *///?}
        }

        @Override
        protected void applyValue() {
            VolumeConfig.setVolume(mixer, (float)this.value);
        }
    }
}
