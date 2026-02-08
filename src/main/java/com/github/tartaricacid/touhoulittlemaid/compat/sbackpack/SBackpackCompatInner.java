package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack;

import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;

public class SBackpackCompatInner {
    static boolean isBackpack(ItemStack stack) {
        return stack.getItem() instanceof BackpackItem;
    }
}
