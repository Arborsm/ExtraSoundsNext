package org.arbor.extrasounds.misc;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ESConfig {
    public final ForgeConfigSpec.DoubleValue MASTER; //0.5
    public final ForgeConfigSpec.DoubleValue INVENTORY;
    public final ForgeConfigSpec.DoubleValue ACTION;
    public final ForgeConfigSpec.DoubleValue CHAT;
    public final ForgeConfigSpec.DoubleValue CHAT_MENTION;
    public final ForgeConfigSpec.DoubleValue EFFECTS;
    public final ForgeConfigSpec.DoubleValue HOTBAR;
    public final ForgeConfigSpec.DoubleValue TYPING;
    public final ForgeConfigSpec.BooleanValue ITEM_DROP;
    public final ForgeConfigSpec.BooleanValue EMPTY_HOTBAR;
    ESConfig(ForgeConfigSpec.Builder builder){
        builder.comment("Sound categories").push("Sounds");
        MASTER = builder.comment("Master Sounds").defineInRange("master", 0.5f, 0, 2);
        INVENTORY = builder.comment("Inventory Sounds").defineInRange("inventory", 1f, 0, 2);
        ACTION = builder.comment("Action Sounds").defineInRange("action", 1f, 0, 2);
        CHAT = builder.comment("Chat Sounds").defineInRange("chat", 1f, 0, 2);
        CHAT_MENTION = builder.comment("Chat mention Sounds").defineInRange("chat_mention", 1f, 0, 2);
        EFFECTS = builder.comment("Effects Sounds").defineInRange("effects", 1f, 0, 2);
        HOTBAR = builder.comment("Hotbar Sounds").defineInRange("hotbar", 1f, 0, 2);
        TYPING = builder.comment("Typing Sounds").defineInRange("typing", 1f, 0, 2);
        ITEM_DROP = builder.comment("Item drop Sounds").define("item_drop", true);
        EMPTY_HOTBAR = builder.comment("Empty hotbar Sounds").define("empty_hotbar", true);
        builder.pop();
    }
    public static final ForgeConfigSpec configSpec;
    public static final ESConfig CONFIG;

    static {
        final Pair<ESConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(ESConfig::new);
        configSpec = pair.getRight();
        CONFIG = pair.getLeft();
    }
}
