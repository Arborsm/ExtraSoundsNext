//? if neoforge || forge && >=1.19 {
package dev.arbor.extrasoundsnext.platform.neoforge;

import dev.arbor.extrasoundsnext.ExtraSoundsNext;
import dev.arbor.extrasoundsnext.mapping.SoundPackLoader;
//? if >=1.19.3 {
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
//?} else {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
*///?}

//? if neoforge {
@EventBusSubscriber(value = Dist.CLIENT)
//?} else {
/*@Mod.EventBusSubscriber(modid = ExtraSoundsNext.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
*///?}
public class SoundPackLoaderForge {
    @SubscribeEvent
    static void onRegisterEvent(RegisterEvent event) {
        //? if >=1.19.3 {
        if (!event.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE))
            return;
        //?} else {
		/*if (!event.getRegistryKey().equals(Registry.LOOT_ITEM_REGISTRY))
			return;
        *///?}
        //? if neoforge && >=26.1 {
        /*// 26.x binds item components only after the initial async resource reload, which happens
        // after this RegisterEvent; defer the cache generation to the client tick on the game bus.
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
                (net.neoforged.neoforge.client.event.ClientTickEvent.Post tickEvent) ->
                        SoundPackLoader.initWhenReady());
        *///?} else {
        SoundPackLoader.init();
        //?}
    }
}
//?}
