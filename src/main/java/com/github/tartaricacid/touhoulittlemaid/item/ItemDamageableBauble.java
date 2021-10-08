package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.github.tartaricacid.touhoulittlemaid.init.MaidGroup.MAIN_TAB;

public class ItemDamageableBauble extends Item {
    public ItemDamageableBauble(int durability) {
        super((new Properties()).tab(MAIN_TAB).durability(durability).setNoRepair());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
