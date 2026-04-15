package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.ref;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;

public interface ContainerRef {
    boolean containing(EntityMaid maid, ItemStack itemToCheck);

    ItemStack insert(EntityMaid maid, ItemStack itemStack, boolean simulate);
}
