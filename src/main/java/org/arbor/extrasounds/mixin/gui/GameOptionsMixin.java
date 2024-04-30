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

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.client.Options;
import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.ExtraSounds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.BiFunction;

@Mixin(Options.class)
public abstract class GameOptionsMixin {
    @Shadow
    @Final
    private Object2FloatMap<SoundSource> sourceVolumes;

    @Redirect(
            method = "processOptionsForge",
            at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;computeFloat(Ljava/lang/Object;Ljava/util/function/BiFunction;)F"),
            remap = false
    )
    private float removeComputeFloat(Object2FloatMap instance, Object key, BiFunction remappingFunction) {
        return 0;
    }

    @Inject(
            method = "processOptionsForge",
            at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;computeFloat(Ljava/lang/Object;Ljava/util/function/BiFunction;)F"),
            remap = false,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void computeFloat(Options.FieldAccess visitor, CallbackInfo ci, SoundSource[] var2, int var3, int var4, SoundSource soundCategory) {
        if (ExtraSounds.DEFAULT_LEVELS.isEmpty()) {
            ExtraSounds.initCategoryLoader();
        }
        sourceVolumes.computeFloat(soundCategory, (category, currentLevel) -> visitor.process("soundCategory_" + category.getName(),
                currentLevel != null ? currentLevel : ExtraSounds.DEFAULT_LEVELS.getOrDefault(category, 1.0f)));
    }

    @Inject(method = "load(Z)V", at = @At(value = "HEAD"), remap = false)
    private void preLoad(CallbackInfo ci) {
        if (ExtraSounds.DEFAULT_LEVELS.isEmpty()) {
            ExtraSounds.initCategoryLoader();
        }
        sourceVolumes.putAll(ExtraSounds.DEFAULT_LEVELS);
    }
}
