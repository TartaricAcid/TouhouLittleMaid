package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.SBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.ITrackedContentsItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * 女仆容器缓存，缓存女仆的容器列表（物品栏 + Curios 栏上的背包）
 * 容器优先级：物品栏（永远为首个元素）> back 槽位背包 > trinkets 槽位背包 > 其他槽位
 * 同槽位类型按 slotIndex 排序
 */
public class MaidBackpackCache {
    private static final WeakHashMap<EntityMaid, List<ContainerRef>> CACHE = new WeakHashMap<>();

    public static abstract class ContainerRef {
        public abstract boolean containing(ItemStack itemToCheck);
        public abstract ItemStack insert(ItemStack itemstack, boolean simulate);
    }

    public static class MaidInventoryRef extends ContainerRef {
        private final EntityMaid maid;

        public MaidInventoryRef(EntityMaid maid) {
            this.maid = maid;
        }

        @Override
        public boolean containing(ItemStack itemToCheck) {
            IItemHandler inv = maid.getAvailableInv(false);
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stackInSlot = inv.getStackInSlot(i);
                if (stackInSlot.isEmpty()) continue;
                if (ItemStack.isSameItemSameTags(stackInSlot, itemToCheck)) return true;
            }
            return false;
        }

        @Override
        public ItemStack insert(ItemStack itemstack, boolean simulate) {
            return ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(false), itemstack, simulate);
        }
    }

    public static class BackpackSlotRef extends ContainerRef {
        public final String slotType;
        public final int slotIndex;
        public final int priority;
        private final EntityMaid maid;

        public BackpackSlotRef(EntityMaid maid, String slotType, int slotIndex) {
            this.maid = maid;
            this.slotType = slotType;
            this.slotIndex = slotIndex;
            this.priority = SBackpackCuriosCompat.getSlotPriority(slotType);
        }

        public ItemStack getBackpackStack() {
            return CuriosApi.getCuriosInventory(maid)
                    .map(handler -> handler.getStacksHandler(slotType)
                            .map(stacksHandler -> {
                                IDynamicStackHandler stacks = stacksHandler.getStacks();
                                if (slotIndex >= stacks.getSlots()) return ItemStack.EMPTY;
                                
                                ItemStack stack = stacks.getStackInSlot(slotIndex);
                                if (SBackpackCompat.isBackpack(stack)) return stack;

                                return ItemStack.EMPTY;
                            })
                            .orElse(ItemStack.EMPTY))
                    .orElse(ItemStack.EMPTY);
        }

        @Override
        public boolean containing(ItemStack itemToCheck) {
            ItemStack backpackStack = getBackpackStack();
            if (backpackStack.isEmpty()) return false;

            return backpackStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance())
                    .map(wrapper -> {
                        ITrackedContentsItemHandler inv = wrapper.getInventoryForUpgradeProcessing();
                        for (int i = 0; i < inv.getSlots(); i++) {
                            ItemStack stackInSlot = inv.getStackInSlot(i);
                            if (stackInSlot.isEmpty()) continue;
                            if (ItemStack.isSameItemSameTags(stackInSlot, itemToCheck)) return true;
                        }
                        return false;
                    })
                    .orElse(false);
        }

        @Override
        public ItemStack insert(ItemStack itemstack, boolean simulate) {
            ItemStack backpackStack = getBackpackStack();
            if (backpackStack.isEmpty()) return itemstack;

            return backpackStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance())
                    .map(wrapper -> {
                        ITrackedContentsItemHandler inv = wrapper.getInventoryForUpgradeProcessing();
                        return ItemHandlerHelper.insertItemStacked(inv, itemstack, simulate);
                    })
                    .orElse(itemstack);
        }

        public int compareTo(BackpackSlotRef other) {
            int priCmp = Integer.compare(this.priority, other.priority);
            if (priCmp != 0) return priCmp;
            return Integer.compare(this.slotIndex, other.slotIndex);
        }
    }

    public static List<ContainerRef> getContainers(EntityMaid maid) {
        List<ContainerRef> containers = CACHE.get(maid);
        if (containers == null) {
            containers = buildContainerRefs(maid);
            CACHE.put(maid, containers);
        }
        return containers;
    }

    public static void onEquipped(EntityMaid maid, String slotType, int slotIndex) {
        List<ContainerRef> containers = getContainers(maid);
        BackpackSlotRef    newRef     = new BackpackSlotRef(maid, slotType, slotIndex);

        for (int i = 1; i < containers.size(); i++) {
            ContainerRef ref = containers.get(i);
            if (!(ref instanceof BackpackSlotRef backpackRef)) continue;
            if (!backpackRef.slotType.equals(slotType)) continue;
            if (backpackRef.slotIndex != slotIndex) continue;
            // 槽位类型与槽位索引均相同，无需处理
            return;
        }

        int insertIndex = containers.size();
        for (int i = 1; i < containers.size(); i++) {
            ContainerRef ref = containers.get(i);
            if (!(ref instanceof BackpackSlotRef backpackRef)) continue;
            if (newRef.compareTo(backpackRef) >= 0) continue;
            insertIndex = i;
            break;
        }
        containers.add(insertIndex, newRef);
    }

    public static void onUnequipped(EntityMaid maid, String slotType, int slotIndex) {
        List<ContainerRef> containers = getContainers(maid);

        containers.removeIf(ref -> {
            if (ref instanceof BackpackSlotRef backpackRef)
                return backpackRef.slotType.equals(slotType) && backpackRef.slotIndex == slotIndex;
            return false;
        });
    }

    public static void invalidate(EntityMaid maid) {
        CACHE.remove(maid);
    }

    private static List<ContainerRef> buildContainerRefs(EntityMaid maid) {
        List<ContainerRef> containers = new ArrayList<>();

        containers.add(new MaidInventoryRef(maid));
        if (!CuriosCompat.isLoadedOrEnable()) return containers;

        List<BackpackSlotRef> backpackRefs = new ArrayList<>();
        CuriosApi.getCuriosInventory(maid).ifPresent(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                String slotType = entry.getKey();
                ICurioStacksHandler stacksHandler = entry.getValue();
                IDynamicStackHandler stacks = stacksHandler.getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (!SBackpackCompat.isBackpack(stack)) continue;
                    backpackRefs.add(new BackpackSlotRef(maid, slotType, i));
                }
            }
        });

        for (BackpackSlotRef newRef : backpackRefs) {
            int insertIndex = containers.size();
            for (int i = 1; i < containers.size(); i++) {
                ContainerRef ref = containers.get(i);
                if (!(ref instanceof BackpackSlotRef backpackRef)) continue;
                if (newRef.compareTo(backpackRef) >= 0) continue;
                insertIndex = i;
                break;
            }
            containers.add(insertIndex, newRef);
        }

        return containers;
    }
}
