package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface ContainerRef {
    boolean containing(EntityMaid maid, ItemStack itemToCheck);

    ItemStack insert(EntityMaid maid, ItemStack itemStack, boolean simulate);

    ItemStack extract(EntityMaid maid, Predicate<ItemStack> filter, int maxCount);
}
