package dev.arbor.extrasoundsnext.sounds;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
//? if >=1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
//? if >=1.19 {
import net.minecraft.util.RandomSource;
//?} else {
/*import java.util.Random;
*///?}
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.debug.DebugUtils;
import dev.arbor.extrasoundsnext.mapping.SoundPackLoader;
import org.jetbrains.annotations.Nullable;
import dev.arbor.extrasoundsnext.reg.ExHelper;

import java.util.Map;
import java.util.function.BiPredicate;

public class SoundManager {
    private static final Logger LOGGER = LogManager.getLogger();
    //? if >=1.19 {
    private static final RandomSource MC_RANDOM = RandomSource.create();
    //?} else {
    /*private static final Random MC_RANDOM = new java.util.Random();
    *///?}

    /**
     * Predicate of Right Mouse Click.
     */
    private static final BiPredicate<ClickType, Integer> RIGHT_CLICK_PREDICATE = (actionType, button) -> (
            actionType != ClickType.THROW && actionType != ClickType.SWAP) && button == 1 ||
            actionType == ClickType.QUICK_CRAFT && AbstractContainerMenu.getQuickcraftType(button) == 1;

    /**
     * Map of the item which should not play sounds.<br>
     * BiPredicate in this value will be passed <code>SlotActionType</code> and <code>int</code> of button ID.<br>
     * Item -&gt; BiPredicate&lt;SlotActionType, Integer&gt;
     */
    private static final Map<Item, BiPredicate<ClickType, Integer>> IGNORE_SOUND_PREDICATE_MAP = Util.make(
            Maps.newHashMap(), map -> map.put(Items.BUNDLE, RIGHT_CLICK_PREDICATE)
    );

    private static long lastPlayed = 0;
    private static Item quickMovingItem = Items.AIR;

    public enum KeyType {
        ERASE,
        CUT,
        INSERT,
        PASTE,
        RETURN,
        CURSOR
    }

    public enum EffectType {
        ADD,
        REMOVE
    }

