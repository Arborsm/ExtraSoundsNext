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

package org.arbor.extrasounds.gui;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.ExtraSounds;

import java.util.Arrays;

public class CustomSoundOptionsScreen extends AbstractSoundListedScreen {
    public CustomSoundOptionsScreen(Screen parent, Options options) {
        super(parent, options, Component.translatable("options.sounds.title"));
    }

    protected void init() {
        assert this.minecraft != null;
        this.list = new SoundList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        this.list.addCategory(SoundSource.MASTER);
        SoundSource[] cats = Arrays.stream(SoundSource.values()).filter(it -> !ExtraSounds.PARENTS.containsKey(it) &&
                !ExtraSounds.PARENTS.containsValue(it) &&
                it != SoundSource.MASTER).toArray(SoundSource[]::new);
        var count = cats.length;
        for (int i = 0; i < count; i += 2) {
            list.addDoubleCategory(cats[i], i + 1 < count ? cats[i + 1] : null);
            ExtraSounds.LOGGER.info("Added category " + cats[i].getName() + " and " + (i + 1 < count ? cats[i + 1].getName() : "null"));
        }
        ExtraSounds.LOGGER.info(ExtraSounds.PARENTS.toString());
        this.list.addSingleOptionEntry(options.soundDevice());
        this.list.addAll(new OptionInstance[]{options.showSubtitles(), options.directionalAudio()});

        for (String key : ExtraSounds.MASTER_CLASSES) {
            final SoundSource category = ExtraSounds.MASTERS.get(key);
            this.list.addGroup(category, button ->
                    this.minecraft.setScreen(new SoundGroupOptionsScreen(this, options, category)));
        }

        this.addWidget(this.list);

        this.addDoneButton();
    }
}
