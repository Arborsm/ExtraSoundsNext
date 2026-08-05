package dev.arbor.extrasoundsnext.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ESMixinPlugin implements IMixinConfigPlugin {
    private static boolean isModFound(String modId) {
        //? if fabric {
        /*return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded(modId);
        *///?} elif neoforge {
        return net.neoforged.fml.loading.LoadingModList.get().getModFileById(modId) != null;
        //?} elif forge {
        /*return net.minecraftforge.fml.loading.LoadingModList.get().getModFileById(modId) != null;
        *///?}
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (isIntegrationMixin(mixinClassName, "ae2")) {
            return isModFound("ae2");
        } else if (isIntegrationMixin(mixinClassName, "emi")) {
            return isModFound("emi");
        } else if (isIntegrationMixin(mixinClassName, "jei")) {
            return isModFound("jei");
        } else if (isIntegrationMixin(mixinClassName, "rei")) {
            return isModFound("roughlyenoughitems");
        }
        return true;
    }

    private static boolean isIntegrationMixin(String mixinClassName, String integration) {
        return mixinClassName.contains("dev.arbor.extrasoundsnext.mixin.integration." + integration) ||
                mixinClassName.contains("dev.arbor.extrasoundsnext.mixin." + integration);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
