package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class MaidFluidUtil {
    public static FluidActionResult tankToBucket(ItemStack bucket, IFluidHandler tank, IItemHandler maidBackpack) {
        if (bucket.isEmpty()) {
            return FluidActionResult.FAILURE;
        }
        FluidActionResult filledSimulated = FluidUtil.tryFillContainer(bucket, tank, Integer.MAX_VALUE, null, false);
        if (filledSimulated.isSuccess()) {
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(maidBackpack, filledSimulated.getResult(), true);
            if (remainder.isEmpty()) {
                FluidActionResult filledReal = FluidUtil.tryFillContainer(bucket, tank, Integer.MAX_VALUE, null, true);
                ItemHandlerHelper.insertItemStacked(maidBackpack, filledReal.getResult(), false);
                bucket.shrink(1);
                return new FluidActionResult(bucket);
            }
        }
        return FluidActionResult.FAILURE;
    }

    public static FluidActionResult bucketToTank(ItemStack bucket, IFluidHandler tank, IItemHandler maidBackpack) {
        if (bucket.isEmpty()) {
            return FluidActionResult.FAILURE;
        }
        FluidActionResult emptiedSimulated = FluidUtil.tryEmptyContainer(bucket, tank, Integer.MAX_VALUE, null, false);
        if (emptiedSimulated.isSuccess()) {
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(maidBackpack, emptiedSimulated.getResult(), true);
            if (remainder.isEmpty()) {
                FluidActionResult emptiedReal = FluidUtil.tryEmptyContainer(bucket, tank, Integer.MAX_VALUE, null, true);
                ItemHandlerHelper.insertItemStacked(maidBackpack, emptiedReal.getResult(), false);
                bucket.shrink(1);
                return new FluidActionResult(bucket);
            }
        }
        return FluidActionResult.FAILURE;
    }
}
