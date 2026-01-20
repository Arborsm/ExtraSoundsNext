package dev.arbor.extrasoundsnext.api;

/**
 * Version adapter interface for handling version-specific code.
 */
public interface VersionAdapter {
    /**
     * Get current Minecraft version.
     */
    String getMinecraftVersion();

    /**
     * Check if current version matches given version.
     */
    boolean isVersion(String version);

    /**
     * Check if current version is at least given version.
     */
    boolean isVersionAtLeast(String version);
}
