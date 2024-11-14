package com.github.tartaricacid.touhoulittlemaid.api.block;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IBoardGameBlock {
    /**
     * 女仆尝试游玩坐在棋盘旁边
     */
    void startMaidSit(EntityMaid maid, BlockState state, Level worldIn, BlockPos pos);
}