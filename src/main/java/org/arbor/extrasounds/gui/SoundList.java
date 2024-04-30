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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.arbor.extrasounds.ExtraSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoundList extends ContainerObjectSelectionList<SoundList.SoundEntry> {
    public SoundList(Minecraft minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public void addOption(Options o, Option w) {
        super.addEntry(SoundEntry.createOption(o, w, this.width));
    }

    public void addCategory(SoundSource soundSource) {
        this.addEntry(SoundEntry.create(this.minecraft, this.width, soundSource));
    }

    public void addDoubleCategory(SoundSource first, @Nullable SoundSource second) {
        this.addEntry(SoundEntry.createDouble(this.minecraft, this.width, first, second));
    }

    public void addAllCategory(SoundSource[] options) {
        for (int i = 0; i < options.length; i += 2) {
            this.addDoubleCategory(options[i], i < options.length - 1 ? options[i + 1] : null);
        }
    }

    public void addGroup(SoundSource group, Button.OnPress pressAction) {
        super.addEntry(SoundEntry.createGroup(group, this.width, pressAction));
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 32;
    }

    @OnlyIn(Dist.CLIENT)
    protected static class SoundEntry extends Entry<SoundEntry> {
        List<? extends AbstractWidget> widgets;

        public SoundEntry(List<? extends AbstractWidget> w) {
            widgets = w;
        }

        public static SoundEntry create(Minecraft minecraft, int width, SoundSource soundSource) {
            return new SoundEntry(List.of(new VolumeSlider(minecraft, width / 2 - 155, 0, soundSource, 310)));
        }

        public static SoundEntry createDouble(Minecraft minecraft, int width, SoundSource first, @Nullable SoundSource second) {
            List<AbstractWidget> widgets = new ArrayList<>();
            addVolumeSlider(minecraft, width, widgets, 0, first);
            if (second != null) {
                addVolumeSlider(minecraft, width, widgets, 160, second);
            }
            return new SoundEntry(widgets);
        }

        public static void addVolumeSlider(Minecraft minecraft, int width, List<AbstractWidget> widgets, int offset, SoundSource soundSource) {
            var toggleable = ExtraSounds.TOGGLEABLE_CATS.get(soundSource);
            if (toggleable != null && toggleable.getFirst()) {
                widgets.add(new VolumeButton(minecraft, width / 2 - 155 + offset, 0, soundSource, 150));
                return;
            }
            widgets.add(new VolumeSlider(minecraft, width / 2 - 155 + offset, 0, soundSource, 150));
        }

        public static SoundEntry createGroup(SoundSource group, int width, Button.OnPress pressAction) {
            return new SoundEntry(
                    List.of(new VolumeSlider(Minecraft.getInstance(), width / 2 - 155, 0, group, 285),
                            new ImageButton(width / 2 + 135, 0, 20, 20, 0, 0, 20,
                                    ExtraSounds.SETTINGS_ICON, 20, 40, pressAction)
                    ));
        }

        public static SoundEntry createOption(Options o, Option w, int width) {
            var b = w.createButton(o, width / 2 - 155, 0, 310);
            return new SoundEntry(List.of(b));
        }

        public @NotNull List<? extends GuiEventListener> children() {
            return this.widgets;
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return this.widgets;
        }

        @Override
        public void render(@NotNull PoseStack context, int index, int y, int x, int entryWidth,
                           int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.widgets.forEach((s) -> {
                s.y = y;
                s.render(context, mouseX, mouseY, tickDelta);
            });
        }
    }
}

