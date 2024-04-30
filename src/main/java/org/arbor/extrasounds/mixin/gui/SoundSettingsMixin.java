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

package org.arbor.extrasounds.mixin.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import org.arbor.extrasounds.gui.CustomSoundOptionsScreen;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OptionsScreen.class)
public class SoundSettingsMixin {
    @Shadow
    private @Final Options options;

    @Dynamic
    @Redirect(
            method = {"lambda$init$3", "m_96273_ (Lnet/minecraft/client/gui/components/Button;)V"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"),
            require = 0
    )
    private void redirectToCustomScreen(Minecraft instance, Screen screen) {
        instance.setScreen(new CustomSoundOptionsScreen(OptionsScreen.class.cast(this), options));
    }
}
