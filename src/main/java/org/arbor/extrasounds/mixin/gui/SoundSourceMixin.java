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

import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundSource;
import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.annotation.CategoryLoader;
import org.arbor.extrasounds.sounds.SoundSouceInit;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.*;

@Mixin(SoundSource.class)
public class SoundSourceMixin {
    // And you
    @Shadow(aliases = "$VALUES", remap = false)
    @Final
    @Mutable
    private static SoundSource[] $VALUES;
    @Unique
    private static final String INVALID_VAR_NAME_REGEX = "[^a-zA-Z0-9_$]";
    @Unique
    private static List<String> SUPPRESSED_NAMES;
    @Unique
    private static List<SoundSource> EDITING_CATS;
    @Unique
    private static Map<String, SoundSource> REGISTERED_VARIANTS;

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static SoundSource extra_sounds$newSoundCategory(String internalName, int order, String name) {
        throw new AssertionError();
    }

    /**
     * Tries to make the custom enum. The created variable can be accessed from specified field.<br>
     * When the name already exists, the reference is created to match it.
     *
     * @param field    The referer.
     * @param instance The instance of an Object that has <code>field</code>.
     * @param name     The name trying to register.
     * @throws IllegalAccessException Thrown when cannot access to the <code>field</code>.
     */
    @Unique
    private static void extra_sounds$tryMakeVariant(Field field, Object instance, String name) throws IllegalAccessException {
        final String varName = name.toUpperCase(Locale.ROOT);
        final String displayName = name.toLowerCase(Locale.ROOT);
        final SoundSource newCategory;

        // Check duplicated name.
        if (REGISTERED_VARIANTS.containsKey(displayName)) {
            if (!SUPPRESSED_NAMES.contains(displayName)) {
                ExtraSounds.LOGGER.error(
                        "[%s] Duplicate enum name was found: '%s'.".formatted(ExtraSounds.class.getSimpleName(), displayName),
                        new RuntimeException("%s is already registered".formatted(displayName)));
                SUPPRESSED_NAMES.add(displayName);
            }
            newCategory = REGISTERED_VARIANTS.get(displayName);
        } else {
            newCategory = extra_sounds$newSoundCategory(varName, EDITING_CATS.getLast().ordinal() + 1, displayName);
        }

        field.set(instance, newCategory);
        if (!EDITING_CATS.contains(newCategory)) {
            EDITING_CATS.add(newCategory);
            REGISTERED_VARIANTS.put(displayName, newCategory);
        }
    }

    /**
     * Adds customized enum variant of {@link SoundSource}.
     */
    @Inject(method = "<clinit>", at = @At(value = "FIELD",
            opcode = Opcodes.PUTSTATIC,
            target = "Lnet/minecraft/sounds/SoundSource;$VALUES:[Lnet/minecraft/sounds/SoundSource;",
            shift = At.Shift.AFTER))
    private static void extra_sounds$addCustomVariants(CallbackInfo ci) {
        REGISTERED_VARIANTS = new HashMap<>();
        SUPPRESSED_NAMES = new ArrayList<>();
        EDITING_CATS = new ArrayList<>(Arrays.asList($VALUES));
        for (SoundSource category : EDITING_CATS) {
            REGISTERED_VARIANTS.put(category.getName(), category);
        }

        final Pair<CategoryLoader, List<Field>> categories = SoundSouceInit.getCategories();
        final CategoryLoader categoryLoader = categories.getFirst();
        categories.getSecond().forEach(field -> {
            if (!field.getType().equals(SoundSource.class)) {
                return;
            }

            final CategoryLoader.Register annotation = field.getAnnotation(CategoryLoader.Register.class);
            final String id = annotation.id().isEmpty() ? field.getName() : annotation.id();
            final String varName = "%s$%s".formatted(ExtraSounds.MODID, id).replaceAll(INVALID_VAR_NAME_REGEX, "_");
            try {
                extra_sounds$tryMakeVariant(field, categoryLoader, varName);
            } catch (Throwable ex) {
                ExtraSounds.LOGGER.error(
                        "[%s] Failed to register SoundCategory with ID '%s'".formatted(ExtraSounds.class.getSimpleName(), varName), ex);
            }
        });

        // Set the new enums.
        $VALUES = EDITING_CATS.toArray(SoundSource[]::new);

        // Cleanup.
        EDITING_CATS.clear();
        SUPPRESSED_NAMES.clear();
        REGISTERED_VARIANTS.clear();
    }
}
