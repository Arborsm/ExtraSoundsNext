package org.arbor.extrasounds.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.util.RandomSource;

public class SoundSerializer implements JsonSerializer<Sound>
{
    RandomSource r = RandomSource.create();

    @Override
    public JsonElement serialize(Sound src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject o = new JsonObject();
        o.addProperty("name", src.getLocation().toString());
        if (src.getVolume().sample(r) != 1)
            o.addProperty("volume", src.getVolume().sample(r));
        if (src.getPitch().sample(r) != 1)
            o.addProperty("pitch", src.getPitch().sample(r));
        if (src.getWeight() != 1)
            o.addProperty("weight", src.getWeight());
        if (src.getType() != Sound.Type.FILE)
            o.addProperty("type", "event");
        if (src.shouldStream())
            o.addProperty("stream", src.shouldStream());
        if (src.shouldPreload())
            o.addProperty("preload", src.shouldPreload());
        if (src.getAttenuationDistance() != 16)
            o.addProperty("attenuation_distance", src.getAttenuationDistance());
        return o;
    }
}
