package com.github.tartaricacid.touhoulittlemaid.util;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class MiscUtil
{
    private MiscUtil()
    {
    }

    public static int findItem(IItemHandler handler, Predicate<ItemStack> filter)
    {
        for (int i = 0; i < handler.getSlots(); i++)
        {
            ItemStack stack = handler.getStackInSlot(i);
            if (filter.test(stack))
            {
                return i;
            }
        }
        return -1;
    }
}
