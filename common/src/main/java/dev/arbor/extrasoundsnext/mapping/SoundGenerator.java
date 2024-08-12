package dev.arbor.extrasoundsnext.mapping;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Fluid;
import dev.arbor.extrasoundsnext.debug.DebugUtils;
import dev.arbor.extrasoundsnext.mixin.misc.BucketFluidAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static dev.arbor.extrasoundsnext.sounds.Categories.METAL;
import static dev.arbor.extrasoundsnext.sounds.Sounds.aliased;
import static dev.arbor.extrasoundsnext.sounds.Sounds.event;


public final class SoundGenerator {
    public final String namespace;
    public final Function<Item, SoundDefinition> itemSoundGenerator;

    private SoundGenerator(String namespace, Function<Item, SoundDefinition> itemSoundGenerator) {
        this.namespace = namespace;
        this.itemSoundGenerator = itemSoundGenerator;
    }

    /**
     * Tells the sounds of your items and/or blocks to ExtraSounds.
     *
     * @param namespace          The item namespace your mod uses.
     * @param itemSoundGenerator The instance of the {@link Function} that converts from {@link Item} to {@link SoundDefinition}.
     * @see DefaultAutoGenerator#generator
     */
    public static SoundGenerator of(@NotNull String namespace, @NotNull Function<Item, SoundDefinition> itemSoundGenerator) {
        return new SoundGenerator(namespace, itemSoundGenerator);
    }

    public static String getDescriptionId(Item item) {
        String id = "";
        try {
            id = item.getDescriptionId();
        } catch (NullPointerException ignored) {
        }
        return id;
    }

    public static SoundDefinition getBucketItemSound(BucketItem bucketItem) {
        SoundEventRegistration soundEntry;
        try {
            final Fluid fluid = ((BucketFluidAccessor) bucketItem).getContent();
            soundEntry = fluid.getPickupSound().map(sound -> event(sound.getLocation(), 0.4f)).orElse(aliased(METAL));
        } catch (NullPointerException ignored) {
            soundEntry = aliased(METAL);
        }
        return SoundDefinition.of(soundEntry);
    }

    public static SoundType getSoundType(Block block) {
        try {
            return block.getSoundType(block.defaultBlockState());
        } catch (Throwable e) {
            if (DebugUtils.DEBUG) {
                ExtraSoundsNext.LOGGER.error("Failed to get sound type for block {}", block, e);
            }
            return SoundType.STONE;
        }
    }
}
