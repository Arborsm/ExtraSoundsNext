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

package dev.arbor.extrasoundsnext.gui;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class AbstractSoundListedScreen extends OptionsSubScreen {
    protected SoundList list;

    public AbstractSoundListedScreen(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    protected void addDoneButton() {
        addDoneButton(false);
    }

    protected void addDoneButton(boolean withCancel) {
        assert this.minecraft != null;
        if (withCancel) {
            this.addRenderableWidget(
                    Button.builder(CommonComponents.GUI_DONE, (button) -> {
                                this.minecraft.options.save();
                                this.minecraft.setScreen(this.lastScreen);
                            })
                            .bounds(this.width / 2 - 155, this.height - 27, 150, 20)
                            .build()
            );
            this.addRenderableWidget(
                    Button.builder(CommonComponents.GUI_CANCEL, (button) -> this.minecraft.setScreen(this.lastScreen))
                            .bounds(this.width / 2 - 155 + 160, this.height - 27, 150, 20)
                            .build()
            );
        } else {
            this.addRenderableWidget(
                    Button.builder(CommonComponents.GUI_DONE, (button) -> {
                                this.minecraft.options.save();
                                this.minecraft.setScreen(this.lastScreen);
                            })
                            .bounds(this.width / 2 - 100, this.height - 27, 200, 20)
                            .build()
            );
        }
    }
}
