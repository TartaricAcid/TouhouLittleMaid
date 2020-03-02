package com.github.tartaricacid.touhoulittlemaid.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;

import javax.annotation.Nonnull;

public class MaidHandsItemHandler extends EntityHandsInvWrapper {
    public MaidHandsItemHandler(EntityLivingBase entity) {
        super(entity);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return !MaidInventoryItemHandler.isIllegalItem(stack);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        // 物品合法才允许插入，否则原样返回
        if (isItemValid(slot, stack)) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }
}
