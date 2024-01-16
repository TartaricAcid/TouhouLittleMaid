package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

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
        FluidActionResult emptiedSimulated = tryEmptyContainer(bucket, tank, false);
        if (emptiedSimulated.isSuccess()) {
            ItemStack remainder = ItemHandlerHelper.insertItemStacked(maidBackpack, emptiedSimulated.getResult(), true);
            if (remainder.isEmpty()) {
                FluidActionResult emptiedReal = tryEmptyContainer(bucket, tank, true);
                ItemHandlerHelper.insertItemStacked(maidBackpack, emptiedReal.getResult(), false);
                bucket.shrink(1);
                return new FluidActionResult(bucket);
            }
        }
        return FluidActionResult.FAILURE;
    }

    private static FluidActionResult tryEmptyContainer(@Nonnull ItemStack container, IFluidHandler fluidDestination, boolean doDrain) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1);
        return FluidUtil.getFluidHandler(containerCopy).map(containerFluidHandler -> {
            FluidStack transfer = FluidUtil.tryFluidTransfer(fluidDestination, containerFluidHandler, Integer.MAX_VALUE, doDrain);
            if (transfer.isEmpty()) return FluidActionResult.FAILURE;
            if (!doDrain) {
                containerFluidHandler.drain(transfer, IFluidHandler.FluidAction.EXECUTE);
            }
            ItemStack resultContainer = containerFluidHandler.getContainer();
            return new FluidActionResult(resultContainer);
        }).orElse(FluidActionResult.FAILURE);
    }
}
