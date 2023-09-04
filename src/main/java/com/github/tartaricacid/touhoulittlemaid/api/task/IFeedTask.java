package com.github.tartaricacid.touhoulittlemaid.api.task;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IFeedTask extends IMaidTask {
    /**
     * 对应的物品是否可以作为食物
     *
     * @param stack 对应的物品
     * @param owner 喂食的对象
     * @return 是否可以作为食物
     */
    boolean isFood(ItemStack stack, Player owner);

    /**
     * 获取对应食物的优先级
     *
     * @param stack 传入的物品
     * @param owner 喂食的对象
     * @return 对应食物的优先级
     */
    Priority getPriority(ItemStack stack, Player owner);

    /**
     * 具体喂食的执行逻辑
     *
     * @param stack 喂食的物品
     * @param owner 喂食的对象
     * @return 喂食后的物品
     */
    ItemStack feed(ItemStack stack, Player owner);

    enum Priority {
        /**
         * 恢复的饱食度超过当前玩家饥饿值的食物，暂时不会进行喂食
         */
        LOWEST,
        /**
         * 恢复的饱食度低于当前玩家饥饿值的食物，优先级次之
         */
        LOW,
        /**
         * 恰好匹配当前饥饿值的食物，也可用于一些具有特殊效果的东西，优先级最高
         */
        HIGH,
        /**
         * 不属于上述分类的食物
         */
        NONE
    }
}
