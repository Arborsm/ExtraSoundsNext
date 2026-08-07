package dev.arbor.extrasoundsnext.mapping;

import dev.arbor.extrasoundsnext.annotation.ISoundsGenerator;
import net.minecraft.client.resources.sounds.Sound;
//? if >=26.1 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
*///?} else {
//? if >=1.21 {
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import static net.minecraft.world.item.ArmorMaterials.*;
//?} else {
/*import net.minecraft.core.Registry;
*///?}
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import dev.arbor.extrasoundsnext.annotation.SoundsGenerator;
import org.jetbrains.annotations.NotNull;

//? if >=1.21 {
import java.util.List;
//?}

import static dev.arbor.extrasoundsnext.sounds.Categories.*;
import static dev.arbor.extrasoundsnext.sounds.Sounds.*;

public final class DefaultAutoGenerator implements ISoundsGenerator {
    @SoundsGenerator
    public final static SoundGenerator generator = SoundGenerator.of(ResourceLocation.DEFAULT_NAMESPACE, DefaultAutoGenerator::autoGenerator);

    //? if >=1.21 && <26.1 {
    private static final List<Holder<ArmorMaterial>> armorMaterials = List.of(LEATHER, CHAIN, IRON, GOLD, DIAMOND, TURTLE, NETHERITE, ARMADILLO);
    //?}

    public static SoundDefinition autoGenerator(Item item) {
        //? if >=26.1 {
        /*String itemId = BuiltInRegistries.ITEM.getKey(item).getPath();
        if (itemId.contains("music_disc")) {
            return SoundDefinition.of(aliased(MUSIC_DISC));
        } else if (item instanceof BoatItem) {
            return SoundDefinition.of(aliased(BOAT));
        } else if (item.components().has(DataComponents.REPAIRABLE)) {
            // Covers tools, armor and 26.x weapons without a TOOL component (spears, mace).
            return getMaterialSound26(item);
        } else if (item.components().has(DataComponents.TOOL)) {
            return getToolItemSound26(item);
        } else if (isArmor26(item)) {
            return getArmorMaterialSound26(item);
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
        } else if (item.components().has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
            return SoundDefinition.of(aliased(BOWL));
        } else if (item instanceof InstrumentItem) {
            return SoundDefinition.of(single(LOOSE_METAL.location(), 0.6f, 0.9f, Sound.Type.SOUND_EVENT));
        } else if (item instanceof SmithingTemplateItem) {
            return SoundDefinition.of(aliased(LOOSE_METAL));
        } else if (item instanceof DiscFragmentItem) {
            return SoundDefinition.of(single(METAL_BITS.location(), 0.7f, 0.85f, Sound.Type.SOUND_EVENT));
        } else if (isBrickItem(item)) {
            return SoundDefinition.of(aliased(BRICK));
        } else if (item instanceof CompassItem || item instanceof SpyglassItem || item instanceof ShearsItem) {
            return SoundDefinition.of(aliased(Gear.GOLDEN));
        } else if (item instanceof LeadItem || item.components().has(DataComponents.GLIDER)) {
            return SoundDefinition.of(aliased(Gear.LEATHER));
        } else if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof FishingRodItem ||
                item instanceof FoodOnAStickItem) {
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (isPaper26(item)) {
            return SoundDefinition.of(aliased(PAPER));
        } else if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            ResourceLocation blockSound = SoundGenerator.getSoundType(block).getPlaceSound().location();

            if (block instanceof BaseRailBlock) {
                return SoundDefinition.of(aliased(RAIL));
            } else if (block instanceof BannerBlock) {
                return SoundDefinition.of(aliased(BANNER));
            } else if (block instanceof SeaPickleBlock) {
                return SoundDefinition.of(event(blockSound, 0.4f));
            } else if (block instanceof LeavesBlock || block instanceof BushBlock || block instanceof SugarCaneBlock) {
                ResourceLocation soundId = SoundGenerator.getSoundType(block).getPlaceSound().location();
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
        *///?} else {
        //? if >=1.21 {
        String itemId = BuiltInRegistries.ITEM.getKey(item).getPath();
        if (itemId.contains("music_disc")) {
        //?} else {
        /*if (item instanceof RecordItem) {
        *///?}
            return SoundDefinition.of(aliased(MUSIC_DISC));
        } else if (item instanceof BoatItem) {
            return SoundDefinition.of(aliased(BOAT));
        } else if (item instanceof TieredItem toolItem) {
            if (toolItem.getTier() instanceof Tiers mat) {
                return getTierItemSound(mat);
            }
            return SoundDefinition.of(aliased(Gear.GENERIC));
        } else if (item instanceof ArmorItem armorItem) {
            //? if >=1.21 {
            if (armorMaterials.contains(armorItem.getMaterial())) {
                return getArmorMaterialSound(armorItem.getMaterial());
            }
            //?} else {
            /*if (armorItem.getMaterial() instanceof ArmorMaterials mat) {
                return getArmorMaterialSound(mat);
            }
            *///?}
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
        //? if >=1.21 {
        } else if (item instanceof SuspiciousStewItem) {
        //?} else {
        /*} else if (item instanceof BowlFoodItem || item instanceof SuspiciousStewItem) {
        *///?}
            return SoundDefinition.of(aliased(BOWL));
        //? if >=1.19 {
        } else if (item instanceof InstrumentItem) {
            return SoundDefinition.of(single(LOOSE_METAL.getLocation(), 0.6f, 0.9f, Sound.Type.SOUND_EVENT));
        //?}
        //? if >=1.20 {
        } else if (item instanceof SmithingTemplateItem) {
            return SoundDefinition.of(aliased(LOOSE_METAL));
        //?}
        //? if >=1.19 {
        } else if (item instanceof DiscFragmentItem) {
            return SoundDefinition.of(single(METAL_BITS.getLocation(), 0.7f, 0.85f, Sound.Type.SOUND_EVENT));
        //?}
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
            //? if >=1.19 {
            } else if (block instanceof RotatedPillarBlock pillarBlock && SoundGenerator.getSoundType(pillarBlock).equals(SoundType.FROGLIGHT)) {
                return SoundDefinition.of(event(blockSound, 0.3f));
            //?}
            }

            return SoundDefinition.of(event(blockSound));
        }

