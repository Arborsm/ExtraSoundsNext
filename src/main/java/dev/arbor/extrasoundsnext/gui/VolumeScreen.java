package dev.arbor.extrasoundsnext.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.arbor.extrasoundsnext.gui.components.CategoryPanel;
import dev.arbor.extrasoundsnext.gui.components.SearchBox;
import dev.arbor.extrasoundsnext.sounds.Mixers;
import dev.arbor.extrasoundsnext.sounds.VolumeConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
//? if >=1.20 {
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
//?} else {
/*import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
*///?}

import java.util.ArrayList;
import java.util.List;

/**
 * Volume configuration screen with search, collapsible categories, and individual sound control.
 */
public class VolumeScreen extends Screen {
    private final Screen parent;
    private SearchBox searchBox;
	private final List<CategoryPanel> categoryPanels = new ArrayList<>();
    private double scrollOffset = 0;
    private int contentHeight = 0;
    private String searchQuery = "";
    private boolean isDraggingScrollbar = false;
    private double scrollbarDragStartY = 0;
    private double scrollOffsetAtDragStart = 0;
    private Button resetButton;
    private Button doneButton;

    // Track expanded state for each mixer
    private final java.util.Map<Mixers, Boolean> expandedStates = new java.util.HashMap<>();

    public VolumeScreen(Screen parent) {
        //? if >=1.19 {
        super(Component.translatable("extrasounds.volume.title"));
        //?} else {
        /*super(new net.minecraft.network.chat.TranslatableComponent("extrasounds.volume.title"));
        *///?}
        this.parent = parent;
    }

