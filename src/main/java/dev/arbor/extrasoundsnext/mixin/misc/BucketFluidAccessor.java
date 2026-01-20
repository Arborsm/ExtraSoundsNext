package dev.arbor.extrasoundsnext.mixin.misc;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? if fabric {
/*import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
*///?} elif neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
*///?} elif forge {
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//?}

//? if fabric {
/*@Environment(EnvType.CLIENT)
*///?} elif neoforge {
/*@OnlyIn(Dist.CLIENT)
*///?} elif forge {
@OnlyIn(Dist.CLIENT)
//?}
@Mixin(BucketItem.class)
@MixinEnvironment()
public interface BucketFluidAccessor {
    @Accessor
    Fluid getContent();
}
