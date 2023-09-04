package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmMoveTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmPlantTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

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
    default double getCloseEnoughDist() {
        return 1.0;
    }

    /**
     * 是否检查作物上方有足够的空间
     *
     * @return 如果不检查，返回 false
     */
    default boolean checkCropPosAbove() {
        return true;
    }

    /**
     * 该模式的环境音效
     *
     * @param maid 女仆对象
     * @return 对应的环境音效
     */
    @Override
    default SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FARM.get(), 0.5f);
    }

    /**
     * 该模式下调用的 AI
     *
     * @param maid 女仆对象
     * @return 如果什么都不做，请返回空集合
     */
    @Override
    default List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidFarmMoveTask maidFarmMoveTask = new MaidFarmMoveTask(this, 0.6f, 16);
        MaidFarmPlantTask maidFarmPlantTask = new MaidFarmPlantTask(this);
        return Lists.newArrayList(Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask));
    }
}
