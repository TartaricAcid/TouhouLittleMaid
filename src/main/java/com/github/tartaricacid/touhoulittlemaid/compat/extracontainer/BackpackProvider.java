package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer;

import net.minecraft.world.item.ItemStack;

public interface BackpackProvider {
    boolean isBackpack(ItemStack stack);

    ContainerRef createSlotRef(String slotType, int slotIndex);
}
