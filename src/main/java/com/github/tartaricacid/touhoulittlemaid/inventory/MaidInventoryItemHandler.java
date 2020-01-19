package com.github.tartaricacid.touhoulittlemaid.inventory;

import com.github.tartaricacid.touhoulittlemaid.item.ItemAlbum;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPhoto;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2020/1/18 1:36
 **/
public class MaidInventoryItemHandler extends ItemStackHandler {
    public MaidInventoryItemHandler(int size) {
        super(size);
    }

    public MaidInventoryItemHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    /**
     * 检查输入的物品是否是非法的
     */
    public static boolean isIllegalItem(ItemStack stack) {
        return stack.getItem() instanceof ItemShulkerBox || stack.getItem() instanceof ItemPhoto || stack.getItem() instanceof ItemAlbum;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return !isIllegalItem(stack);
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