    @Override
    protected void init() {
        categoryPanels.clear();

        // Search box at top
        Component searchPlaceholder;
        //? if >=1.19 {
        searchPlaceholder = Component.translatable("extrasounds.search.placeholder");
        //?} else {
        /*searchPlaceholder = new net.minecraft.network.chat.TranslatableComponent("extrasounds.search.placeholder");
        *///?}

        this.searchBox = new SearchBox(
            this.font,
            this.width / 2 - 150,
            20,
            280,
            20,
            searchPlaceholder
        );
        this.searchBox.setOnTextChanged(text -> {
            this.searchQuery = text.toLowerCase();
            rebuildPanels();
        });
        this.addRenderableWidget(this.searchBox);
        this.setInitialFocus(this.searchBox);

        // Clear search button
        Component clearText;
        //? if >=1.19 {
        clearText = Component.literal("X");
        //?} else {
        /*clearText = new net.minecraft.network.chat.TextComponent("X");
        *///?}

        //? if >=1.19.3 {
        Button clearButton = Button.builder(clearText, btn -> {
            this.searchBox.setValue("");
        }).bounds(this.width / 2 + 135, 20, 20, 20).build();
        //?} else {
        /*Button clearButton = new Button(this.width / 2 + 135, 20, 20, 20, clearText, btn -> {
            this.searchBox.setValue("");
        });
        *///?}
        this.addRenderableWidget(clearButton);

        // Build category panels
        rebuildPanels();

        // Reset button
        Component resetText;
        //? if >=1.19 {
        resetText = Component.translatable("extrasounds.button.reset");
        //?} else {
        /*resetText = new net.minecraft.network.chat.TranslatableComponent("extrasounds.button.reset");
        *///?}

        //? if >=1.19.3 {
        this.resetButton = Button.builder(resetText, btn -> {
            VolumeConfig.resetToDefaults();
            this.minecraft.setScreen(new VolumeScreen(this.parent));
        }).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build();
        //?} else {
 		/*this.resetButton = new Button(this.width / 2 - 155, this.height - 28, 150, 20, resetText, btn -> {
 			VolumeConfig.resetToDefaults();
 			this.minecraft.setScreen(new VolumeScreen(this.parent));
 		});
        *///?}
        this.addRenderableWidget(resetButton);

        // Done button
        //? if >=1.19.3 {
        this.doneButton = Button.builder(CommonComponents.GUI_DONE, btn -> {
            VolumeConfig.save();
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build();
        //?} else {
 		/*this.doneButton = new Button(this.width / 2 + 5, this.height - 28, 150, 20, CommonComponents.GUI_DONE, btn -> {
 			VolumeConfig.save();
 			this.minecraft.setScreen(this.parent);
 		});
        *///?}
        this.addRenderableWidget(doneButton);
    }

    private void rebuildPanels() {
        categoryPanels.clear();

        int panelY = 50 + (int)scrollOffset;
        int panelWidth = 320;
        int centerX = this.width / 2 - panelWidth / 2;

        // MASTER panel (always visible)
        CategoryPanel masterPanel = new CategoryPanel(Mixers.MASTER, centerX, panelY, panelWidth);
        masterPanel.setExpanded(expandedStates.getOrDefault(Mixers.MASTER, false));
        masterPanel.setOnExpandToggle(panel -> {
            expandedStates.put(Mixers.MASTER, panel.isExpanded());
            rebuildPanels();
        });
        masterPanel.init(this.minecraft);
        categoryPanels.add(masterPanel);
        panelY += masterPanel.getHeight() + 4;

        // Other category panels
        Mixers[] categories = {
            Mixers.INVENTORY,
            Mixers.CHAT,
            Mixers.HOTBAR,
            Mixers.TYPING,
            Mixers.EFFECTS,
            Mixers.ACTION
        };

        for (Mixers mixer : categories) {
            // Filter by search query
            if (!searchQuery.isEmpty()) {
                Component name;
                //? if >=1.19 {
                name = Component.translatable(mixer.getTranslationKey());
                //?} else {
                /*name = new net.minecraft.network.chat.TranslatableComponent(mixer.getTranslationKey());
                *///?}
                if (!name.getString().toLowerCase().contains(searchQuery)) {
                    continue;
                }
            }

            CategoryPanel panel = new CategoryPanel(mixer, centerX, panelY, panelWidth);
            panel.setExpanded(expandedStates.getOrDefault(mixer, false));
            panel.setOnExpandToggle(p -> {
                expandedStates.put(mixer, p.isExpanded());
                rebuildPanels();
            });
            panel.init(this.minecraft);
            categoryPanels.add(panel);
            panelY += panel.getHeight() + 4;
        }

        contentHeight = panelY - 50 - (int)scrollOffset;
    }

    //? if >=1.20 {
    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
		//? if >= 1.21 {
		this.renderBackground(context, mouseX, mouseY, delta);
		//?} else {
		/*this.renderBackground(context);
		*///?}
		super.render(context, mouseX, mouseY, delta);

		// Draw title
        context.drawCenteredString(this.font, this.title, this.width / 2, 6, 0xFFFFFFFF);

        // Define scrollable area
        int scrollAreaTop = 50;
        int scrollAreaBottom = this.height - 30;
        int scrollAreaHeight = scrollAreaBottom - scrollAreaTop;

        // Enable scissor test for scrollable content
        context.enableScissor(0, scrollAreaTop, this.width, scrollAreaBottom);

        // Render category panels
        for (CategoryPanel panel : categoryPanels) {
            panel.render(context, mouseX, mouseY, delta);
        }

        // Disable scissor test
        context.disableScissor();

        // Draw scrollbar if content is larger than viewport
        int maxScroll = Math.max(0, contentHeight - scrollAreaHeight);
        if (maxScroll > 0) {
            drawScrollbar(context, mouseX, mouseY, scrollAreaTop, scrollAreaBottom, maxScroll);
        }

        // Render search box and buttons on top
        this.searchBox.render(context, mouseX, mouseY, delta);
    }
    //?} else {
    /*@Override
    public void render(@NotNull PoseStack context, int mouseX, int mouseY, float delta) {
        //? if >=1.19 {
        this.renderBackground(context);
        //?} else {
        /^this.renderBackground(context, 0);
        ^///?}

        // Draw title
        //? if >=1.19 {
        drawCenteredString(context, this.font, this.title, this.width / 2, 6, 0xFFFFFFFF);
        //?} else {
        /^drawCenteredString(context, this.font, this.title, this.width / 2, 6, 0xFFFFFFFF);
        ^///?}

        // Define scrollable area
        int scrollAreaTop = 50;
        int scrollAreaBottom = this.height - 30;
        int scrollAreaHeight = scrollAreaBottom - scrollAreaTop;

        // Enable scissor test for scrollable content
        enableScissorTest(0, scrollAreaTop, this.width, scrollAreaHeight);

        // Render category panels
        for (CategoryPanel panel : categoryPanels) {
            panel.render(context, mouseX, mouseY, delta);
        }

        // Disable scissor test
        disableScissorTest();

        // Draw scrollbar if content is larger than viewport
        int maxScroll = Math.max(0, contentHeight - scrollAreaHeight);
        if (maxScroll > 0) {
            drawScrollbar(context, mouseX, mouseY, scrollAreaTop, scrollAreaBottom, maxScroll);
        }

        // Render search box and buttons on top
        this.searchBox.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
    *///?}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check scrollbar first
        int scrollAreaTop = 50;
        int scrollAreaBottom = this.height - 30;
        int scrollAreaHeight = scrollAreaBottom - scrollAreaTop;
        int maxScroll = Math.max(0, contentHeight - scrollAreaHeight);

        if (maxScroll > 0 && button == 0) {
            int scrollbarX = this.width - 10;
            int scrollbarWidth = 6;

            // Calculate thumb position
            float contentRatio = (float)scrollAreaHeight / (float)(contentHeight);
            int thumbHeight = Math.max(20, (int)(scrollAreaHeight * contentRatio));
            float scrollRatio = (float)(-scrollOffset) / (float)maxScroll;
            int thumbY = scrollAreaTop + (int)((scrollAreaHeight - thumbHeight) * scrollRatio);

            // Check if clicked on scrollbar thumb
            if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth &&
                mouseY >= thumbY && mouseY <= thumbY + thumbHeight) {
                isDraggingScrollbar = true;
                scrollbarDragStartY = mouseY;
                scrollOffsetAtDragStart = scrollOffset;
                return true;
            }
        }

