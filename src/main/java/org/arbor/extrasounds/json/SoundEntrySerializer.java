package org.arbor.extrasounds.json;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Objects;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;

public class SoundEntrySerializer implements JsonSerializer<SoundEventRegistration>
{
    @Override
    public JsonElement serialize(SoundEventRegistration src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject o = new JsonObject();
        JsonArray sounds = new JsonArray();
        for (Sound snd : src.getSounds())
            sounds.add(context.serialize(snd));
        o.add("sounds", sounds);
        if (src.isReplace())
            o.addProperty("replace", src.isReplace());
        if (!Objects.equals(src.getSubtitle(), ""))
            o.addProperty("subtitle", src.getSubtitle());
        return o;
    }
}
