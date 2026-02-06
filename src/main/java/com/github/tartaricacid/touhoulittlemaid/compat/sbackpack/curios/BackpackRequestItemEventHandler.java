package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidRequestItemEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.MaidBackpackHandler;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.ITrackedContentsItemHandler;

import java.util.List;
import java.util.function.Predicate;

public class BackpackRequestItemEventHandler {

    @SubscribeEvent
    public void onMaidRequestItem(MaidRequestItemEvent event) {
        EntityMaid           maid   = event.getMaid();
        Predicate<ItemStack> filter = event.getItemFilter();
        int                  maxCnt = event.getMaxCount();

        List<MaidBackpackCache.ContainerRef> containers = MaidBackpackCache.getContainers(maid);
        if (containers.size() <= 1) return;

        for (int i = 1; i < containers.size(); i++) {
            MaidBackpackCache.ContainerRef ref = containers.get(i);
            if (!(ref instanceof MaidBackpackCache.BackpackSlotRef backpackRef)) continue;

            ItemStack extracted = extractItemsFromBackpack(backpackRef, filter, maxCnt);
            if (extracted.isEmpty()) continue;

            ItemStack remaining = transferToMaidInv(maid, extracted);
            if (remaining.getCount() == extracted.getCount())
                if (transferToBackpack(maid, containers))
                    remaining = transferToMaidInv(maid, extracted);

            if (remaining.getCount() < extracted.getCount()) {
                int insertedCount = extracted.getCount() - remaining.getCount();
                ItemStack result = extracted.copyWithCount(insertedCount);

                if (!remaining.isEmpty()) backpackRef.insert(remaining, false);

                event.setRequestedItem(result);
                event.setCanceled(true);
                return;
            } else backpackRef.insert(extracted, false);
        }
    }

    /**
     * 尝试从背包中提取符合条件的物品
     *
     * @param backpackRef 背包引用
     * @param filter      物品筛选条件
     * @param maxCount    最大提取数量，-1 表示自动根据物品堆叠上限确定
     * @return 提取的物品，如果没找到则返回 {@link ItemStack#EMPTY}
     */
    private ItemStack extractItemsFromBackpack(MaidBackpackCache.BackpackSlotRef backpackRef,
                                              Predicate<ItemStack> filter, int maxCount) {
        ItemStack backpackStack = backpackRef.getBackpackStack();
        if (backpackStack.isEmpty()) return ItemStack.EMPTY;

        return backpackStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance())
                .map(wrapper -> {
                    ITrackedContentsItemHandler inv = wrapper.getInventoryForUpgradeProcessing();

                    for (int slot = 0; slot < inv.getSlots(); slot++) {
                        ItemStack stackInSlot = inv.getStackInSlot(slot);
                        if (stackInSlot.isEmpty() || !filter.test(stackInSlot)) continue;

                        int itemMaxStack = stackInSlot.getMaxStackSize();
                        int effectiveMaxCount = (maxCount == -1) 
                                ? itemMaxStack 
                                : Math.min(maxCount, itemMaxStack);
                        int extractCount = Math.min(effectiveMaxCount, stackInSlot.getCount());
                        ItemStack extracted = inv.extractItem(slot, extractCount, false);
                        return extracted;
                    }
                    return ItemStack.EMPTY;
                })
                .orElse(ItemStack.EMPTY);
    }

    private ItemStack transferToMaidInv(EntityMaid maid, ItemStack stack) {
        IItemHandler inv = maid.getAvailableInv(false);
        return ItemHandlerHelper.insertItemStacked(inv, stack, false);
    }

    /**
     * 尝试将物品栏中的物品放入背包以腾出空间
     * 会跳过装饰槽位 {@link MaidBackpackHandler#BACKPACK_ITEM_SLOT}
     * 该槽位物品永远不会被尝试放入背包
     */
    private boolean transferToBackpack(EntityMaid maid, List<MaidBackpackCache.ContainerRef> containers) {
        RangedWrapper inv = maid.getAvailableBackpackInv();
        
        int targetSlot = -1;
        ItemStack targetStack = ItemStack.EMPTY;
        for (int i = inv.getSlots() - 1; i >= 0; i--) {
            if (i == MaidBackpackHandler.BACKPACK_ITEM_SLOT) continue;
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            targetSlot = i;
            targetStack = stack;
            break;
        }
        
        if (targetSlot < 0 || targetStack.isEmpty()) return false;

        // 尝试将该物品放入背包
        // 优先放到已有该物品的背包中，但排除物品栏自身（从索引1开始）
        ItemStack remaining = targetStack.copy();
        final int slotToEmpty = targetSlot;
        final ItemStack originalStack = targetStack;

        for (int i = 1; i < containers.size(); i++) {
            MaidBackpackCache.ContainerRef ref = containers.get(i);
            if (!(ref instanceof MaidBackpackCache.BackpackSlotRef backpackRef)) continue;

            if (!backpackRef.containing(remaining)) continue;
            remaining = backpackRef.insert(remaining, false);
            if (remaining.isEmpty()) break;
        }

        if (!remaining.isEmpty()) {
            for (int i = 1; i < containers.size(); i++) {
                MaidBackpackCache.ContainerRef ref = containers.get(i);
                if (!(ref instanceof MaidBackpackCache.BackpackSlotRef backpackRef)) continue;

                remaining = backpackRef.insert(remaining, false);
                if (remaining.isEmpty()) break;
            }
        }

        if (remaining.getCount() < originalStack.getCount()) {
            if (remaining.isEmpty()) inv.extractItem(slotToEmpty, originalStack.getCount(), false);
            else inv.extractItem(slotToEmpty, originalStack.getCount() - remaining.getCount(), false);

            return inv.getStackInSlot(slotToEmpty).isEmpty() ||
                   inv.getStackInSlot(slotToEmpty).getCount() < inv.getStackInSlot(slotToEmpty).getMaxStackSize();
        }

        return false;
    }
}
