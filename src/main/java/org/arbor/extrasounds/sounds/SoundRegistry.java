package org.arbor.extrasounds.sounds;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.apache.logging.log4j.LogManager;

public class SoundRegistry
{
    static SoundEvent register(ResourceLocation id)
    {
        var e = new SoundEvent(id);
        try
        {
            Registry.register(Registry.SOUND_EVENT, id, e);
        }
        catch (IllegalStateException exception)
        {
            LogManager.getLogger()
                      .error("Failed to register SoundEvent - please report this on ExtraSounds' Github page!");
            exception.printStackTrace();
        }
        return e;
    }

    static SoundEvent register(String id)
    {
        return register(new ResourceLocation("extrasounds:" + id));
    }
}
