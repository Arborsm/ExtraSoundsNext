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

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.arbor.extrasounds.ExtraSounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoundList extends ContainerObjectSelectionList<SoundList.SoundEntry> {

    public SoundList(Minecraft minecraftClient, int i, int j, int k, int l, int m) {
        super(minecraftClient, i, j, k, l, m);
        this.centerListVertically = false;
    }

    public void addSingleOptionEntry(OptionInstance<?> option) {
        this.addEntry(SoundEntry.create(this.minecraft.options, this.width, option));
    }

    public void addOptionEntry(OptionInstance<?> firstOption, @Nullable OptionInstance<?> secondOption) {
        this.addEntry(SoundEntry.createDouble(this.minecraft.options, this.width, firstOption, secondOption));
    }

    public void addAll(OptionInstance<?>[] options) {
        for (int i = 0; i < options.length; i += 2) {
            this.addOptionEntry(options[i], i < options.length - 1 ? options[i + 1] : null);
        }
    }

    public void addCategory(SoundSource cat) {
        this.addSingleOptionEntry(this.createCustomizedOption(cat));
    }

    public void addDoubleCategory(SoundSource first, @Nullable SoundSource second) {
        this.addOptionEntry(this.createCustomizedOption(first),
                (second != null) ? this.createCustomizedOption(second) : null
        );
    }

    public void addAllCategory(SoundSource[] categories) {
        this.addAll(Arrays.stream(categories).map(this::createCustomizedOption).toArray(OptionInstance[]::new));
    }

    public void addGroup(SoundSource group, Button.OnPress pressAction) {
        super.addEntry(SoundEntry.createGroup(this.minecraft.options, this.createCustomizedOption(group), this.width, pressAction));
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 32;
    }

    private OptionInstance<?> createCustomizedOption(SoundSource category) {
        final OptionInstance<Double> simpleOption = this.minecraft.options.getSoundSourceOptionInstance(category);
        if (ExtraSounds.TOGGLEABLE_CATS.getOrDefault(category, Pair.of(false, false)).getFirst()) {
            return OptionInstance.createBoolean(simpleOption.toString(), value ->
                            Tooltip.create(ExtraSounds.TOOLTIPS.getOrDefault(category, CommonComponents.EMPTY)),
                    ExtraSounds.TOGGLEABLE_CATS.getOrDefault(category, Pair.of(false, false)).getSecond(), value -> simpleOption.set(value ? 1.0 : 0.0));
        }
        return simpleOption;
    }

    @OnlyIn(Dist.CLIENT)
    protected static class SoundEntry extends Entry<SoundEntry> {
        List<? extends AbstractWidget> widgets;

        public SoundEntry(List<? extends AbstractWidget> w) {
            widgets = w;
        }

        public static SoundEntry create(Options options, int width, OptionInstance<?> simpleOption) {
            return new SoundEntry(List.of(simpleOption.createButton(options, width / 2 - 155, 0, 310)));
        }

        public static SoundEntry createDouble(Options options, int width, OptionInstance<?> first, @Nullable OptionInstance<?> second) {
            List<AbstractWidget> widgets = new ArrayList<>();
            widgets.add(first.createButton(options, width / 2 - 155, 0, 150));
            if (second != null) {
                widgets.add(second.createButton(options, width / 2 + 5, 0, 150));
            }
            return new SoundEntry(widgets);
        }

        public static SoundEntry createGroup(Options options, OptionInstance<?> group, int width, Button.OnPress pressAction) {
            return new SoundEntry(
                    List.of(
                            group.createButton(options, width / 2 - 155, 0, 285),
                            new ImageButton(width / 2 + 135, 0, 20, 20, 0, 0, 20,
                                    ExtraSounds.SETTINGS_ICON, 20, 40, pressAction)
                    ));
        }

        public @NotNull List<? extends GuiEventListener> children() {
            return this.widgets;
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return this.widgets;
        }

        @Override
        public void render(@NotNull GuiGraphics context, int index, int y, int x, int entryWidth,
                           int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.widgets.forEach((s) -> {
                s.setY(y);
                s.render(context, mouseX, mouseY, tickDelta);
            });
        }
    }
}