        return SoundDefinition.of(aliased(ITEM_PICK));
        //?}
    }

    //? if >=26.1 {
    /*@NotNull
    private static boolean isArmor26(Item item) {
        var equippable = item.components().get(DataComponents.EQUIPPABLE);
        return equippable != null && equippable.slot().getType() == EquipmentSlot.Type.HUMANOID_ARMOR;
    }

    @NotNull
    private static SoundDefinition getToolItemSound26(Item item) {
        return getMaterialSound26(item);
    }

    @NotNull
    private static SoundDefinition getArmorMaterialSound26(Item item) {
        return getMaterialSound26(item);
    }

    @NotNull
    private static SoundDefinition getMaterialSound26(Item item) {
        var repairable = item.components().get(DataComponents.REPAIRABLE);
        if (repairable != null) {
            var items = repairable.items();
            if (items.contains(Items.NETHERITE_INGOT.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.NETHERITE));
            if (items.contains(Items.DIAMOND.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.DIAMOND));
            if (items.contains(Items.GOLD_INGOT.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.GOLDEN));
            if (items.contains(Items.IRON_INGOT.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.IRON));
            if (items.contains(Items.COPPER_INGOT.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.IRON));
            if (items.contains(Items.STONE.builtInRegistryHolder()) || items.contains(Items.COBBLESTONE.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.STONE));
            if (items.contains(Items.OAK_PLANKS.builtInRegistryHolder()) || items.contains(Items.STICK.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.WOOD));
            if (items.contains(Items.BREEZE_ROD.builtInRegistryHolder()))
                return SoundDefinition.of(aliased(Gear.HEAVY));
        }
        return SoundDefinition.of(aliased(Gear.GENERIC));
    }

    @NotNull
    private static boolean isPaper26(Item item) {
        return item.components().has(DataComponents.WRITABLE_BOOK_CONTENT) ||
                item.components().has(DataComponents.WRITTEN_BOOK_CONTENT) ||
                item instanceof EmptyMapItem || item instanceof MapItem || item instanceof NameTagItem ||
                item instanceof KnowledgeBookItem;
    }
    *///?}

    //? if >=1.21 && <26.1 {
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
    //?} elif <1.21 {
    /*@SuppressWarnings("all")
    @NotNull
    private static SoundDefinition getArmorMaterialSound(ArmorMaterials mat) {
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
    *///?}

    //? if <26.1 {
    @SuppressWarnings("UnnecessaryDefault")
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
        //? if >=1.21 {
        return item instanceof AnimalArmorItem || item instanceof CompassItem ||
                item instanceof SpyglassItem || item instanceof ShearsItem;
        //?} else {
        /*return item instanceof HorseArmorItem || item instanceof CompassItem ||
                item instanceof SpyglassItem || item instanceof ShearsItem;
        *///?}
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
    //?}

    private static boolean isBrickItem(Item item) {
        return item == Items.BRICK || SoundGenerator.getDescriptionId(item).endsWith("pottery_sherd");
    }
}
