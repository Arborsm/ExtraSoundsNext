package dev.arbor.extrasoundsnext.api;

//? if neoforge {
import net.neoforged.fml.ModList;
//?}

import java.util.Optional;

public final class NeoForgePlatformHelper implements PlatformHelper {
    @Override
    public String getLoader() {
        return "NeoForge";
    }

    @Override
    public boolean isClient() {
        //? if neoforge {
        return net.neoforged.fml.loading.FMLLoader.getDist().isClient();
        //?} else {
        /*return true;
        *///?}
    }

    @Override
    public boolean isModLoaded(String modId) {
        //? if neoforge {
        return ModList.get().isLoaded(modId);
        //?} else {
        /*return false;
        *///?}
    }

    @Override
    public String getMinecraftVersion() {
        //? if neoforge {
        return ModList.get().getModContainerById("minecraft").getModInfo().getVersion().toString();
        //?} else {
        /*return "1.21.1";
        *///?}
    }

    @Override
    public String getModVersion() {
        //? if neoforge {
        return ModList.get().getModContainerById("extrasounds").getModInfo().getVersion().toString();
        //?} else {
        /*return "1.4";
        *///?}
    }
}
