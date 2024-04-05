package org.arbor.extrasounds.misc;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ESConfig {
    public static final ModConfigSpec configSpec;

    static {
        final Pair<ESConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
                .configure(ESConfig::new);
        configSpec = pair.getRight();
        CONFIG = pair.getLeft();
    }

    public final ModConfigSpec.DoubleValue MASTER; //0.5
    public final ModConfigSpec.DoubleValue INVENTORY;
    public final ModConfigSpec.DoubleValue ACTION;
    public final ModConfigSpec.DoubleValue CHAT;
    public final ModConfigSpec.DoubleValue CHAT_MENTION;
    public final ModConfigSpec.DoubleValue EFFECTS;
    public final ModConfigSpec.DoubleValue HOTBAR;
    public final ModConfigSpec.DoubleValue TYPING;
    public final ModConfigSpec.BooleanValue ITEM_DROP;
    public final ModConfigSpec.BooleanValue EMPTY_HOTBAR;
    public final ModConfigSpec.BooleanValue ENABLED_EFFECTS;
    public static final ESConfig CONFIG;

    ESConfig(ModConfigSpec.Builder builder) {
        builder.comment("Sound categories").push("Sounds");
        MASTER = builder.comment("Master Sounds").defineInRange("master", 0.5f, 0, 2);
        INVENTORY = builder.comment("Inventory Sounds").defineInRange("inventory", 1f, 0, 2);
        ACTION = builder.comment("Action Sounds").defineInRange("action", 1f, 0, 2);
        CHAT = builder.comment("Chat Sounds").defineInRange("chat", 1f, 0, 2);
        CHAT_MENTION = builder.comment("Chat mention Sounds").defineInRange("chat_mention", 1f, 0, 2);
        ENABLED_EFFECTS = builder.comment("Enable Effects Sounds").define("effects_enable", true);
        EFFECTS = builder.comment("Effects Sounds").defineInRange("effects", 1f, 0, 2);
        HOTBAR = builder.comment("Hotbar Sounds").defineInRange("hotbar", 1f, 0, 2);
        TYPING = builder.comment("Typing Sounds").defineInRange("typing", 1f, 0, 2);
        ITEM_DROP = builder.comment("Item drop Sounds").define("item_drop", true);
        EMPTY_HOTBAR = builder.comment("Empty hotbar Sounds").define("empty_hotbar", true);
        builder.pop();
    }
}
