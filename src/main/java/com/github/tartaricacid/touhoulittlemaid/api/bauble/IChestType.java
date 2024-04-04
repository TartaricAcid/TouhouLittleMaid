package com.github.tartaricacid.touhoulittlemaid.api.bauble;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IChestType {
    int ALLOW_COUNT = 0;
    int DENY_COUNT = Integer.MAX_VALUE;

    /**
     * 该传入的 BlockEntity 是不是可识别的箱子
     *
     * @param chest 待检测的箱子
     * @return 是否能识别
     */
    boolean isChest(BlockEntity chest);

    /**
     * 用于检测箱子能否被玩家打开，防止标记非法的箱子
     *
     * @param chest  需要检查权限的箱子
     * @param player 检查权限的玩家
     * @return 能否打开
     */
    boolean canOpenByPlayer(BlockEntity chest, Player player);

    /**
     * 目前箱子的打开计数，原版用这个方法防止多个玩家同时操作一个箱子
     *
     * @param level 世界
     * @param pos   坐标
     * @param chest 箱子
     * @return <=0 代表可以放入，其他情况表示不能放入
     */
    int getOpenCount(BlockGetter level, BlockPos pos, BlockEntity chest);
}
