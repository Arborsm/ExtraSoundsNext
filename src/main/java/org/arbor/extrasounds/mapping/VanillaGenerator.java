package org.arbor.extrasounds.mapping;

import org.arbor.extrasounds.mixin.BucketFluidAccessor;
import org.arbor.extrasounds.sounds.Categories.Gear;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DiscFragmentItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.EmptyMapItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.FoodOnAStickItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFrameItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.KnowledgeBookItem;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.material.Fluid;

import static org.arbor.extrasounds.sounds.Categories.*;
import static org.arbor.extrasounds.sounds.Sounds.*;

public final class VanillaGenerator {
    private static boolean isGearGoldenItem(Item item) {
        return item instanceof HorseArmorItem || item instanceof CompassItem ||
                item instanceof SpyglassItem || item instanceof ShearsItem;
    }
    private static boolean isGearLeatherItem(Item item) {
        return item instanceof LeadItem || item instanceof ElytraItem || item instanceof SaddleItem;
    }
    private static boolean isGearGenericItem(Item item) {
        return item instanceof BowItem || item instanceof CrossbowItem || item instanceof FishingRodItem ||
                item instanceof FoodOnAStickItem;
    }
    private static boolean isPaperItem(Item item) {
        return item instanceof BannerPatternItem || item instanceof BookItem || item instanceof WritableBookItem ||
                item instanceof WrittenBookItem || item instanceof EnchantedBookItem || item instanceof EmptyMapItem ||
                item instanceof MapItem || item instanceof NameTagItem || item instanceof KnowledgeBookItem;
    }
    private static boolean isBrickItem(Item item) {
        return item == Items.BRICK || item.getDescriptionId().endsWith("pottery_sherd");
    }

    public static SoundGenerator generator = SoundGenerator.of(ResourceLocation.DEFAULT_NAMESPACE, item -> {
        if (item instanceof RecordItem) {
            return SoundDefinition.of(aliased(MUSIC_DISC));
        } else if (item instanceof BoatItem) {
            return SoundDefinition.of(aliased(BOAT));
        } else if (item instanceof TieredItem toolItem) {
            if (toolItem.getTier() instanceof Tiers mat) {
                return switch (mat) {
                    case WOOD -> SoundDefinition.of(aliased(Gear.WOOD));
                    case STONE -> SoundDefinition.of(aliased(Gear.STONE));
                    case IRON -> SoundDefinition.of(aliased(Gear.IRON));
                    case GOLD -> SoundDefinition.of(aliased(Gear.GOLDEN));
                    case DIAMOND -> SoundDefinition.of(aliased(Gear.DIAMOND));
                    case NETHERITE -> SoundDefinition.of(aliased(Gear.NETHERITE));
                    default -> SoundDefinition.of(aliased(Gear.GENERIC));
                    //⬆ even though not required, this is in case any mods add to the enum of materials
                };
            }
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (item instanceof ArmorItem armorItem) {
            if (armorItem.getMaterial() instanceof ArmorMaterials mat) {
                return switch (mat) {
                    case IRON -> SoundDefinition.of(aliased(Gear.IRON));
                    case GOLD -> SoundDefinition.of(aliased(Gear.GOLDEN));
                    case DIAMOND -> SoundDefinition.of(aliased(Gear.DIAMOND));
                    case NETHERITE -> SoundDefinition.of(aliased(Gear.NETHERITE));
                    case CHAIN -> SoundDefinition.of(aliased(Gear.CHAIN));
                    case TURTLE -> SoundDefinition.of(aliased(Gear.TURTLE));
                    case LEATHER -> SoundDefinition.of(aliased(Gear.LEATHER));
                    default -> SoundDefinition.of(aliased(Gear.GENERIC));
                };
            }
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (item instanceof ShieldItem) {
            return SoundDefinition.of(aliased(Gear.IRON));
        } else if (item instanceof BucketItem bucketItem) {
            final Fluid fluid = ((BucketFluidAccessor) bucketItem).getContent();
            final SoundEventRegistration soundEntry = fluid.getPickupSound().map(sound -> event(sound.getLocation(), 0.4f)).orElse(aliased(METAL));
            return SoundDefinition.of(soundEntry);
        } else if (item instanceof MinecartItem) {
            return SoundDefinition.of(aliased(MINECART));
        } else if (item instanceof ItemFrameItem) {
            return SoundDefinition.of(aliased(FRAME));
        } else if (item instanceof PotionItem || item instanceof ExperienceBottleItem) {
            return SoundDefinition.of(aliased(POTION));
        } else if (item instanceof ArrowItem) {
            return SoundDefinition.of(aliased(ARROW));
        } else if (item instanceof DyeItem) {
            return SoundDefinition.of(aliased(DUST));
        } else if (item instanceof SpawnEggItem) {
            return SoundDefinition.of(aliased(WET_SLIPPERY));
        } else if (item instanceof BowlFoodItem || item instanceof SuspiciousStewItem) {
            return SoundDefinition.of(aliased(BOWL));
        } else if (item instanceof InstrumentItem) {
            return SoundDefinition.of(single(LOOSE_METAL.getLocation(), 0.6f, 0.9f, Sound.Type.SOUND_EVENT));
        } else if (item instanceof SmithingTemplateItem) {
            return SoundDefinition.of(aliased(LOOSE_METAL));
        } else if (item instanceof DiscFragmentItem) {
            return SoundDefinition.of(single(METAL_BITS.getLocation(), 0.7f, 0.85f, Sound.Type.SOUND_EVENT));
        } else if (isBrickItem(item)) {
            return SoundDefinition.of(aliased(BRICK));
        } else if (isGearGoldenItem(item)) {
            return SoundDefinition.of(aliased(Gear.GOLDEN));
        } else if (isGearLeatherItem(item)) {
            return SoundDefinition.of(aliased(Gear.LEATHER));
        } else if (isGearGenericItem(item)) {
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (isPaperItem(item)) {
            return SoundDefinition.of(aliased(PAPER));
        } else if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            ResourceLocation blockSound = block.getSoundType(block.defaultBlockState()).getPlaceSound().getLocation();

            if (block instanceof BaseRailBlock) {
                return SoundDefinition.of(aliased(RAIL));
            } else if (block instanceof BannerBlock) {
                return SoundDefinition.of(aliased(BANNER));
            } else if (block instanceof SeaPickleBlock) {
                return SoundDefinition.of(event(blockSound, 0.4f));
            } else if (block instanceof LeavesBlock || block instanceof BushBlock || block instanceof SugarCaneBlock) {
                ResourceLocation soundId = block.getSoundType(block.defaultBlockState()).getPlaceSound().getLocation();
                if (soundId.getPath().equals("block.grass.place")) {
                    return SoundDefinition.of(aliased(LEAVES));
                } else {
                    return SoundDefinition.of(event(soundId));
                }
            } else if (block instanceof RotatedPillarBlock pillarBlock && pillarBlock.getSoundType(pillarBlock.defaultBlockState()).equals(SoundType.FROGLIGHT)) {
                return SoundDefinition.of(event(blockSound, 0.3f));
            }

            return SoundDefinition.of(event(blockSound));
        }

        return SoundDefinition.of(aliased(ITEM_PICK));
    });
}
