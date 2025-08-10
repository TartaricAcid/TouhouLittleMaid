package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.IItemHandler;

/**
 * 在女仆使用隙间传输物品时触发
 * <p>
 * 此事件可以取消，取消后则不会进行物品传输
 */
@Cancelable
public abstract class MaidWirelessIOEvent extends Event {
    private final EntityMaid maid;
    /**
     * 女仆的物品栏
     */
    private final IItemHandler maidInv;
    /**
     * 箱子的物品栏
     */
    private final IItemHandler chestInv;
    /**
     * 隙间过滤器的标记的物品
     */
    private final IItemHandler filterInv;
    /**
     * 是否是黑名单模式
     */
    private final boolean isBlacklist;
    /**
     * 物品栏的配置，true 表示该槽位 不 允许传输
     */
    private final boolean[] slotConfig;

    public MaidWirelessIOEvent(EntityMaid maid, IItemHandler maidInv, IItemHandler chestInv, IItemHandler filterInv, boolean isBlacklist, boolean[] slotConfig) {
        this.maid = maid;
        this.maidInv = maidInv;
        this.chestInv = chestInv;
        this.filterInv = filterInv;
        this.isBlacklist = isBlacklist;
        this.slotConfig = slotConfig;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public IItemHandler getMaidInv() {
        return maidInv;
    }

    public IItemHandler getChestInv() {
        return chestInv;
    }

    public IItemHandler getFilterInv() {
        return filterInv;
    }

    public boolean isBlacklist() {
        return isBlacklist;
    }

    public boolean[] getSlotConfig() {
        return slotConfig;
    }

    public static class MaidToChest extends MaidWirelessIOEvent {
        public MaidToChest(EntityMaid maid, IItemHandler maidInv, IItemHandler chestInv, IItemHandler filterInv, boolean isBlacklist, boolean[] slotConfig) {
            super(maid, maidInv, chestInv, filterInv, isBlacklist, slotConfig);
        }
    }

    public static class ChestToMaid extends MaidWirelessIOEvent {
        public ChestToMaid(EntityMaid maid, IItemHandler maidInv, IItemHandler chestInv, IItemHandler filterInv, boolean isBlacklist, boolean[] slotConfig) {
            super(maid, maidInv, chestInv, filterInv, isBlacklist, slotConfig);
        }
    }
}
