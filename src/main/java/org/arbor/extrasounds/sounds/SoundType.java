package org.arbor.extrasounds.sounds;

import net.minecraft.sounds.SoundSource;

public enum SoundType
{
    PICKUP(1f, SoundSource.PLAYERS, "item.pickup"),
    PLACE(0.9f, SoundSource.PLAYERS, "item.place"),
    HOTBAR(1f, SoundSource.PLAYERS, "item.select"),
    EFFECT(1f, SoundSource.PLAYERS, "effect"),
    CHAT(1f, SoundSource.PLAYERS, "ui.chat"),
    CHAT_MENTION(1f, SoundSource.PLAYERS, "ui.chat"),
    TYPING(1f, SoundSource.PLAYERS, "ui.typing"),
    ACTION(1f, SoundSource.PLAYERS, "action");

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
