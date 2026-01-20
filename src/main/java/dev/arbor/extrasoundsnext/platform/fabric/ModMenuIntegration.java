// ModMenu integration is disabled due to optional dependency issues
// To enable, add ModMenu as a compileOnly dependency in build.gradle
/*
//? if fabric {
package dev.arbor.extrasoundsnext.platform.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.arbor.extrasoundsnext.gui.VolumeScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return VolumeScreen::new;
    }
}
//?}
*/
