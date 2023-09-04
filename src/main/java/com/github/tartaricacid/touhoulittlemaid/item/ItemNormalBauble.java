package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemNormalBauble extends Item {
    public ItemNormalBauble() {
        super((new Properties()).stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
