package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

/**
 * 当女仆需要从外部获取物品到自己物品栏时触发此事件。
 * 此事件可取消。如果取消，表示已处理完毕，不再继续传递给其他处理器。
 */
@ApiStatus.AvailableSince("1.5.1")
public class MaidRequestItemEvent extends Event {
    private final EntityMaid maid;
    private final Predicate<ItemStack> itemFilter;
    private final int maxCount;
    private ItemStack requestedItem = ItemStack.EMPTY;

    /**
     * @param maid       请求物品的女仆
     * @param itemFilter 物品筛选条件
     * @param maxCount   最大请求数量
     */
    public MaidRequestItemEvent(EntityMaid maid, Predicate<ItemStack> itemFilter, int maxCount) {
        this.maid = maid;
        this.itemFilter = itemFilter;
        this.maxCount = maxCount;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public Predicate<ItemStack> getItemFilter() {
        return itemFilter;
    }

    public int getMaxCount() {
        return maxCount;
    }

    /**
     * 获取处理结果，即已成功转移到女仆物品栏的物品
     */
    public ItemStack getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(ItemStack requestedItem) {
        this.requestedItem = requestedItem;
    }

    public boolean hasRequestedItem() {
        return !requestedItem.isEmpty();
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
