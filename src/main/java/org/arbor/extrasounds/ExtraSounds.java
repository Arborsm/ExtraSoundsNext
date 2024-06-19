/*
 * Copyright 2021 stashymane
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arbor.extrasounds;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.sounds.Mixers;
import org.arbor.extrasounds.sounds.SoundType;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Mod(ExtraSounds.MODID)
public class ExtraSounds {
    public static final String MODID = "extrasounds";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Mixers MIXERS = new Mixers();
    public static final ResourceLocation SETTINGS_ICON = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/settings.png");
    public static final SoundEvent MISSING = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "missing"));

    public ExtraSounds() {

    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            DebugUtils.init();
        }
    }

    @Nullable
    public static ResourceLocation getClickId(ResourceLocation id, SoundType type) {
        if (id == null || type == null) {
            return null;
        }
        return ResourceLocation.fromNamespaceAndPath(MODID, "%s.%s.%s".formatted(type.prefix, id.getNamespace(), id.getPath()));
    }

    public static SoundEvent createEvent(String path) {
        try {
            return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, path));
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSounds.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static SoundEvent createEvent(ResourceLocation path) {
        try {
            return SoundEvent.createVariableRangeEvent(path);
        } catch (Throwable ex) {
            LOGGER.error("[%s] Failed to create SoundEvent".formatted(ExtraSounds.class.getSimpleName()), ex);
        }
        return MISSING;
    }

    public static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(ExtraSounds.MODID, id);
    }

}
