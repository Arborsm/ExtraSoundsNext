package dev.arbor.extrasoundsnext.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BucketItem.class)
@Environment(EnvType.CLIENT)
public interface BucketFluidAccessor {
    @Accessor
    Fluid getContent();
}
