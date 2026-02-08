package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.SBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.ref.BackpackSlotRef;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.ref.ContainerRef;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.ref.MaidInventoryRef;
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
public class MaidBackpackCache {
    private static final WeakHashMap<EntityMaid, List<ContainerRef>> CACHE = new WeakHashMap<>();

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
        BackpackSlotRef newRef = new BackpackSlotRef(maid, slotType, slotIndex);

        for (int i = 1; i < containers.size(); i++) {
            ContainerRef ref = containers.get(i);
            if (!(ref instanceof BackpackSlotRef backpackRef)) {
                continue;
            }
            if (!backpackRef.slotType.equals(slotType)) {
                continue;
            }
            if (backpackRef.slotIndex != slotIndex) {
                continue;
            }
            // 槽位类型与槽位索引均相同，无需处理
            return;
        }

        int insertIndex = containers.size();
        for (int i = 1; i < containers.size(); i++) {
            ContainerRef ref = containers.get(i);
            if (!(ref instanceof BackpackSlotRef backpackRef)) {
                continue;
            }
            if (newRef.compareTo(backpackRef) >= 0) {
                continue;
            }
            insertIndex = i;
            break;
        }
        containers.add(insertIndex, newRef);
    }

    public static void onUnequipped(EntityMaid maid, String slotType, int slotIndex) {
        List<ContainerRef> containers = getContainers(maid);
        containers.removeIf(ref -> {
            if (ref instanceof BackpackSlotRef backpackRef) {
                return backpackRef.slotType.equals(slotType) && backpackRef.slotIndex == slotIndex;
            }
            return false;
        });
    }

    public static void invalidate(EntityMaid maid) {
        CACHE.remove(maid);
    }

    private static List<ContainerRef> buildContainerRefs(EntityMaid maid) {
        List<ContainerRef> containers = Lists.newArrayList();

        containers.add(new MaidInventoryRef(maid));
        if (!CuriosCompat.isLoadedOrEnable()) {
            return containers;
        }

        List<BackpackSlotRef> backpackRefs = Lists.newArrayList();
        CuriosApi.getCuriosInventory(maid).ifPresent(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                String slotType = entry.getKey();
                ICurioStacksHandler stacksHandler = entry.getValue();
                IDynamicStackHandler stacks = stacksHandler.getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (!SBackpackCompat.isBackpack(stack)) {
                        continue;
                    }
                    backpackRefs.add(new BackpackSlotRef(maid, slotType, i));
                }
            }
        });

        for (BackpackSlotRef newRef : backpackRefs) {
            int insertIndex = containers.size();
            for (int i = 1; i < containers.size(); i++) {
                ContainerRef ref = containers.get(i);
                if (!(ref instanceof BackpackSlotRef backpackRef)) {
                    continue;
                }
                if (newRef.compareTo(backpackRef) >= 0) {
                    continue;
                }
                insertIndex = i;
                break;
            }
            containers.add(insertIndex, newRef);
        }

        return containers;
    }
}
