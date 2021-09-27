package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDamageableBauble extends Item {
    public ItemDamageableBauble(int durability) {
        super((new Properties()).tab(InitItems.MAIN_TAB).durability(durability).setNoRepair());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
