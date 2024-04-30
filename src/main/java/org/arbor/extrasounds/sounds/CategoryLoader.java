package org.arbor.extrasounds.sounds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The enum modifier API that defines your unique {@link net.minecraft.sounds.SoundSource}.
 */
public interface CategoryLoader {
    /**
     * Registers a new {@link net.minecraft.sounds.SoundSource} and injects its reference to the field that has this annotation.<br>
     * The field name will be prefixed your modId and <code>$</code>, like following:<br>
     * <ul>
     *     <li>Declared by <code>mod-id</code><br>
     *     <pre>class CustomCats implements CategoryLoader {<br>    @Register<br>    public static SoundCategory MASTER;<br>}</pre></li>
     *     <li>Generated code at runtime<br>
     *     <pre>SoundCategory.MOD_ID$MASTER("mod_id$master");</pre></li>
     * </ul>
     * In this case, the translation key will be <code>"soundCategory.mod_id$master"</code>. You can access this SoundCategory using <code>CustomCats.MASTER</code> directly.
     *
     * @see Register#id
     * @see Register#master
     * @see Register#defaultLevel
     * @see Register#toggle
     * @see Register#tooltip
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Register {
        /**
         * The ID of the sound category - if omitted, will be automatically set from the field name.
         */
        String id() default "";

        /**
         * Sets the SoundCategory of a field to be the master category.<br>
         * It can be declared only once in your class and grouped within the master.<br>
         * To create multiple master categories, please create separate classes.
         */
        boolean master() default false;

        /**
         * Allows changing the default volume level if one has not yet been set.
         */
        float defaultLevel() default 1.0f;

        /**
         * Sets the SoundCategory as a toggle button.
         */
        boolean toggle() default false;

        /**
         * Sets the button state to be on by default.
         */
        boolean defaultOn() default true;

        /**
         * Sets tooltip to be displayed on mouse hover.<br>
         * This value will be passed to {@link net.minecraft.network.chat.TranslatableComponent}.
         */
        String tooltip() default "";
    }
}
