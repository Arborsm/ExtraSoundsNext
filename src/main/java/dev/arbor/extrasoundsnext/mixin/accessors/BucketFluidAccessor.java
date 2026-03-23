package dev.arbor.extrasoundsnext.mixin.accessors;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BucketItem.class)
@MixinEnvironment()
public interface BucketFluidAccessor {
    @Accessor
    Fluid getContent();
}
