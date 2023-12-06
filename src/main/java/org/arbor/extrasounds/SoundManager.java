package org.arbor.extrasounds;

import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static long lastPlayed = System.currentTimeMillis();

    public static void playSound(ItemStack stack, SoundType type)
    {
        var itemId = Registry.ITEM.getKey(stack.getItem());
        String idString = ExtraSounds.getClickId(itemId, type);
        if (!ResourceLocation.isValidResourceLocation(idString))
        {
            LOGGER.error("Unable to parse sound from ID: " + idString);
            return;
        }

        ResourceLocation id = ResourceLocation.tryParse(idString);
        Registry.SOUND_EVENT.getOptional(id).ifPresentOrElse(
                (snd) -> playSound(snd, type),
                () -> LOGGER.error("Sound cannot be found in registry: " + id));
    }

    public static void playSound(MobEffect effect, boolean add)
    {
        DebugUtils.effectLog(effect, add);

        SoundEvent e = add ?
                switch (effect.getCategory())
                        {
                            case HARMFUL -> Sounds.EFFECT_ADD_NEGATIVE;
                            case NEUTRAL, BENEFICIAL -> Sounds.EFFECT_ADD_POSITIVE;
                        }
                :
                switch (effect.getCategory())
                        {
                            case HARMFUL -> Sounds.EFFECT_REMOVE_NEGATIVE;
                            case NEUTRAL, BENEFICIAL -> Sounds.EFFECT_REMOVE_POSITIVE;
                        };
        playSound(e, SoundType.EFFECT);
    }

    public static void playSound(SoundEvent snd, SoundType type)
    {
        playSound(snd, type, type.category);
    }

    public static void playSound(SoundEvent snd, SoundType type, SoundSource cat)
    {
        playSound(snd, type.pitch, cat);
    }

    public static void playSound(SoundEvent snd, float pitch, SoundSource cat)
    {
        playSound(new SimpleSoundInstance(snd.getLocation(), cat, getMasterVol(), pitch, ExtraSounds.mcRandom,
                                              false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D,
                                              true));
        DebugUtils.soundLog(snd);
    }

    public static void playSound(SoundEvent snd, SoundType type, BlockPos position)
    {
        playSound(new SimpleSoundInstance(snd, type.category, getMasterVol(), type.pitch,
                                              ExtraSounds.mcRandom,
                                              position.getX() + 0.5,
                                              position.getY() + 0.5,
                                              position.getZ() + 0.5));
        DebugUtils.soundLog(snd);
    }

    public static void playSound(SimpleSoundInstance instance)
    {
        throttle(() -> {
            var client = Minecraft.getInstance();
            client.tell(() -> {
                client.getSoundManager().play(instance);
            });
        });
    }

    public static void stopSound(SoundEvent e, SoundType type)
    {
        Minecraft.getInstance().getSoundManager().stop(e.getLocation(), type.category);
    }

    private static void throttle(Runnable r)
    {
        try
        {
            long now = System.currentTimeMillis();
            if (now - lastPlayed > 5) r.run();
            lastPlayed = now;
        }
        catch (Exception e)
        {
            System.err.println("Failed to play sound:");
            e.printStackTrace();
        }
    }

    private static float getMasterVol()
    {
        return Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MASTER);
    }
}
