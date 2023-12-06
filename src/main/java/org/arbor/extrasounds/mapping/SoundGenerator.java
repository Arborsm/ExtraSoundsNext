package org.arbor.extrasounds.mapping;

import java.util.function.Function;
import net.minecraft.world.item.Item;

public record SoundGenerator(String namespace, String modId,
                             Function<Item, SoundDefinition> itemSoundGenerator)
{
    public static SoundGenerator of(String namespace, String modId, Function<Item, SoundDefinition> itemSoundGenerator)
    {
        return new SoundGenerator(namespace, modId, itemSoundGenerator);
    }
}