        // Check buttons first (Done, Reset, Clear)
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        // Check search box
        if (this.searchBox.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        // Check category panels
        for (CategoryPanel panel : categoryPanels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        // Handle scrollbar dragging
        if (isDraggingScrollbar) {
            int scrollAreaTop = 50;
            int scrollAreaBottom = this.height - 30;
            int scrollAreaHeight = scrollAreaBottom - scrollAreaTop;
            int maxScroll = Math.max(0, contentHeight - scrollAreaHeight);

            float contentRatio = (float)scrollAreaHeight / (float)(contentHeight);
            int thumbHeight = Math.max(20, (int)(scrollAreaHeight * contentRatio));
            int availableScrollSpace = scrollAreaHeight - thumbHeight;

            double dragDelta = mouseY - scrollbarDragStartY;
            double scrollDelta = (dragDelta / availableScrollSpace) * maxScroll;

            scrollOffset = Math.max(-maxScroll, Math.min(0, scrollOffsetAtDragStart - scrollDelta));
            rebuildPanels();
            return true;
        }

        // Check buttons first
        if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
            return true;
        }

        // Check category panels
        for (CategoryPanel panel : categoryPanels) {
            if (panel.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Release scrollbar drag
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }

        // Check buttons first
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }

        // Check category panels
        for (CategoryPanel panel : categoryPanels) {
            if (panel.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    //? if >=1.21 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (isDraggingScrollbar) {
            return false;
        }

        // Scroll content
        int scrollAreaHeight = this.height - 80;
        int maxScroll = Math.max(0, contentHeight - scrollAreaHeight);
        scrollOffset = Math.max(-maxScroll, Math.min(0, scrollOffset - verticalAmount * 10));
        rebuildPanels();
        return true;
    }
    //?} else {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (isDraggingScrollbar) {
            return false;
        }

        // Scroll content
        int scrollAreaHeight = this.height - 80;
        int maxScroll = Math.max(0, contentHeight - scrollAreaHeight);
        scrollOffset = Math.max(-maxScroll, Math.min(0, scrollOffset + amount * 10));
        rebuildPanels();
        return true;
    }
    *///?}

    private void enableScissorTest(int x, int y, int width, int height) {
        double scale = this.minecraft.getWindow().getGuiScale();
        int scaledY = (int)(this.minecraft.getWindow().getHeight() - (y + height) * scale);
        RenderSystem.enableScissor(
            (int)(x * scale),
            scaledY,
            (int)(width * scale),
            (int)(height * scale)
        );
    }

    private void disableScissorTest() {
        RenderSystem.disableScissor();
    }

    //? if >=1.20 {
    private void drawScrollbar(GuiGraphics context, int mouseX, int mouseY, int top, int bottom, int maxScroll) {
        int scrollbarX = this.width - 10;
        int scrollbarWidth = 6;
        int scrollbarHeight = bottom - top;

        // Calculate scrollbar thumb size and position
        float contentRatio = (float)scrollbarHeight / (float)(contentHeight);
        int thumbHeight = Math.max(20, (int)(scrollbarHeight * contentRatio));
        float scrollRatio = (float)(-scrollOffset) / (float)maxScroll;
        int thumbY = top + (int)((scrollbarHeight - thumbHeight) * scrollRatio);

        // Draw scrollbar track
        context.fill(scrollbarX, top, scrollbarX + scrollbarWidth, bottom, 0x80000000);

        // Draw scrollbar thumb
        boolean isHovered = mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth &&
                           mouseY >= thumbY && mouseY <= thumbY + thumbHeight;
        int thumbColor = isHovered || isDraggingScrollbar ? 0xFFAAAAAA : 0xFF888888;
        context.fill(scrollbarX, thumbY, scrollbarX + scrollbarWidth, thumbY + thumbHeight, thumbColor);
    }
    //?} else {
    /*private void drawScrollbar(PoseStack context, int mouseX, int mouseY, int top, int bottom, int maxScroll) {
        int scrollbarX = this.width - 10;
        int scrollbarWidth = 6;
        int scrollbarHeight = bottom - top;

        // Calculate scrollbar thumb size and position
        float contentRatio = (float)scrollbarHeight / (float)(contentHeight);
        int thumbHeight = Math.max(20, (int)(scrollbarHeight * contentRatio));
        float scrollRatio = (float)(-scrollOffset) / (float)maxScroll;
        int thumbY = top + (int)((scrollbarHeight - thumbHeight) * scrollRatio);

        // Draw scrollbar track
        Screen.fill(context, scrollbarX, top, scrollbarX + scrollbarWidth, bottom, 0x80000000);

        // Draw scrollbar thumb
        boolean isHovered = mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth &&
                           mouseY >= thumbY && mouseY <= thumbY + thumbHeight;
        int thumbColor = isHovered || isDraggingScrollbar ? 0xFFAAAAAA : 0xFF888888;
        Screen.fill(context, scrollbarX, thumbY, scrollbarX + scrollbarWidth, thumbY + thumbHeight, thumbColor);
    }
    *///?}

    @Override
    public void removed() {
        VolumeConfig.save();
    }
}
