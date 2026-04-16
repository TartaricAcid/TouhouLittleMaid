package com.github.tartaricacid.touhoulittlemaid.compat.tbackpack.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios.CuriosSlotRef;
import com.github.tartaricacid.touhoulittlemaid.compat.tbackpack.TBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public class TBackpackSlotRef extends CuriosSlotRef {
    public TBackpackSlotRef(String slotType, int slotIndex) {
        super(slotType, slotIndex);
    }

    private ItemStack getBackpackStack(EntityMaid maid) {
        ItemStack stack = getCuriosStack(maid);
        if (TBackpackCompat.isBackpack(stack)) {
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean containing(EntityMaid maid, ItemStack itemToCheck) {
        ItemStack backpackStack = getBackpackStack(maid);
        if (backpackStack.isEmpty()) {
            return false;
        }
        return backpackStack.getCapability(ForgeCapabilities.ITEM_HANDLER).map(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stackInSlot = handler.getStackInSlot(i);
                if (!stackInSlot.isEmpty() && ItemStack.isSameItemSameTags(stackInSlot, itemToCheck)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    @Override
    public ItemStack insert(EntityMaid maid, ItemStack itemStack, boolean simulate) {
        ItemStack backpackStack = getBackpackStack(maid);
        if (backpackStack.isEmpty()) {
            return itemStack;
        }
        return backpackStack.getCapability(ForgeCapabilities.ITEM_HANDLER).map(handler -> {
            ItemStack remaining = itemStack.copy();
            for (int i = 0; i < handler.getSlots(); i++) {
                remaining = handler.insertItem(i, remaining, simulate);
                if (remaining.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
            return remaining;
        }).orElse(itemStack);
    }

    @Override
    public ItemStack extract(EntityMaid maid, Predicate<ItemStack> filter, int maxCount) {
        ItemStack backpackStack = getBackpackStack(maid);
        if (backpackStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return backpackStack.getCapability(ForgeCapabilities.ITEM_HANDLER).map(handler ->
                extractFromHandler(handler, filter, maxCount)
        ).orElse(ItemStack.EMPTY);
    }

    private static ItemStack extractFromHandler(IItemHandler handler, Predicate<ItemStack> filter, int maxCount) {
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stackInSlot = handler.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || !filter.test(stackInSlot)) {
                continue;
            }
            int itemMaxStack = stackInSlot.getMaxStackSize();
            int effectiveMaxCount = (maxCount == -1)
                    ? itemMaxStack
                    : Math.min(maxCount, itemMaxStack);
            int extractCount = Math.min(effectiveMaxCount, stackInSlot.getCount());
            return handler.extractItem(slot, extractCount, false);
        }
        return ItemStack.EMPTY;
    }
}
