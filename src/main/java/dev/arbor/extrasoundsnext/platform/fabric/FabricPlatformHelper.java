package dev.arbor.extrasoundsnext.platform.fabric;

//? if fabric {
import dev.arbor.extrasoundsnext.api.PlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
//?} neoforge {
/*import net.neoforged.fml.ModLoadingContext;
 *import net.neoforged.fml.loading.FMLLoader;
 *///?} forge {
/*import net.minecraftforge.fml.ModLoadingContext;
 *import net.minecraftforge.fml.loading.FMLLoader;
 *///?}

import java.util.Optional;

public final class FabricPlatformHelper implements PlatformHelper {
    @Override
    public String getLoader() {
        return "Fabric";
    }

    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public String getMinecraftVersion() {
        return FabricLoader.getInstance().getModContainer("minecraft").getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public String getModVersion() {
        return FabricLoader.getInstance().getModContainer("extrasounds").getMetadata().getVersion().getFriendlyString();
    }
}
