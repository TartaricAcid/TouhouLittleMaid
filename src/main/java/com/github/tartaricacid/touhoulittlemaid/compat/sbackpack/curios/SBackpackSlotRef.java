package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios.CuriosSlotRef;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.SBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemHandlerHelper;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.ITrackedContentsItemHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;

import java.util.Set;
import java.util.function.Predicate;

public class SBackpackSlotRef extends CuriosSlotRef {
    public SBackpackSlotRef(String slotType, int slotIndex) {
        super(slotType, slotIndex);
    }

    public ItemStack getBackpackStack(EntityMaid maid) {
        ItemStack stack = getCuriosStack(maid);
        if (SBackpackCompat.isBackpack(stack)) {
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
        var capability = backpackStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
        return capability.map(wrapper -> {
            ITrackedContentsItemHandler inv = wrapper.getInventoryForUpgradeProcessing();
            Set<ItemStackKey> trackedStacks = inv.getTrackedStacks();
            return trackedStacks.stream().anyMatch(key ->
                    ItemStack.isSameItemSameTags(key.getStack(), itemToCheck));
        }).orElse(false);
    }

    @Override
    public ItemStack insert(EntityMaid maid, ItemStack itemStack, boolean simulate) {
        ItemStack backpackStack = getBackpackStack(maid);
        if (backpackStack.isEmpty()) {
            return itemStack;
        }
        var capability = backpackStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
        return capability.map(wrapper -> {
            ITrackedContentsItemHandler inv = wrapper.getInventoryForUpgradeProcessing();
            return ItemHandlerHelper.insertItemStacked(inv, itemStack, simulate);
        }).orElse(itemStack);
    }

    @Override
    public ItemStack extract(EntityMaid maid, Predicate<ItemStack> filter, int maxCount) {
        ItemStack backpackStack = getBackpackStack(maid);
        if (backpackStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        Capability<IBackpackWrapper> instance = CapabilityBackpackWrapper.getCapabilityInstance();
        var capability = backpackStack.getCapability(instance);

        return capability.map(wrapper -> {
            ITrackedContentsItemHandler inv = wrapper.getInventoryForUpgradeProcessing();
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                if (stackInSlot.isEmpty() || !filter.test(stackInSlot)) {
                    continue;
                }

                int itemMaxStack = stackInSlot.getMaxStackSize();
                int effectiveMaxCount = (maxCount == -1)
                        ? itemMaxStack
                        : Math.min(maxCount, itemMaxStack);
                int extractCount = Math.min(effectiveMaxCount, stackInSlot.getCount());
                return inv.extractItem(slot, extractCount, false);
            }
            return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
    }
}
