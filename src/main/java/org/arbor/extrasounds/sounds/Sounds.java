package org.arbor.extrasounds.sounds;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.ConstantFloat;
import org.arbor.extrasounds.ExtraSounds;

import java.util.List;

public class Sounds {
    public static final SoundEvent CHAT = ExtraSounds.createEvent("chat.message");
    public static final SoundEvent CHAT_MENTION = ExtraSounds.createEvent("chat.mention");
    public static final SoundEvent HOTBAR_SCROLL = ExtraSounds.createEvent("hotbar_scroll");
    public static final SoundEvent INVENTORY_OPEN = ExtraSounds.createEvent("inventory.open");
    public static final SoundEvent INVENTORY_CLOSE = ExtraSounds.createEvent("inventory.close");
    public static final SoundEvent INVENTORY_SCROLL = ExtraSounds.createEvent("inventory.scroll");
    public static final SoundEvent ITEM_DROP = ExtraSounds.createEvent("item.drop");
    public static final SoundEvent ITEM_PICK = ExtraSounds.createEvent("item.pickup");
    public static final SoundEvent ITEM_PICK_ALL = ExtraSounds.createEvent("item.pickup_all");
    public static final SoundEvent ITEM_CLONE = ExtraSounds.createEvent("item.clone");
    public static final SoundEvent ITEM_DELETE = ExtraSounds.createEvent("item.delete");
    public static final SoundEvent ITEM_DRAG = ExtraSounds.createEvent("item.drag");
    public static final SoundEvent EFFECT_ADD_POSITIVE = ExtraSounds.createEvent("effect.add.positive");
    public static final SoundEvent EFFECT_ADD_NEGATIVE = ExtraSounds.createEvent("effect.add.negative");
    public static final SoundEvent EFFECT_REMOVE_POSITIVE = ExtraSounds.createEvent("effect.remove.positive");
    public static final SoundEvent EFFECT_REMOVE_NEGATIVE = ExtraSounds.createEvent("effect.remove.negative");
    public static final SoundEvent KEYBOARD_TYPE = ExtraSounds.createEvent("keyboard.type");
    public static final SoundEvent KEYBOARD_MOVE = ExtraSounds.createEvent("keyboard.move");
    public static final SoundEvent KEYBOARD_ERASE = ExtraSounds.createEvent("keyboard.erase");
    public static final SoundEvent KEYBOARD_CUT = ExtraSounds.createEvent("keyboard.cut");
    public static final SoundEvent KEYBOARD_PASTE = ExtraSounds.createEvent("keyboard.paste");

    public static class Actions {
        public static final SoundEvent BOW_PULL = ExtraSounds.createEvent("action.bow");
        public static final SoundEvent REPEATER_ADD = ExtraSounds.createEvent("action.repeater.add");
        public static final SoundEvent REPEATER_RESET = ExtraSounds.createEvent("action.repeater.reset");
    }

    public static class Entities {
        public static final SoundEvent POOF = ExtraSounds.createEvent("entity.poof");
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
