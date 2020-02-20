package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class MaidNodeProcessor extends WalkNodeProcessor {
    @Nonnull
    @Override
    protected PathNodeType getPathNodeTypeRaw(IBlockAccess blockAccess, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState blockState = blockAccess.getBlockState(pos);
        Block block = blockState.getBlock();
        Material material = blockState.getMaterial();
        PathNodeType type = block.getAiPathNodeType(blockState, blockAccess, pos);

        if (type != null) {
            return type;
        }
        if (material == Material.AIR) {
            return PathNodeType.OPEN;
        }
        if (block == Blocks.COCOA) {
            // 防止可可豆阻塞寻路算法
            return PathNodeType.BLOCKED;
        }
        // 原版内容
        if (block == Blocks.TRAPDOOR || block == Blocks.IRON_TRAPDOOR || block == Blocks.WATERLILY) {
            return PathNodeType.TRAPDOOR;
        }
        if (block == Blocks.FIRE) {
            return PathNodeType.DAMAGE_FIRE;
        }
        if (block == Blocks.CACTUS) {
            return PathNodeType.DAMAGE_CACTUS;
        }
        if (block instanceof BlockDoor && material == Material.WOOD && !blockState.getValue(BlockDoor.OPEN)) {
            return PathNodeType.DOOR_WOOD_CLOSED;
        }
        if (block instanceof BlockDoor && material == Material.IRON && !blockState.getValue(BlockDoor.OPEN)) {
            return PathNodeType.DOOR_IRON_CLOSED;
        }
        if (block instanceof BlockDoor && blockState.getValue(BlockDoor.OPEN)) {
            return PathNodeType.DOOR_OPEN;
        }
        if (block instanceof BlockRailBase) {
            return PathNodeType.RAIL;
        }
        boolean closedFenceGate = block instanceof BlockFenceGate && !blockState.getValue(BlockFenceGate.OPEN);
        if (block instanceof BlockFence || block instanceof BlockWall || closedFenceGate) {
            return PathNodeType.FENCE;
        }
        if (material == Material.WATER) {
            return PathNodeType.WATER;
        }
        if (material == Material.LAVA) {
            return PathNodeType.LAVA;
        }
        return block.isPassable(blockAccess, pos) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
    }
}
