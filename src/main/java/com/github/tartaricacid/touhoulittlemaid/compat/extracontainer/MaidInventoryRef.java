package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.function.Predicate;

public class MaidInventoryRef implements ContainerRef {
    @Override
    public boolean containing(EntityMaid maid, ItemStack itemToCheck) {
        // 女仆物品栏不存在 O(1) 复杂度的物品包含方法，只能遍历
        IItemHandler inv = maid.getAvailableInv(false);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stackInSlot = inv.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                continue;
            }
            if (ItemStack.isSameItemSameTags(stackInSlot, itemToCheck)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack insert(EntityMaid maid, ItemStack itemstack, boolean simulate) {
        IItemHandler inv = maid.getAvailableInv(false);
        return ItemHandlerHelper.insertItemStacked(inv, itemstack, simulate);
    }

    @Override
    public ItemStack extract(EntityMaid maid, Predicate<ItemStack> filter, int maxCount) {
        return ItemStack.EMPTY;
    }
}
