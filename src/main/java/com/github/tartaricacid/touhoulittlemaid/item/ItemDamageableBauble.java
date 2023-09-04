package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemDamageableBauble extends Item {
    public ItemDamageableBauble(int durability) {
        super((new Properties()).durability(durability).setNoRepair());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
