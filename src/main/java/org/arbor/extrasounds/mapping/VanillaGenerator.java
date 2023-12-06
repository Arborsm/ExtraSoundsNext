package org.arbor.extrasounds.mapping;

import org.arbor.extrasounds.ExtraSounds;
import org.arbor.extrasounds.mixin.BlockMaterialAccessor;
import org.arbor.extrasounds.mixin.BucketFluidAccessor;
import org.arbor.extrasounds.sounds.Categories.Gear;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.core.Registry;
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
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
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
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.arbor.extrasounds.sounds.Categories.*;
import static org.arbor.extrasounds.sounds.Sounds.*;

public class VanillaGenerator
{
    public static final Map<Class<? extends Item>, Function<Item, SoundEventRegistration>> map = new HashMap<>();

    public static SoundGenerator generator = SoundGenerator.of("minecraft", ExtraSounds.MODID, item -> {
        ResourceLocation id = Registry.ITEM.getKey(item);
        Class cls = item.getClass();
        while (!map.containsKey(cls) && cls.getSuperclass() != null && Item.class.isAssignableFrom(cls.getSuperclass()))
            cls = cls.getSuperclass();
        return SoundDefinition.of(map.containsKey(cls) ? map.get(cls).apply(item) : aliased(ITEM_PICK));
    });

    static
    {
        map.put(RecordItem.class, it -> aliased(MUSIC_DISC));
        map.put(BoatItem.class, it -> aliased(BOAT));
        map.put(TieredItem.class, it -> {
            if (((TieredItem) it).getTier() instanceof Tiers mat)
                return switch (mat)
                        {
                            case WOOD -> aliased(Gear.WOOD);
                            case STONE -> aliased(Gear.STONE);
                            case IRON -> aliased(Gear.IRON);
                            case GOLD -> aliased(Gear.GOLDEN);
                            case DIAMOND -> aliased(Gear.DIAMOND);
                            case NETHERITE -> aliased(Gear.NETHERITE);
                            default -> aliased(Gear.GENERIC);
                            //⬆ even though not required, this is in case any mods add to the enum of materials
                        };
            else
                return aliased(Gear.GENERIC);
        });
        map.put(ArmorItem.class, it -> {
            if (((ArmorItem) it).getMaterial() instanceof ArmorMaterials mat)
                return switch (mat)
                        {
                            case IRON -> aliased(Gear.IRON);
                            case GOLD -> aliased(Gear.GOLDEN);
                            case DIAMOND -> aliased(Gear.DIAMOND);
                            case NETHERITE -> aliased(Gear.NETHERITE);
                            case CHAIN -> aliased(Gear.CHAIN);
                            case TURTLE -> aliased(Gear.TURTLE);
                            case LEATHER -> aliased(Gear.LEATHER);
                            default -> aliased(Gear.GENERIC);
                        };
            else
                return aliased(Gear.GENERIC);
        });
        map.put(ShieldItem.class, it -> aliased(Gear.IRON));
        putMulti(it -> aliased(Gear.GOLDEN), HorseArmorItem.class, CompassItem.class, SpyglassItem.class,
                 ShearsItem.class);
        putMulti(it -> aliased(Gear.LEATHER), LeadItem.class, ElytraItem.class, SaddleItem.class);
        putMulti(it -> aliased(Gear.GENERIC), BowItem.class, CrossbowItem.class, FishingRodItem.class,
                 FoodOnAStickItem.class);
        map.put(BucketItem.class, it -> {
            var f = ((BucketFluidAccessor) it).getContent();
            return f.getPickupSound().isPresent() ?
                    event(f.getPickupSound().get().getLocation(), 0.4f) : aliased(METAL);
        });
        map.put(MinecartItem.class, it -> aliased(MINECART));
        map.put(ItemFrameItem.class, it -> aliased(FRAME));
        putMulti(it -> aliased(POTION), PotionItem.class, ExperienceBottleItem.class);
        putMulti(it -> aliased(PAPER), BannerPatternItem.class, BookItem.class, WritableBookItem.class,
                 WrittenBookItem.class,
                 EnchantedBookItem.class, EmptyMapItem.class, MapItem.class, NameTagItem.class);
        map.put(ArrowItem.class, it -> aliased(ARROW));
        map.put(DyeItem.class, it -> aliased(DUST));
        map.put(SpawnEggItem.class, it -> aliased(WET_SLIPPERY));
        putMulti(it -> aliased(BOWL), BowlFoodItem.class, SuspiciousStewItem.class);
        map.put(InstrumentItem.class, it -> single(LOOSE_METAL.getLocation(), 0.6f, 0.9f, Sound.Type.SOUND_EVENT));
        map.put(BlockItem.class, it -> {
            Block b = ((BlockItem) it).getBlock();
            ResourceLocation blockSound = b.getSoundType(b.defaultBlockState()).getPlaceSound().getLocation();

            if (b instanceof BaseRailBlock)
                return aliased(RAIL);
            else if (b instanceof BannerBlock)
                return aliased(BANNER);
            else if (b instanceof SeaPickleBlock)
                return event(blockSound, 0.4f);
            else if (b instanceof LeavesBlock || b instanceof BushBlock || b instanceof SugarCaneBlock)
            {
                ResourceLocation soundId = b.getSoundType(b.defaultBlockState()).getPlaceSound().getLocation();
                if (soundId.getPath().equals("block.grass.place"))
                    return aliased(LEAVES);
                else
                    return event(soundId);
            }
            else if (b instanceof RotatedPillarBlock && ((BlockMaterialAccessor) b).getMaterial().equals(Material.FROGLIGHT))
            {
                return event(blockSound, 0.3f);
            }

            return event(blockSound);
        });
    }

    @SafeVarargs
    private static void putMulti(Function<Item, SoundEventRegistration> entry, Class<? extends Item>... classez)
    {
        for (var clazz : classez)
            map.put(clazz, entry);
    }
}
