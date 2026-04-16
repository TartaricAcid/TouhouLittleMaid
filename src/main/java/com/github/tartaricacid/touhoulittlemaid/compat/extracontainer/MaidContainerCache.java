package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios.CuriosSlotRef;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.WeakHashMap;

/**
 * 女仆容器缓存，缓存女仆的容器列表（物品栏 + Curios 栏上的背包）
 * 容器优先级：物品栏（永远为首个元素）> back 槽位背包 > trinkets 槽位背包 > 其他槽位
 * 同槽位类型按 slotIndex 排序
 */
public class MaidContainerCache {
    private static final WeakHashMap<EntityMaid, List<ContainerRef>> CACHE = new WeakHashMap<>();

    public static List<ContainerRef> getContainers(EntityMaid maid) {
        List<ContainerRef> containers = CACHE.get(maid);
        if (containers == null) {
            containers = buildContainerRefs(maid);
            CACHE.put(maid, containers);
        }
        return containers;
    }

    public static void onEquipped(EntityMaid maid, ItemStack stack, String slotType, int slotIndex) {
        List<ContainerRef> containers = getContainers(maid);
        ContainerRef newRef = ExtraContainerManager.tryCreateSlotRef(stack, slotType, slotIndex);
        if (newRef == null || !(newRef instanceof CuriosSlotRef newSlotRef)) {
            return;
        }

        for (int i = 1; i < containers.size(); i++) {
            ContainerRef ref = containers.get(i);
            if (!(ref instanceof CuriosSlotRef slotRef)) {
                continue;
            }
            if (slotRef.slotType.equals(slotType) && slotRef.slotIndex == slotIndex) {
                return;
            }
        }

        int insertIndex = containers.size();
        for (int i = 1; i < containers.size(); i++) {
            ContainerRef ref = containers.get(i);
            if (!(ref instanceof CuriosSlotRef slotRef)) {
                continue;
            }
            if (newSlotRef.compareTo(slotRef) < 0) {
                insertIndex = i;
                break;
            }
        }
        containers.add(insertIndex, newSlotRef);
    }

    public static void onUnequipped(EntityMaid maid, String slotType, int slotIndex) {
        List<ContainerRef> containers = getContainers(maid);
        containers.removeIf(ref -> {
            if (ref instanceof CuriosSlotRef slotRef) {
                return slotRef.slotType.equals(slotType) && slotRef.slotIndex == slotIndex;
            }
            return false;
        });
    }

    public static void invalidate(EntityMaid maid) {
        CACHE.remove(maid);
    }

    public static int getCacheSize() {
        return CACHE.size();
    }

    private static List<ContainerRef> buildContainerRefs(EntityMaid maid) {
        List<ContainerRef> containers = Lists.newArrayList();

        containers.add(new MaidInventoryRef());
        if (!CuriosCompat.isLoadedOrEnable()) {
            return containers;
        }

        List<CuriosSlotRef> slotRefs = Lists.newArrayList();
        CuriosApi.getCuriosInventory(maid).ifPresent(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                String slotType = entry.getKey();
                ICurioStacksHandler stacksHandler = entry.getValue();
                IDynamicStackHandler stacks = stacksHandler.getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    ContainerRef ref = ExtraContainerManager.tryCreateSlotRef(stack, slotType, i);
                    if (ref instanceof CuriosSlotRef curiosRef) {
                        slotRefs.add(curiosRef);
                    }
                }
            }
        });

        for (CuriosSlotRef newRef : slotRefs) {
            int insertIndex = containers.size();
            for (int i = 1; i < containers.size(); i++) {
                ContainerRef ref = containers.get(i);
                if (!(ref instanceof CuriosSlotRef slotRef)) {
                    continue;
                }
                if (newRef.compareTo(slotRef) < 0) {
                    insertIndex = i;
                    break;
                }
            }
            containers.add(insertIndex, newRef);
        }

        return containers;
    }
}
