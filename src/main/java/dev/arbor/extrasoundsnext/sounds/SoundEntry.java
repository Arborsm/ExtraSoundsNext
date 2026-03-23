package dev.arbor.extrasoundsnext.sounds;

import net.minecraft.sounds.SoundEvent;

/**
 * Enumeration of all individual sound effects in ExtraSounds.
 * Each entry maps to a specific SoundEvent and belongs to a category group.
 */
public enum SoundEntry {
    // Chat group (2 sounds)
    CHAT("chat", Sounds.CHAT, SoundType.CHAT, "chat"),

    // Inventory group (3 sounds)
    INVENTORY_OPEN("inventory_open", Sounds.INVENTORY_OPEN, SoundType.PICKUP, "inventory"),
    INVENTORY_CLOSE("inventory_close", Sounds.INVENTORY_CLOSE, SoundType.PICKUP, "inventory"),
    INVENTORY_SCROLL("inventory_scroll", Sounds.INVENTORY_SCROLL, SoundType.PICKUP, "inventory"),

    // Item operations group (6 sounds)
    ITEM_PICK("item_pick", Sounds.ITEM_PICK, SoundType.PICKUP, "item_operations"),
    ITEM_PICK_ALL("item_pick_all", Sounds.ITEM_PICK_ALL, SoundType.PICKUP, "item_operations"),
    ITEM_DROP("item_drop", Sounds.ITEM_DROP, SoundType.PICKUP, "item_operations"),
    ITEM_CLONE("item_clone", Sounds.ITEM_CLONE, SoundType.PICKUP, "item_operations"),
    ITEM_DELETE("item_delete", Sounds.ITEM_DELETE, SoundType.PICKUP, "item_operations"),
    ITEM_DRAG("item_drag", Sounds.ITEM_DRAG, SoundType.PLACE, "item_operations"),

    // Hotbar group (1 sound)
    HOTBAR_SCROLL("hotbar_scroll", Sounds.HOTBAR_SCROLL, SoundType.HOTBAR, "hotbar"),

    // Keyboard group (5 sounds)
    KEYBOARD_TYPE("keyboard_type", Sounds.KEYBOARD_TYPE, SoundType.TYPING, "keyboard"),
    KEYBOARD_MOVE("keyboard_move", Sounds.KEYBOARD_MOVE, SoundType.TYPING, "keyboard"),
    KEYBOARD_ERASE("keyboard_erase", Sounds.KEYBOARD_ERASE, SoundType.TYPING, "keyboard"),
    KEYBOARD_CUT("keyboard_cut", Sounds.KEYBOARD_CUT, SoundType.TYPING, "keyboard"),
    KEYBOARD_PASTE("keyboard_paste", Sounds.KEYBOARD_PASTE, SoundType.TYPING, "keyboard"),

    // Effects group (4 sounds)
    EFFECT_ADD_POSITIVE("effect_add_positive", Sounds.EFFECT_ADD_POSITIVE, SoundType.EFFECT, "effects"),
    EFFECT_ADD_NEGATIVE("effect_add_negative", Sounds.EFFECT_ADD_NEGATIVE, SoundType.EFFECT, "effects"),
    EFFECT_REMOVE_POSITIVE("effect_remove_positive", Sounds.EFFECT_REMOVE_POSITIVE, SoundType.EFFECT, "effects"),
    EFFECT_REMOVE_NEGATIVE("effect_remove_negative", Sounds.EFFECT_REMOVE_NEGATIVE, SoundType.EFFECT, "effects"),

    // Actions group (3 sounds)
    BOW_PULL("bow_pull", Sounds.Actions.BOW_PULL, SoundType.ACTION, "actions"),
    REPEATER_ADD("repeater_add", Sounds.Actions.REPEATER_ADD, SoundType.ACTION, "actions"),
    REPEATER_RESET("repeater_reset", Sounds.Actions.REPEATER_RESET, SoundType.ACTION, "actions"),

    // Entities group (1 sound)
    ENTITY_POOF("entity_poof", Sounds.Entities.POOF, SoundType.ACTION, "entities");

    public final String id;
    public final SoundEvent soundEvent;
    public final SoundType soundType;
    public final String group;

    SoundEntry(String id, SoundEvent soundEvent, SoundType soundType, String group) {
        this.id = id;
        this.soundEvent = soundEvent;
        this.soundType = soundType;
        this.group = group;
    }

    /**
     * Gets the translation key for this sound entry.
     * @return Translation key in format "extrasounds.sound.{id}"
     */
    public String getTranslationKey() {
        return "extrasounds.sound." + id;
    }

    /**
     * Finds the SoundEntry corresponding to a given SoundEvent.
     * @param event The SoundEvent to look up
     * @return The matching SoundEntry, or null if not found
     */
    public static SoundEntry fromSoundEvent(SoundEvent event) {
        if (event == null) return null;
        for (SoundEntry entry : values()) {
            if (entry.soundEvent == event) {
                return entry;
            }
        }
        return null;
    }
}
