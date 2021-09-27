package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemNormalBauble extends Item {
    public ItemNormalBauble() {
        super((new Properties()).tab(InitItems.MAIN_TAB).stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
