package dev.arbor.extrasoundsnext.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.sounds.Sound;
//? if >=1.19 {
import net.minecraft.util.RandomSource;
//?} else {
/*import java.util.Random;
*///?}
//? if >=1.19.3 {
import net.minecraft.util.valueproviders.ConstantFloat;
//?}

import java.lang.reflect.Type;

public class SoundSerializer implements JsonSerializer<Sound>
{
    //? if >=1.19 {
    RandomSource r = RandomSource.create();
    //?} else {
    /*Random r = new java.util.Random();
    *///?}

    @Override
    public JsonElement serialize(Sound src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject o = new JsonObject();
        o.addProperty("name", src.getLocation().toString());
        //? if >=1.19.3 {
        if (src.getVolume() instanceof ConstantFloat volumeFloat && volumeFloat.getValue() != 1)
            o.addProperty("volume", src.getVolume().sample(r));
        if (src.getPitch() instanceof ConstantFloat pitchFloat && pitchFloat.getValue() != 1)
            o.addProperty("pitch", src.getPitch().sample(r));
        //?} elif >=1.19 {
        /*float volume = src.getVolume().sample(r);
        if (volume != 1)
            o.addProperty("volume", volume);
        float pitch = src.getPitch().sample(r);
        if (pitch != 1)
            o.addProperty("pitch", pitch);
        *///?} else {
        /*float volume = src.getVolume();
        if (volume != 1)
            o.addProperty("volume", volume);
        float pitch = src.getPitch();
        if (pitch != 1)
            o.addProperty("pitch", pitch);
        *///?}
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
