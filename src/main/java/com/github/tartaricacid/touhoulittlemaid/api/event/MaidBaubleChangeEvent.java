package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * 女仆饰品变化事件
 * 当玩家给女仆佩戴或卸下饰品时触发
 */
@ApiStatus.AvailableSince("1.4.3")
public abstract class MaidBaubleChangeEvent extends LivingEvent {
    private final EntityMaid maid;
    private final ItemStack baubleItem;

    public MaidBaubleChangeEvent(EntityMaid maid, ItemStack baubleItem) {
        super(maid);
        this.maid = maid;
        this.baubleItem = baubleItem;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ItemStack getBaubleItem() {
        return baubleItem;
    }

    /**
     * 当玩家给女仆佩戴任意饰品时触发
     */
    public static class PutOn extends MaidBaubleChangeEvent {
        public PutOn(EntityMaid maid, ItemStack baubleItem) {
            super(maid, baubleItem);
        }
    }

    /**
     * 当玩家从女仆身上卸下任意饰品时触发
     */
    public static class TakeOff extends MaidBaubleChangeEvent {
        public TakeOff(EntityMaid maid, ItemStack baubleItem) {
            super(maid, baubleItem);
        }
    }
}
