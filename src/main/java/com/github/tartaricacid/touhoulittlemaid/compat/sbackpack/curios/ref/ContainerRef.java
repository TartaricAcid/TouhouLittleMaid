package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.ref;

import net.minecraft.world.item.ItemStack;

public interface ContainerRef {
    boolean containing(ItemStack itemToCheck);

    ItemStack insert(ItemStack itemStack, boolean simulate);
}