    public static void hotbar(int i) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        ItemStack stack = player.getInventory().getItem(i);
        if (stack.getItem() == Items.AIR) {
            playSound(Sounds.HOTBAR_SCROLL, SoundType.HOTBAR, Mixers.EMPTY_HOTBAR);
        } else {
            playSound(stack, SoundType.HOTBAR);
        }
    }

    public static void inventoryClick(ItemStack inSlot, ItemStack onCursor, ClickType actionType) {
        final boolean hasCursor = !onCursor.isEmpty();
        final boolean hasSlot = !inSlot.isEmpty();
        if (!hasCursor && !hasSlot) {
            return;
        }

        switch (actionType) {
            case PICKUP_ALL -> {
                if (hasCursor && onCursor.getCount() < onCursor.getMaxStackSize()) {
                    playSound(Sounds.ITEM_PICK_ALL, SoundType.PICKUP);
                }
            }
            case THROW -> {
                if (!hasCursor) {
                    playThrow(inSlot);
                }
            }
            case QUICK_MOVE -> {
                if (hasSlot) {
                    handleQuickMoveSound(inSlot);
                }
            }
            default -> {
                if (hasSlot) {
                    playSound(inSlot, SoundType.PICKUP);
                } else {
                    playSound(onCursor, SoundType.PLACE);
                }
            }
        }
    }

    /**
     * Handles Click and KeyPress on inventory
     *
     * @param player     player instance
     * @param slot       slot in ScreenHandler
     * @param slotIndex  slotIndex
     * @param cursor     item that held by cursor
     * @param actionType action type
     * @param button     clicked mouse, pressed key or including QuickCraftStage
     */
    public static void handleInventorySlot(Player player, @Nullable Slot slot, int slotIndex, ItemStack cursor, ClickType actionType, int button) {
        if (actionType == ClickType.QUICK_CRAFT && AbstractContainerMenu.getQuickcraftHeader(button) < 2) {
            // while dragging
            return;
        }
        if (slotIndex == -1) {
            // screen border clicked
            return;
        }

        // Determine Slot item
        final ItemStack slotItem = (slot == null) ? ItemStack.EMPTY : slot.getItem().copy();
        if (actionType == ClickType.QUICK_MOVE) {
            // cursor holding an item, then Shift + mouse (double) click
            handleQuickMoveSound(slotItem);
            return;
        }

        // Determine Cursor item
        final ItemStack cursorItem;
        if (actionType == ClickType.SWAP) {
            // Swap event
            if (Inventory.isHotbarSlot(button)) {
                // Pressed hotbar key
                cursorItem = player.getInventory().getItem(button).copy();
            } else {
                // Pressed an offhand key
                cursorItem = player.getOffhandItem().copy();
            }
        } else {
            cursorItem = cursor.copy();
        }

        if (slotIndex == AbstractContainerMenu.SLOT_CLICKED_OUTSIDE && actionType != ClickType.QUICK_CRAFT) {
            // out of screen area
            if (RIGHT_CLICK_PREDICATE.test(actionType, button)) {
                cursorItem.setCount(1);
            }
            playThrow(cursorItem);
            return;
        }

        if (actionType == ClickType.THROW && button == 0) {
            // one item drop from stack (default: Q key)
            slotItem.setCount(1);
        }

        // Test if the item should not play sound
        try {
            var predicateForCursor = IGNORE_SOUND_PREDICATE_MAP.getOrDefault(cursorItem.getItem(), null);
            if (predicateForCursor != null && predicateForCursor.test(actionType, button)) {
                return;
            }
            var predicateForSlot = IGNORE_SOUND_PREDICATE_MAP.getOrDefault(slotItem.getItem(), null);
            if (predicateForSlot != null && predicateForSlot.test(actionType, button)) {
                return;
            }
        } catch (Throwable ignore) {
        }

        inventoryClick(slotItem, cursorItem, actionType);
    }

    public static void playSound(ItemStack stack, SoundType type) {
        //? if >=1.19.3 {
        var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        //?} else {
        /*var itemId = Registry.ITEM.getKey(stack.getItem());
        *///?}
        ResourceLocation id = ExtraSoundsNext.getClickId(itemId, type);
        SoundEvent event = SoundPackLoader.CUSTOM_SOUND_EVENT.getOrDefault(id, null);
        if (event == null) {
            playDefaultSound(stack, type);
            return;
        }
        playSound(event, type);
    }

    public static void playDefaultSound(ItemStack stack, SoundType type) {
        //? if >=1.19.3 {
        ResourceLocation defaultItem = ExtraSoundsNext.getClickId(BuiltInRegistries.ITEM.getKey(Items.DIAMOND), type);
        ResourceLocation defaultBlock = ExtraSoundsNext.getClickId(BuiltInRegistries.ITEM.getKey(Items.STONE), type);
        //?} else {
        /*ResourceLocation defaultItem = ExtraSoundsNext.getClickId(Registry.ITEM.getKey(Items.DIAMOND), type);
        ResourceLocation defaultBlock = ExtraSoundsNext.getClickId(Registry.ITEM.getKey(Items.STONE), type);
        *///?}
        SoundEvent defaultSound;
        if (stack.getItem() instanceof BlockItem){
            defaultSound = SoundPackLoader.CUSTOM_SOUND_EVENT.get(defaultBlock);
        }else{
            defaultSound = SoundPackLoader.CUSTOM_SOUND_EVENT.get(defaultItem);
        }
        playSound(defaultSound, type);
    }

    public static void effectChanged(MobEffect effect, EffectType type) {
        if (DebugUtils.DEBUG) {
            DebugUtils.effectLog(effect, type);
        }

        final SoundEvent event;
        if (type == EffectType.ADD) {
            event = switch (effect.getCategory()) {
                case HARMFUL -> Sounds.EFFECT_ADD_NEGATIVE;
                case NEUTRAL, BENEFICIAL -> Sounds.EFFECT_ADD_POSITIVE;
            };
        } else if (type == EffectType.REMOVE) {
            event = switch (effect.getCategory()) {
                case HARMFUL -> Sounds.EFFECT_REMOVE_NEGATIVE;
                case NEUTRAL, BENEFICIAL -> Sounds.EFFECT_REMOVE_POSITIVE;
            };
        } else {
            LOGGER.error("[{}] Unknown type of '{}' is approaching: '{}'", ExtraSoundsNext.class.getSimpleName(), EffectType.class.getSimpleName(), type);
            return;
        }
        playSound(event, SoundType.EFFECT, Mixers.ENABLED_EFFECTS);
    }

    public static void playSound(SoundEvent snd, SoundType type) {
        // Check if this specific sound is enabled
        SoundEntry entry = SoundEntry.fromSoundEvent(snd);
        if (entry != null && !VolumeConfig.isSoundEnabled(entry)) {
            return; // Sound is disabled, skip playback
        }
        playSound(snd, type.pitch, type.category);
    }

    public static void playSound(SoundEvent snd, SoundType type, Mixers... optionalVolumes) {
        // Check if this specific sound is enabled
        SoundEntry entry = SoundEntry.fromSoundEvent(snd);
        if (entry != null && !VolumeConfig.isSoundEnabled(entry)) {
            return; // Sound is disabled, skip playback
        }

        float volume = getSoundVolume(Mixers.MASTER) * getSoundVolume(type.category);
        if (optionalVolumes != null) {
            for (Mixers cat : optionalVolumes) {
                volume *= getSoundVolume(cat);
            }
        }
        //? if >=1.19 {
        playSound(new SimpleSoundInstance(snd == null ? ExHelper.id("missing") : snd.getLocation(), SoundSource.MASTER, volume, type.pitch, MC_RANDOM,
                false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D,
                true));
        //?} else {
		/*playSound(new SimpleSoundInstance(snd == null ? ExHelper.id("missing") : snd.getLocation(), SoundSource.MASTER, volume, type.pitch,
				false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D,
				true));
        *///?}
    }

    public static void playSound(SoundEvent snd, float pitch, Mixers... optionalVolumes) {
        float volume = getSoundVolume(Mixers.MASTER);
        if (optionalVolumes != null) {
            for (Mixers cat : optionalVolumes) {
                volume *= getSoundVolume(cat);
            }
        }
        //? if >=1.19 {
        playSound(new SimpleSoundInstance(snd == null ? ExHelper.id("missing") : snd.getLocation(), SoundSource.MASTER, volume, pitch, MC_RANDOM,
                false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D,
                true));
        //?} else {
		/*playSound(new SimpleSoundInstance(snd == null ? ExHelper.id("missing") : snd.getLocation(), SoundSource.MASTER, volume, pitch,
				false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D,
				true));
        *///?}
    }

    //? if =1.19.2 {
    /*public static void playSound(SoundEvent snd, float pitch, SoundSource category, SoundSource... optionalVolumes) {
        float volume = getSoundVolume(Mixers.MASTER);
        if (optionalVolumes != null) {
            for (SoundSource cat : optionalVolumes) {
                volume = Math.min(getSoundVolume(cat), volume);
            }
        }
        playSound(new SimpleSoundInstance(snd == null ? ExHelper.id("missing") : snd.getLocation(), category, volume, pitch, MC_RANDOM,
                false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D,
                true));
    }
    *///?} elif <1.19.3 {
    /*public static void playSound(SoundEvent snd, float pitch, SoundSource category, SoundSource... optionalVolumes) {
        float volume = getSoundVolume(Mixers.MASTER);
        if (optionalVolumes != null) {
            for (SoundSource cat : optionalVolumes) {
                volume *= getSoundVolume(cat);
            }
        }
        playSound(new SimpleSoundInstance(snd, category, volume, pitch, 0.0D, 0.0D, 0.0D));
    }
    *///?}

    public static void playSound(SoundEvent snd, SoundType type, float volume, float pitch, BlockPos position, Mixers... optionalVolumes) {
        playSound(snd, type, volume, pitch, position, false, optionalVolumes);
    }

    public static void playSound(SoundEvent snd, SoundType type, float volume, float pitch, BlockPos position, boolean anti, Mixers... optionalVolumes) {
        volume *= getSoundVolume(Mixers.MASTER) * getSoundVolume(type);
        if (optionalVolumes != null) {
            for (Mixers cat : optionalVolumes) {
                volume *= getSoundVolume(cat, anti);
            }
        }
        //? if >=1.19 {
        playSound(new SimpleSoundInstance(snd, SoundSource.MASTER, volume, pitch, MC_RANDOM, position));
        //?} else {
        /*playSound(new SimpleSoundInstance(snd, SoundSource.MASTER, volume, pitch, position));
        *///?}
    }

    public static void playSound(SoundEvent snd, SoundType type, Mixers enabledFootstep, BlockPos position) {
        playSound(snd, type, 1f, type.pitch, position, enabledFootstep);
    }

    public static void playSound(SoundInstance instance) {
        try {
            long now = System.currentTimeMillis();
            if (now - lastPlayed > 5) {
                final Minecraft client = Minecraft.getInstance();
                client.tell(() -> client.getSoundManager().play(instance));
                lastPlayed = now;
                if (DebugUtils.DEBUG) {
                    DebugUtils.soundLog(instance);
                }
            } else {
                if (DebugUtils.DEBUG) {
                    LOGGER.warn("Sound suppressed due to the fast interval between method calls, was '{}'.", instance.getLocation());
                }
            }
        } catch (Throwable e) {
            LOGGER.error("Failed to play sound", e);
        }
    }

    public static void playThrow(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }
        final float maxPitch = 2f;
        final float pitch = (!itemStack.isStackable()) ? maxPitch :
                //? if >=1.21 {
                Mth.clampedLerp(maxPitch, 1.5f, (float) itemStack.getCount() / itemStack.getItem().getDefaultMaxStackSize());
                //?} else {
                /*Mth.clampedLerp(maxPitch, 1.5f, (float) itemStack.getCount() / itemStack.getItem().getMaxStackSize());
                *///?}
        playSound(Sounds.ITEM_DROP, pitch, Mixers.ITEM_DROP);
    }

    public static void stopSound(SoundEvent e, SoundType type) {
        Minecraft.getInstance().getSoundManager().stop(e.getLocation(), SoundSource.MASTER);
    }

    /**
     * SlotActionType.QUICK_MOVE is too many method calls
     *
     * @param itemStack target item to quickMove
     * @see net.minecraft.client.multiplayer.MultiPlayerGameMode#handleInventoryMouseClick
     * @see AbstractContainerMenu
     */
    public static void handleQuickMoveSound(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastPlayed > 50 || !itemStack.is(quickMovingItem)) {
            playSound(itemStack, SoundType.PICKUP);
            lastPlayed = now;
            quickMovingItem = itemStack.getItem();
        }
    }

    public static void keyboard(KeyType type) {
        switch (type) {
            case ERASE -> playSound(Sounds.KEYBOARD_ERASE, SoundType.TYPING);
            case CUT -> playSound(Sounds.KEYBOARD_CUT, SoundType.TYPING);
            case CURSOR, RETURN -> playSound(Sounds.KEYBOARD_MOVE, SoundType.TYPING);
            case INSERT -> playSound(Sounds.KEYBOARD_TYPE, SoundType.TYPING);
            case PASTE -> playSound(Sounds.KEYBOARD_PASTE, SoundType.TYPING);
        }
    }

	public static float getSoundVolume(SoundType type, boolean... anti) {
		float volume = getSoundVolume(type.category);
		if (anti != null && anti.length > 0 && anti[0] && volume == 1f) {
			return 0f;
		}
		return volume * type.pitch;
	}

    public static float getSoundVolume(Mixers category, boolean... anti) {
        float volume = VolumeConfig.getVolume(category);
		if (category == Mixers.MASTER)
			volume *= 2f;
        if (anti != null && anti.length > 0 && anti[0] && volume == 1f) {
            return 0f;
        }
        return volume;
    }

    //? if <1.19.3 {
    /*public static float getSoundVolume(SoundSource source, boolean... anti) {
        final float volume = Minecraft.getInstance().options.getSoundSourceVolume(source);
        if (anti != null && anti.length > 0 && anti[0] && volume == 1f) {
            return 0f;
        }
        return volume;
    }
    *///?}
}
