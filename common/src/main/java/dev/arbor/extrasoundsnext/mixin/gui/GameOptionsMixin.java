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

package dev.arbor.extrasoundsnext.mixin.gui;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.sounds.SoundSource;
import dev.arbor.extrasoundsnext.sounds.SoundSouceInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Options.class)
public abstract class GameOptionsMixin {
    @Unique
    private SoundSource extra_sounds$currentCategory = null;

    /**
     * Stores the current SoundCategory in loop.
     */
    @Inject(method = "createSoundSliderOptionInstance", at = @At("HEAD"))
    private void extra_sounds$storeCategory(String key, SoundSource soundCategory, CallbackInfoReturnable<OptionInstance<?>> cir) {
        this.extra_sounds$currentCategory = soundCategory;
    }

    /**
     * Modifies a Constant of the default sound volume that exists in {@link SoundSouceInit#DEFAULT_LEVELS} and matches {@link GameOptionsMixin#extra_sounds$currentCategory}.<br>
     * default value is 1.0.
     *
     * @see Options#Options
     */
    @ModifyConstant(method = "createSoundSliderOptionInstance", constant = @Constant(doubleValue = 1.0))
    private double extra_sounds$changeDefault(double value) {
        if (this.extra_sounds$currentCategory == null) {
            return value;
        }
        if (SoundSouceInit.DEFAULT_LEVELS.isEmpty()) {
            SoundSouceInit.initCategoryLoader();
        }
        return SoundSouceInit.DEFAULT_LEVELS.getOrDefault(this.extra_sounds$currentCategory, (float) value);
    }

    @Redirect(method = "createSoundSliderOptionInstance", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;noTooltip()Lnet/minecraft/client/OptionInstance$TooltipSupplier;"), require = 0)
    private OptionInstance.TooltipSupplier<?> extra_sounds$modifyTooltip(String key, SoundSource category) {
        if (SoundSouceInit.TOOLTIPS.isEmpty()) {
            SoundSouceInit.initCategoryLoader();
        }
        if (SoundSouceInit.TOOLTIPS.containsKey(category)) {
            return value -> Tooltip.create(SoundSouceInit.TOOLTIPS.get(category));
        }
        return OptionInstance.noTooltip();
    }
}
