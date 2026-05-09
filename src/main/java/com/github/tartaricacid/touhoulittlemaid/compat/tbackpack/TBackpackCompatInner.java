package com.github.tartaricacid.touhoulittlemaid.compat.tbackpack;

import com.tiviacz.travelersbackpack.items.TravelersBackpackItem;
import net.minecraft.world.item.ItemStack;

public class TBackpackCompatInner {
    public static boolean isBackpack(ItemStack stack) {
        return stack.getItem() instanceof TravelersBackpackItem;
    }
}
