package org.arbor.extrasounds.sounds;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.arbor.extrasounds.ExtraSounds;

import java.util.List;

public class Sounds {
    public static final SoundEvent CHAT = register("chat.message");
    public static final SoundEvent CHAT_MENTION = register("chat.mention");
    public static final SoundEvent HOTBAR_SCROLL = register("hotbar_scroll");
    public static final SoundEvent INVENTORY_OPEN = register("inventory.open");
    public static final SoundEvent INVENTORY_CLOSE = register("inventory.close");
    public static final SoundEvent INVENTORY_SCROLL = register("inventory.scroll");
    public static final SoundEvent ITEM_DROP = register("item.drop");
    public static final SoundEvent ITEM_PICK = register("item.pickup");
    public static final SoundEvent ITEM_PICK_ALL = register("item.pickup_all");
    public static final SoundEvent ITEM_CLONE = register("item.clone");
    public static final SoundEvent ITEM_DELETE = register("item.delete");
    public static final SoundEvent ITEM_DRAG = register("item.drag");
    public static final SoundEvent EFFECT_ADD_POSITIVE = register("effect.add.positive");
    public static final SoundEvent EFFECT_ADD_NEGATIVE = register("effect.add.negative");
    public static final SoundEvent EFFECT_REMOVE_POSITIVE = register("effect.remove.positive");
    public static final SoundEvent EFFECT_REMOVE_NEGATIVE = register("effect.remove.negative");
    public static final SoundEvent KEYBOARD_TYPE = register("keyboard.type");
    public static final SoundEvent KEYBOARD_MOVE = register("keyboard.move");
    public static final SoundEvent KEYBOARD_ERASE = register("keyboard.erase");
    public static final SoundEvent KEYBOARD_CUT = register("keyboard.cut");
    public static final SoundEvent KEYBOARD_PASTE = register("keyboard.paste");

    public static class Actions {
        public static final SoundEvent BOW_PULL = register("action.bow");
        public static final SoundEvent REPEATER_ADD = register("action.repeater.add");
        public static final SoundEvent REPEATER_RESET = register("action.repeater.reset");
    }

    public static class Entities {
        public static final SoundEvent POOF = register("entity.poof");
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
                new Sound(id.toString(), volume, pitch, 1,
                        type, false, false, 16)
        ), false, null);
    }

    public static SoundEvent register(String id) {
        return new SoundEvent(new ResourceLocation(ExtraSounds.MODID, id));
    }
}
