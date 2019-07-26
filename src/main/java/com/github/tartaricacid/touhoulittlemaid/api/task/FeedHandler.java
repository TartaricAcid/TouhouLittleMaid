package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * 用于女仆在喂食模式下的判定食物，执行喂食行为的接口
 *
 * @author Snownee
 * @date 2019/7/25 21:41
 */
public interface FeedHandler extends TaskHandler {
    /**
     * 对应的物品是否可以作为食物
     *
     * @param stack 对应的物品
     * @param owner 喂食的对象
     * @return 是否可以作为食物
     */
    boolean isFood(ItemStack stack, EntityPlayer owner);

    /**
     * 获取对应食物的 Trend
     *
     * @param stack 传入的物品
     * @param owner 喂食的对象
     * @return 对应食物的 Trend
     */
    Trend getTrend(ItemStack stack, EntityPlayer owner);

    /**
     * 具体喂食的执行逻辑
     *
     * @param stack 喂食的物品
     * @param owner 喂食的对象
     * @return 喂食后的物品
     */
    ItemStack feed(ItemStack stack, EntityPlayer owner);

    /**
     * 是否启用此 Handler
     *
     * @param maid 女仆对象
     * @return boolean
     */
    @Override
    default boolean canExecute(AbstractEntityMaid maid) {
        return true;
    }
}
