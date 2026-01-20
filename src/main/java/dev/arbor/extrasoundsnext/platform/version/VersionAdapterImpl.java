package dev.arbor.extrasoundsnext.api;

import net.fabricmc.loader.api.FabricLoader;
//? if neoforge {
/*import net.neoforged.fml.ModList;
 *///?}

public final class VersionAdapterImpl implements VersionAdapter {
    private static final String MINECRAFT_VERSION = getCurrentVersion();

    private static String getCurrentVersion() {
        //? if fabric {
        return FabricLoader.getInstance().getModContainer("minecraft").getMetadata().getVersion().getFriendlyString();
        //?} neoforge {
        /*return ModList.get().getModContainerById("minecraft").getModInfo().getVersion().toString();
        *///?} else {
        /*return "1.21.1";
        *///?}
    }

    @Override
    public String getMinecraftVersion() {
        return MINECRAFT_VERSION;
    }

    @Override
    public boolean isVersion(String version) {
        return MINECRAFT_VERSION.equals(version);
    }

    @Override
    public boolean isVersionAtLeast(String version) {
        return compareVersions(MINECRAFT_VERSION, version) >= 0;
    }

    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
            int n1 = Integer.parseInt(parts1[i]);
            int n2 = Integer.parseInt(parts2[i]);
            if (n1 != n2) {
                return n1 - n2;
            }
        }
        return parts1.length - parts2.length;
    }
}
