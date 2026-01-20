package dev.arbor.extrasoundsnext.sounds;

public enum SoundType {
    PICKUP(1f, Mixers.INVENTORY, "item.pickup"),
    PLACE(0.9f, Mixers.INVENTORY, "item.place"),
    HOTBAR(1f, Mixers.HOTBAR, "item.select"),
    EFFECT(1f, Mixers.EFFECTS, "effect"),
    CHAT(1f, Mixers.CHAT, "ui.chat"),
    CHAT_MENTION(1f, Mixers.CHAT_MENTION, "ui.chat"),
    TYPING(1f, Mixers.TYPING, "ui.typing"),
    ACTION(1f, Mixers.ACTION, "action");

    public final float pitch;
    public final Mixers category;
    public final String prefix;

    SoundType(float pitch, Mixers category, String prefix) {
        this.pitch = pitch;
        this.category = category;
        this.prefix = prefix;
    }
}
