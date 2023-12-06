package org.arbor.extrasounds.mapping;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.arbor.extrasounds.ExtraSounds;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SoundPackLoader {
    public static final Map<ResourceLocation, SoundEvent> CUSTOM_SOUND_EVENT = new HashMap<>();
    static Gson gson = new Gson();
    static InputStream is = SoundPackLoader.class.getResourceAsStream("/assets/arbor/sounds.json");
    static String json = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is))).lines().collect(Collectors.joining("\n"));
    static JsonObject sounds = gson.fromJson(json, JsonObject.class);
    public static void init() {
        sounds.keySet().forEach(key -> putSoundEvent(new ResourceLocation(ExtraSounds.MODID, key)));
    }

    /**
     * Registers the {@link SoundEvent} from specified {@link ResourceLocation}.
     *
     * @param clickId Target id.
     */
    private static void putSoundEvent(ResourceLocation clickId) {
        CUSTOM_SOUND_EVENT.put(clickId, new SoundEvent(clickId));
    }
}
