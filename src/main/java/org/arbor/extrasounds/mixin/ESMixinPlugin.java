package org.arbor.extrasounds.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ESMixinPlugin implements IMixinConfigPlugin {
    private static boolean isClassFound(String className) {
        try {
            Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
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
        if (mixinClassName.contains("org.arbor.extrasounds.mixin.emi")) {
            return isClassFound("dev.emi.emi.api.EmiPlugin");
        } else if (mixinClassName.contains("org.arbor.extrasounds.mixin.jei")) {
            return isClassFound("mezz.jei.api.JeiPlugin");
        } else if (mixinClassName.contains("org.arbor.extrasounds.mixin.rei")) {
            return isClassFound("me.shedaniel.rei.api.common.plugins.REIPlugin");
        }
        return true;
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
