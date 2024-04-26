package org.arbor.extrasounds.sounds;

import net.minecraft.sounds.SoundSource;

public class Mixers implements CategoryLoader {
    @Register(master = true, defaultLevel = 0.5f)
    public static SoundSource MASTER;
    @Register
    public static SoundSource INVENTORY;
    @Register(tooltip = "tooltip.soundCategory.extrasounds_action")
    public static SoundSource ACTION;
    @Register
    public static SoundSource CHAT;
    @Register
    public static SoundSource CHAT_MENTION;
    @Register
    public static SoundSource EFFECTS;
    @Register
    public static SoundSource HOTBAR;
    @Register
    public static SoundSource TYPING;
    @Register(toggle = true)
    public static SoundSource ITEM_DROP;
    @Register(toggle = true)
    public static SoundSource EMPTY_HOTBAR;
    @Register(toggle = true)
    public static SoundSource ENABLED_FOOTSTEP;
    @Register(toggle = true, defaultOn = false)
    public static SoundSource ENABLED_EFFECTS;
    @Register(toggle = true, defaultOn = false)
    public static SoundSource ENABLED_POOF;
}
