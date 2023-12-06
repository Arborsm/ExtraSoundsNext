package org.arbor.extrasounds.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.sounds.Sound;

import java.lang.reflect.Type;

public class SoundSerializer implements JsonSerializer<Sound>
{
    @Override
    public JsonElement serialize(Sound src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject o = new JsonObject();
        o.addProperty("name", src.getLocation().toString());
        if (src.getVolume() != 1)
            o.addProperty("volume", src.getVolume());
        if (src.getPitch() != 1)
            o.addProperty("pitch", src.getPitch());
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
