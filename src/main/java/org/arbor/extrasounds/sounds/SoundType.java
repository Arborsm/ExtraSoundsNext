package org.arbor.extrasounds.sounds;

import net.minecraft.sounds.SoundSource;
import net.minecraftforge.common.ForgeConfigSpec;
import org.arbor.extrasounds.misc.ESConfig;

public enum SoundType
{
    PICKUP(1f, SoundSource.PLAYERS, "item.pickup", ESConfig.CONFIG.INVENTORY),
    PLACE(0.9f, SoundSource.PLAYERS, "item.place", ESConfig.CONFIG.INVENTORY),
    HOTBAR(1f, SoundSource.PLAYERS, "item.select", ESConfig.CONFIG.HOTBAR),
    EFFECT(1f, SoundSource.PLAYERS, "effect", ESConfig.CONFIG.EFFECTS),
    CHAT(1f, SoundSource.PLAYERS, "ui.chat", ESConfig.CONFIG.CHAT),
    CHAT_MENTION(1f, SoundSource.PLAYERS, "ui.chat", ESConfig.CONFIG.CHAT_MENTION),
    TYPING(1f, SoundSource.PLAYERS, "ui.typing", ESConfig.CONFIG.TYPING),
    ACTION(1f, SoundSource.PLAYERS, "action", ESConfig.CONFIG.ACTION);

    public final float pitch;
    public final SoundSource category;
    public final String prefix;
    public final ForgeConfigSpec.DoubleValue volume;

    SoundType(float pitch, SoundSource category, String prefix, ForgeConfigSpec.DoubleValue volume)
    {
        this.pitch = pitch;
        this.category = category;
        this.prefix = prefix;
        this.volume = volume;
    }
}
