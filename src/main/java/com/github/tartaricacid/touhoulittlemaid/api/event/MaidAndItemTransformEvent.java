package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

/**
 * 当把女仆转成物品或从物品转成女仆时触发的事件。
 * <p>
 * 比如魂符收取女仆、相机拍照、女仆死亡掉落胶片都会触发
 */
public abstract class MaidAndItemTransformEvent extends Event {
    /**
     * 当前打算保存或写入数据的女仆实体
     * <p>
     * 注意此时还没给这个女仆实体附加下面的 data 数据
     */
    private final EntityMaid maid;
    /**
     * 当前打算保存或写入数据的物品，可能是魂符、胶片等
     * <p>
     * 注意此时还没给这个物品附加下面的 data 数据
     */
    private final ItemStack item;
    /**
     * 打算保存或写入的数据，仅包含女仆实体数据
     */
    private final CompoundTag data;

    public MaidAndItemTransformEvent(EntityMaid maid, ItemStack item, CompoundTag data) {
        this.maid = maid;
        this.item = item;
        this.data = data;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ItemStack getItem() {
        return item;
    }

    public CompoundTag getData() {
        return data;
    }

    public static class ToItem extends MaidAndItemTransformEvent {
        public ToItem(EntityMaid maid, ItemStack item, CompoundTag data) {
            super(maid, item, data);
        }
    }

    public static class ToMaid extends MaidAndItemTransformEvent {
        public ToMaid(EntityMaid maid, ItemStack item, CompoundTag data) {
            super(maid, item, data);
        }
    }
}
