package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface IFarmTask extends IMaidTask {
    /**
     * 传入的 ItemStack 对象是否可以作为种子
     *
     * @param stack ItemStack
     * @return boolean
     */
    boolean isSeed(ItemStack stack);

    /**
     * 当前位置方块是否可以收获
     *
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param cropState 作物的 BlockState
     * @return 是否可以收获
     */
    boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    /**
     * 对应的收获行为
     *
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param cropState 作物的 BlockState
     */
    void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    /**
     * 该位置是否可以种植作物
     *
     * @param maid      女仆对象
     * @param basePos   作物基底坐标
     * @param baseState 作物基底状态
     * @param seed      种植的种子
     * @return 是否可以种植
     */
    boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed);

    /**
     * 对应的种植行为
     *
     * @param maid      女仆对象
     * @param basePos   作物基底坐标
     * @param baseState 作物基底状态
     * @param seed      种植的种子
     * @return 种植后返回的物品
     */
    ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed);

    /**
     * 获取最小的种植、收获距离
     *
     * @return 距离
     */
    default int getCloseEnoughDist() {
        return 1;
    }
}
