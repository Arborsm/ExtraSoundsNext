package dev.arbor.extrasoundsnext.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Search box with real-time filtering capability.
 */
public class SearchBox extends EditBox {
    private Consumer<String> onTextChanged;

    public SearchBox(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
        this.setMaxLength(100);
    }

    public void setOnTextChanged(Consumer<String> onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    @Override
    public void setValue(@NotNull String text) {
        super.setValue(text);
        if (this.onTextChanged != null) {
            this.onTextChanged.accept(text);
        }
    }

    //? if >=26.1 {
    /*@Override
    public boolean charTyped(net.minecraft.client.input.CharacterEvent event) {
        boolean result = super.charTyped(event);
        if (result && this.onTextChanged != null) {
            this.onTextChanged.accept(this.getValue());
        }
        return result;
    }
    *///?} else {
    @Override
    public boolean charTyped(char chr, int modifiers) {
        boolean result = super.charTyped(chr, modifiers);
        if (result && this.onTextChanged != null) {
            this.onTextChanged.accept(this.getValue());
        }
        return result;
    }
    //?}

    //? if >=26.1 {
    /*@Override
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        boolean result = super.keyPressed(event);
        if (this.onTextChanged != null) {
            this.onTextChanged.accept(this.getValue());
        }
        return result;
    }
    *///?} else {
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean result = super.keyPressed(keyCode, scanCode, modifiers);
        if (this.onTextChanged != null) {
            this.onTextChanged.accept(this.getValue());
        }
        return result;
    }
    //?}
}
