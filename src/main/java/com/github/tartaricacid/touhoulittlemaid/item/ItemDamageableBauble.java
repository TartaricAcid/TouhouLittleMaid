package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemDamageableBauble extends Item {
    public ItemDamageableBauble(int durability) {
        super((new Properties()).tab(MAIN_TAB).durability(durability).setNoRepair());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
