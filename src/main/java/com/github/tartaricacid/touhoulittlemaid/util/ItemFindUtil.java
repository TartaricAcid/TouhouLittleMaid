package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

/**
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public final class ItemFindUtil {
    private ItemFindUtil() {
    }

    /**
     * 传入 IItemHandler 和判定条件 filter，获取对应的格子数
     *
     * @return 如果没找到，返回 -1
     */
    public static int findItem(IItemHandler handler, Predicate<ItemStack> filter) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (filter.test(stack)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 符合 filter 条件的物品是否在 handler 中
     */
    public static boolean isItemIn(IItemHandler handler, Predicate<ItemStack> filter) {
        return findItem(handler, filter) >= 0;
    }
}
