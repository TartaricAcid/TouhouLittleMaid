package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static boolean isStackIn(EntityMaid maid, Predicate<ItemStack> filter) {
        return findStackSlot(maid.getAvailableInv(false), filter) >= 0;
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

    public static ItemStack getStack(EntityMaid maid, Predicate<ItemStack> filter) {
        return getStack(maid.getAvailableInv(false), filter);
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

    /**
     * 获取物品Id
     */
    public static String getItemId(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        Preconditions.checkNotNull(key);
        return key.toString();
    }


    /**
     * 获取物品
     */
    public static ItemStack getItemStack(String itemId) {
        ResourceLocation resourceLocation = new ResourceLocation(itemId);
        Item value = ForgeRegistries.ITEMS.getValue(resourceLocation);
        Preconditions.checkNotNull(value);
        return new ItemStack(value);
    }

    public static void giveItemToMaid(EntityMaid maid, ItemStack itemStack) {
        IItemHandler inv = maid.getAvailableInv(false);
        ItemStack stack = ItemHandlerHelper.insertItemStacked(inv, itemStack, false);
        if (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(maid.level(), maid.getX(), maid.getY() + 0.5, maid.getZ(), stack);
            maid.level.addFreshEntity(itemEntity);
        }
    }

    /**
     * 判断玩家主背包（包括快捷栏）能否插入物品
     *
     * @param player 要检查的玩家
     * @return 如果背包已满返回true，否则返回false
     */
    public static boolean canItemInsert(Player player, ItemStack testStack) {
        // 获取玩家主背包的物品处理器（与giveItemToPlayer使用相同的包装器）
        IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());

        // 遍历所有背包槽位
        for (int i = 0; i < inventory.getSlots(); i++) {
            // 模拟插入物品（第三个参数为true表示仅测试，不实际修改物品栏）
            ItemStack remainder = inventory.insertItem(i, testStack, true);

            // 如果插入后没有剩余，说明该槽位可以容纳物品
            if (remainder.isEmpty()) {
                return true;
            }
        }

        // 所有槽位都无法容纳测试物品
        return false;
    }
}
