package dev.arbor.extrasoundsnext.mapping;

import net.minecraft.client.resources.sounds.SoundEventRegistration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundDefinition
{
    public final SoundEventRegistration pickup;
    @Nullable
    public final SoundEventRegistration place;
    @Nullable
    public final SoundEventRegistration hotbar;

    private SoundDefinition(SoundEventRegistration sound)
    {
        this(sound, null, null);
    }

    private SoundDefinition(@NotNull SoundEventRegistration pickup, @Nullable SoundEventRegistration place, @Nullable SoundEventRegistration hotbar)
    {
        this.pickup = pickup;
        this.place = place;
        this.hotbar = hotbar;
    }

    public static SoundDefinition of(@NotNull SoundEventRegistration pickup, SoundEventRegistration place, SoundEventRegistration hotbar)
    {
        return new SoundDefinition(pickup, place, hotbar);
    }

    public static SoundDefinition of(@NotNull SoundEventRegistration sound)
    {
        return new SoundDefinition(sound);
    }
}
