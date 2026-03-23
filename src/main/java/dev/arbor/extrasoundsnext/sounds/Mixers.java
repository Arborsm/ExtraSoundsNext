package dev.arbor.extrasoundsnext.sounds;

public enum Mixers {
    MASTER("master", 0.5f, false, true),
    INVENTORY("inventory", 1.0f, false, true),
    ACTION("action", 1.0f, false, true),
    CHAT("chat", 1.0f, false, true),
    EFFECTS("effects", 1.0f, false, true),
    HOTBAR("hotbar", 1.0f, false, true),
    TYPING("typing", 1.0f, false, true),
    ITEM_DROP("item_drop", 1.0f, true, true),
    EMPTY_HOTBAR("empty_hotbar", 1.0f, true, true),
    ENABLED_FOOTSTEP("enabled_footstep", 1.0f, true, true),
    ENABLED_EFFECTS("enabled_effects", 1.0f, true, false),
    ENABLED_POOF("enabled_poof", 1.0f, true, false);

    public final String id;
    public final float defaultLevel;
    public final boolean toggle;
    public final boolean defaultOn;

    Mixers(String id, float defaultLevel, boolean toggle, boolean defaultOn) {
        this.id = id;
        this.defaultLevel = defaultLevel;
        this.toggle = toggle;
        this.defaultOn = defaultOn;
    }

    public String getTranslationKey() {
        return "extrasounds.mixer." + id;
    }
}
