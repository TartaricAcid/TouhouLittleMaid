package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemNormalBauble extends Item {
    public ItemNormalBauble() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
