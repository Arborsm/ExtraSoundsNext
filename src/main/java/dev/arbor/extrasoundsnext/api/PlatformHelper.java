package dev.arbor.extrasoundsnext.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
//? if neoforge {
/*import net.neoforged.fml.ModLoadingContext;
 *import net.neoforged.fml.loading.FMLLoader;
 *///?} forge {
/*import net.minecraftforge.fml.ModLoadingContext;
 *import net.minecraftforge.fml.loading.FMLLoader;
 *///?}

import java.util.Optional;

/**
 * Platform abstraction interface for multi-platform compatibility.
 */
public interface PlatformHelper {
    /**
     * Get the current platform/loader name.
     */
    String getLoader();

    /**
     * Check if running on client side.
     */
    boolean isClient();

    /**
     * Check if mod is loaded.
     */
    boolean isModLoaded(String modId);

    /**
     * Get current Minecraft version.
     */
    String getMinecraftVersion();

    /**
     * Get mod version.
     */
    String getModVersion();

    /**
     * Get platform-specific configuration.
     */
    default <T> Optional<T> getPlatformConfig(Class<T> configClass) {
        return Optional.empty();
    }
}
