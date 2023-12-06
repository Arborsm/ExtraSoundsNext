package org.arbor.extrasounds.mapping;

import javax.annotation.Nonnull;
import net.minecraft.client.resources.sounds.SoundEventRegistration;

public class SoundDefinition
{
    public SoundEventRegistration pickup;
    public SoundEventRegistration place = null;
    public SoundEventRegistration hotbar = null;

    public SoundDefinition(SoundEventRegistration sound)
    {
        this(sound, null, null);
    }

    public SoundDefinition(@Nonnull SoundEventRegistration pickup, SoundEventRegistration place, SoundEventRegistration hotbar)
    {
        this.pickup = pickup;
        this.place = place;
        this.hotbar = hotbar;
    }

    public static SoundDefinition of(@Nonnull SoundEventRegistration pickup, SoundEventRegistration place, SoundEventRegistration hotbar)
    {
        return new SoundDefinition(pickup, place, hotbar);
    }

    public static SoundDefinition of(@Nonnull SoundEventRegistration sound)
    {
        return new SoundDefinition(sound);
    }
}
