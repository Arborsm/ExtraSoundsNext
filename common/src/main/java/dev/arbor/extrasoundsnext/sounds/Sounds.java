package dev.arbor.extrasoundsnext.sounds;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.ConstantFloat;
import dev.arbor.extrasoundsnext.ExtraSoundsNext;

import java.util.List;

public class Sounds {
    public static final SoundEvent CHAT = ExtraSoundsNext.createEvent("chat.message");
    public static final SoundEvent CHAT_MENTION = ExtraSoundsNext.createEvent("chat.mention");
    public static final SoundEvent HOTBAR_SCROLL = ExtraSoundsNext.createEvent("hotbar_scroll");
    public static final SoundEvent INVENTORY_OPEN = ExtraSoundsNext.createEvent("inventory.open");
    public static final SoundEvent INVENTORY_CLOSE = ExtraSoundsNext.createEvent("inventory.close");
    public static final SoundEvent INVENTORY_SCROLL = ExtraSoundsNext.createEvent("inventory.scroll");
    public static final SoundEvent ITEM_DROP = ExtraSoundsNext.createEvent("item.drop");
    public static final SoundEvent ITEM_PICK = ExtraSoundsNext.createEvent("item.pickup");
    public static final SoundEvent ITEM_PICK_ALL = ExtraSoundsNext.createEvent("item.pickup_all");
    public static final SoundEvent ITEM_CLONE = ExtraSoundsNext.createEvent("item.clone");
    public static final SoundEvent ITEM_DELETE = ExtraSoundsNext.createEvent("item.delete");
    public static final SoundEvent ITEM_DRAG = ExtraSoundsNext.createEvent("item.drag");
    public static final SoundEvent EFFECT_ADD_POSITIVE = ExtraSoundsNext.createEvent("effect.add.positive");
    public static final SoundEvent EFFECT_ADD_NEGATIVE = ExtraSoundsNext.createEvent("effect.add.negative");
    public static final SoundEvent EFFECT_REMOVE_POSITIVE = ExtraSoundsNext.createEvent("effect.remove.positive");
    public static final SoundEvent EFFECT_REMOVE_NEGATIVE = ExtraSoundsNext.createEvent("effect.remove.negative");
    public static final SoundEvent KEYBOARD_TYPE = ExtraSoundsNext.createEvent("keyboard.type");
    public static final SoundEvent KEYBOARD_MOVE = ExtraSoundsNext.createEvent("keyboard.move");
    public static final SoundEvent KEYBOARD_ERASE = ExtraSoundsNext.createEvent("keyboard.erase");
    public static final SoundEvent KEYBOARD_CUT = ExtraSoundsNext.createEvent("keyboard.cut");
    public static final SoundEvent KEYBOARD_PASTE = ExtraSoundsNext.createEvent("keyboard.paste");

    public static class Actions {
        public static final SoundEvent BOW_PULL = ExtraSoundsNext.createEvent("action.bow");
        public static final SoundEvent REPEATER_ADD = ExtraSoundsNext.createEvent("action.repeater.add");
        public static final SoundEvent REPEATER_RESET = ExtraSoundsNext.createEvent("action.repeater.reset");
    }

    public static class Entities {
        public static final SoundEvent POOF = ExtraSoundsNext.createEvent("entity.poof");
    }

    public static SoundEventRegistration aliased(SoundEvent e) {
        return aliased(e, 1f);
    }

    public static SoundEventRegistration aliased(SoundEvent e, float volume) {
        return single(e.getLocation(), volume, 1f, Sound.Type.SOUND_EVENT);
    }

    public static SoundEventRegistration event(ResourceLocation id) {
        return event(id, 0.6f);
    }

    public static SoundEventRegistration event(ResourceLocation id, float volume) {
        return single(id, volume, 1.7f, Sound.Type.SOUND_EVENT);
    }

    public static SoundEventRegistration single(ResourceLocation id, float volume, float pitch, Sound.Type type) {
        return new SoundEventRegistration(List.of(
                new Sound(id.toString(), ConstantFloat.of(volume), ConstantFloat.of(pitch), 1,
                        type, false, false, 16)
        ), false, null);
    }
}
