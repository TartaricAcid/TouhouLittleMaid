package com.github.tartaricacid.touhoulittlemaid.api.backpack;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 实现该接口的 SlotItemHandler 类会在槽位变化时触发饰品或者背包 takeoff 事件
 * <p>
 * Mojang 非常奇妙，Shift 点击转移物品时无法在 SlotItemHandler 自带的的 takeoff 方法中获取变化前物品的信息
 * 因为物品已经被清空了，所以只能通过自己来实现类似的功能
 */
public interface ITriggerSlotChange {
    /**
     * 当玩家 Shift 点击物品从槽位中取出时触发
     *
     * @param player 触发事件的玩家，可能为 null
     * @param stack  被取出的物品
     */
    void onShiftTakeoff(@Nullable Player player, ItemStack stack);
}
