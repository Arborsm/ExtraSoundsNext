package org.arbor.extrasounds.sounds;

import net.minecraft.sounds.SoundSource;

public enum SoundType
{
    PICKUP(1f, Mixers.INVENTORY, "item.pickup"),
    PLACE(0.9f, Mixers.INVENTORY, "item.place"),
    HOTBAR(1f, Mixers.HOTBAR, "item.select"),
    EFFECT(1f, Mixers.EFFECTS, "effect"),
    CHAT(1f, Mixers.CHAT, "ui.chat"),
    CHAT_MENTION(1f, Mixers.CHAT_MENTION, "ui.chat"),
    TYPING(1f, Mixers.TYPING, "ui.typing"),
    ACTION(1f, Mixers.ACTION, "action");

    public final float pitch;
    public final SoundSource category;
    public final String prefix;

    SoundType(float pitch, SoundSource category, String prefix)
    {
        this.pitch = pitch;
        this.category = category;
        this.prefix = prefix;
    }
}
