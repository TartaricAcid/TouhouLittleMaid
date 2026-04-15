package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.ref;

import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.SBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.SBackpackCuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.ITrackedContentsItemHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.ItemStackKey;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Set;

public class BackpackSlotRef implements ContainerRef {
    public final String slotType;
    public final int slotIndex;
    public final int priority;

    public BackpackSlotRef(String slotType, int slotIndex) {
        this.slotType = slotType;
        this.slotIndex = slotIndex;
        this.priority = SBackpackCuriosCompat.getSlotPriority(slotType);
    }

    public ItemStack getBackpackStack(EntityMaid maid) {
        var inventory = CuriosApi.getCuriosInventory(maid);
        return inventory.map(handler -> handler.getStacksHandler(slotType)
                .map(stacksHandler -> {
                    IDynamicStackHandler stacks = stacksHandler.getStacks();
                    if (slotIndex >= stacks.getSlots()) {
                        return ItemStack.EMPTY;
                    }

                    ItemStack stack = stacks.getStackInSlot(slotIndex);
                    if (SBackpackCompat.isBackpack(stack)) {
                        return stack;
                    }

                    return ItemStack.EMPTY;
                }).orElse(ItemStack.EMPTY)
        ).orElse(ItemStack.EMPTY);
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
            return trackedStacks.stream()
                    .anyMatch(key -> ItemStack.isSameItemSameTags(key.getStack(), itemToCheck));
        }).orElse(false);
    }

    @Override
    public ItemStack insert(EntityMaid maid, ItemStack itemstack, boolean simulate) {
        ItemStack backpackStack = getBackpackStack(maid);
        if (backpackStack.isEmpty()) {
            return itemstack;
        }
        var capability = backpackStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
        return capability.map(wrapper -> {
            ITrackedContentsItemHandler inv = wrapper.getInventoryForUpgradeProcessing();
            return ItemHandlerHelper.insertItemStacked(inv, itemstack, simulate);
        }).orElse(itemstack);
    }

    public int compareTo(BackpackSlotRef other) {
        int priorityCompare = Integer.compare(this.priority, other.priority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return Integer.compare(this.slotIndex, other.slotIndex);
    }
}
