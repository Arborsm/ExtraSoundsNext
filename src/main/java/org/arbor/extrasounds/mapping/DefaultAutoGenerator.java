package org.arbor.extrasounds.mapping;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SugarCaneBlock;
import org.arbor.extrasounds.annotation.SoundsGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.world.item.ArmorMaterials.*;
import static org.arbor.extrasounds.sounds.Categories.*;
import static org.arbor.extrasounds.sounds.Sounds.*;

public final class DefaultAutoGenerator {
    @SoundsGenerator
    public final static SoundGenerator generator = SoundGenerator.of(ResourceLocation.DEFAULT_NAMESPACE, DefaultAutoGenerator::autoGenerator);

    private static final List<Holder<ArmorMaterial>> armorMaterials = List.of(LEATHER, CHAIN, IRON, GOLD, DIAMOND, TURTLE, NETHERITE, ARMADILLO);

    public static SoundDefinition autoGenerator(Item item) {
        String itemId = BuiltInRegistries.ITEM.getKey(item).getPath();
        if (itemId.contains("music_disc")) {
            return SoundDefinition.of(aliased(MUSIC_DISC));
        } else if (item instanceof BoatItem) {
            return SoundDefinition.of(aliased(BOAT));
        } else if (item instanceof TieredItem toolItem) {
            if (toolItem.getTier() instanceof Tiers mat) {
                return getTierItemSound(mat);
            }
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (item instanceof ArmorItem armorItem) {
            if (armorMaterials.contains(armorItem.getMaterial())) {
                return getArmorMaterialSound(armorItem.getMaterial());
            }
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (item instanceof ShieldItem) {
            return SoundDefinition.of(aliased(Gear.IRON));
        } else if (item instanceof BucketItem bucketItem) {
            return SoundGenerator.getBucketItemSound(bucketItem);
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
        } else if (item == Items.BOWL || item instanceof SuspiciousStewItem) {
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
            ResourceLocation blockSound = SoundGenerator.getSoundType(block).getPlaceSound().getLocation();

            if (block instanceof BaseRailBlock) {
                return SoundDefinition.of(aliased(RAIL));
            } else if (block instanceof BannerBlock) {
                return SoundDefinition.of(aliased(BANNER));
            } else if (block instanceof SeaPickleBlock) {
                return SoundDefinition.of(event(blockSound, 0.4f));
            } else if (block instanceof LeavesBlock || block instanceof BushBlock || block instanceof SugarCaneBlock) {
                ResourceLocation soundId = SoundGenerator.getSoundType(block).getPlaceSound().getLocation();
                if (soundId.getPath().equals("block.grass.place")) {
                    return SoundDefinition.of(aliased(LEAVES));
                } else {
                    return SoundDefinition.of(event(soundId));
                }
            } else if (block instanceof RotatedPillarBlock pillarBlock && SoundGenerator.getSoundType(pillarBlock).equals(SoundType.FROGLIGHT)) {
                return SoundDefinition.of(event(blockSound, 0.3f));
            }

            return SoundDefinition.of(event(blockSound));
        }

        return SoundDefinition.of(aliased(ITEM_PICK));
    }

    @SuppressWarnings("all")
    @NotNull
    private static SoundDefinition getArmorMaterialSound(Holder<ArmorMaterial> mat) {
        if (mat == LEATHER){
            return SoundDefinition.of(aliased(Gear.LEATHER));
        }
        if (mat == CHAIN){
            return SoundDefinition.of(aliased(Gear.CHAIN));
        }
        if (mat == IRON){
            return SoundDefinition.of(aliased(Gear.IRON));
        }
        if (mat == GOLD){
            return SoundDefinition.of(aliased(Gear.GOLDEN));
        }
        if (mat == DIAMOND){
            return SoundDefinition.of(aliased(Gear.DIAMOND));
        }
        if (mat == TURTLE){
            return SoundDefinition.of(aliased(Gear.TURTLE));
        }
        if (mat == NETHERITE){
            return SoundDefinition.of(aliased(Gear.NETHERITE));
        }
        return SoundDefinition.of(aliased(Gear.GENERIC));
    }

    @SuppressWarnings("all")
    @NotNull
    private static SoundDefinition getTierItemSound(Tiers mat) {
        return switch (mat) {
            case WOOD -> SoundDefinition.of(aliased(Gear.WOOD));
            case STONE -> SoundDefinition.of(aliased(Gear.STONE));
            case IRON -> SoundDefinition.of(aliased(Gear.IRON));
            case GOLD -> SoundDefinition.of(aliased(Gear.GOLDEN));
            case DIAMOND -> SoundDefinition.of(aliased(Gear.DIAMOND));
            case NETHERITE -> SoundDefinition.of(aliased(Gear.NETHERITE));
            default -> SoundDefinition.of(aliased(Gear.GENERIC));
        };
    }

    private static boolean isGearGoldenItem(Item item) {
        return item instanceof AnimalArmorItem || item instanceof CompassItem ||
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
        return item == Items.BRICK || SoundGenerator.getDescriptionId(item).endsWith("pottery_sherd");
    }
}
