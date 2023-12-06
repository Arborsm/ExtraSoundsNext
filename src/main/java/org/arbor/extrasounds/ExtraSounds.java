package org.arbor.extrasounds;

import net.minecraftforge.fml.common.Mod;
import org.arbor.extrasounds.debug.DebugUtils;
import org.arbor.extrasounds.mapping.SoundPackLoader;
import org.arbor.extrasounds.sounds.Categories;
import org.arbor.extrasounds.sounds.SoundType;
import org.arbor.extrasounds.sounds.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mod(ExtraSounds.MODID)
public class ExtraSounds {
    public static final String MODID = "extrasounds";
    static final RandomSource mcRandom = RandomSource.create();

    public ExtraSounds(){
        //load classes so they register all resources before they're used
        Object loader = Categories.HAY;
        loader = Sounds.CHAT;
        loader = Sounds.Actions.BOW_PULL;

        SoundPackLoader.init();
        DebugUtils.init();
    }

    public static void hotbar(int i)
    {
        ItemStack stack = Minecraft.getInstance().player.getInventory().getItem(i);
        if (stack.getItem() == Items.AIR)
            SoundManager.playSound(Sounds.HOTBAR_SCROLL, SoundType.HOTBAR);
        else
            SoundManager.playSound(stack, SoundType.HOTBAR);
    }

    public static void inventoryClick(Slot slot, ItemStack cursor, ClickType actionType)
    {
        ItemStack clicked = slot.getItem();
        boolean hasCursor = !cursor.isEmpty();
        boolean hasSlot = !clicked.isEmpty();

        switch (actionType)
        {
            case PICKUP_ALL:
                if (hasCursor)
                    SoundManager.playSound(Sounds.ITEM_PICK_ALL, SoundType.PICKUP);
                return;
            case CLONE:
                SoundManager.playSound(Sounds.ITEM_CLONE, SoundType.PLACE);
                return;
            default:
                if (hasCursor) SoundManager.playSound(cursor, SoundType.PICKUP);
                else if (hasSlot)
                    SoundManager.playSound(clicked, SoundType.PLACE);
        }
    }

    public static String getClickId(ResourceLocation id, SoundType type)
    {
        return getClickId(id, type, true);
    }

    public static String getClickId(ResourceLocation id, SoundType type, boolean includeNamespace)
    {
        return (includeNamespace ? MODID + ":" : "") + type.prefix + "." + id.getNamespace() + "." + id.getPath();
    }
}