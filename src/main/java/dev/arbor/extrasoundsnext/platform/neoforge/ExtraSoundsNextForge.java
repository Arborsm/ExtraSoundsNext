//? if neoforge {
package dev.arbor.extrasoundsnext.platform.neoforge;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.gui.VolumeScreen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(ExtraSoundsNext.MODID)
public final class ExtraSoundsNextForge {
    public ExtraSoundsNextForge() {
        ExtraSoundsNext.init();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, parent) -> new VolumeScreen(parent)
        );
    }
}
//?} elif forge && >=1.19 {
/*package dev.arbor.extrasoundsnext.platform.neoforge;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.gui.VolumeScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ExtraSoundsNext.MODID)
public final class ExtraSoundsNextForge {
    @SuppressWarnings("removal")
	public ExtraSoundsNextForge() {
        ExtraSoundsNext.init();
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, parent) -> new VolumeScreen(parent)
                )
        );
    }
}
*///?} elif forge {
/*package dev.arbor.extrasoundsnext.platform.neoforge;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.mapping.SoundPackLoader;
import net.minecraftforge.fml.common.Mod;

@Mod(ExtraSoundsNext.MODID)
public final class ExtraSoundsNextForge {
    public ExtraSoundsNextForge() {
        ExtraSoundsNext.init();
        SoundPackLoader.init();
    }
}
*///?} else {
//?}
