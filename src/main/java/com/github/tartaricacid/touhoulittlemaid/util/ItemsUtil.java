package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.function.Predicate;

public final class ItemsUtil {
    private ItemsUtil() {
    }

    /**
     * 掉落指定起始、结束槽位的物品
     */
    public static void dropEntityItems(Entity entity, IItemHandler itemHandler, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);
            ItemStack extractItem = itemHandler.extractItem(i, stackInSlot.getCount(), false);
            if (!extractItem.isEmpty()) {
                entity.spawnAtLocation(extractItem);
            }
        }
    }

    /**
     * 掉落指定起始的物品
     */
    public static void dropEntityItems(Entity entity, IItemHandler itemHandler, int startIndex) {
        dropEntityItems(entity, itemHandler, startIndex, itemHandler.getSlots());
    }

    /**
     * 掉落全部物品
     */
    public static void dropEntityItems(Entity entity, IItemHandler itemHandler) {
        dropEntityItems(entity, itemHandler, 0, itemHandler.getSlots());
    }

    /**
     * 传入 IItemHandler 和判定条件 filter，获取对应的格子数
     *
     * @return 如果没找到，返回 -1
     */
    public static int findStackSlot(IItemHandler handler, Predicate<ItemStack> filter) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (filter.test(stack)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取符合条件的 slot 列表
     */
    public static List<Integer> getFilterStackSlots(IItemHandler handler, Predicate<ItemStack> filter) {
        IntList slots = new IntArrayList();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (filter.test(stack)) {
                slots.add(i);
            }
        }
        return slots;
    }

    /**
     * 符合 filter 条件的物品是否在 handler 中
     */
    public static boolean isStackIn(IItemHandler handler, Predicate<ItemStack> filter) {
        return findStackSlot(handler, filter) >= 0;
    }

    /**
     * 获取符合 filter 添加的 ItemStack
     *
     * @return 如果该物品不存在，返回 ItemStack.EMPTY
     */
    public static ItemStack getStack(IItemHandler handler, Predicate<ItemStack> filter) {
        int slotIndex = findStackSlot(handler, filter);
        if (slotIndex >= 0) {
            return handler.getStackInSlot(slotIndex);
        } else {
            return ItemStack.EMPTY;
        }
    }

    /**
     * 获取女仆饰品栏的饰品数据
     *
     * @return 如果没找到，返回 -1
     */
    public static int getBaubleSlotInMaid(EntityMaid maid, IMaidBauble bauble) {
        BaubleItemHandler handler = maid.getMaidBauble();
        for (int i = 0; i < handler.getSlots(); i++) {
            IMaidBauble baubleIn = handler.getBaubleInSlot(i);
            if (baubleIn == bauble) {
                return i;
            }
        }
        return -1;
    }
}
