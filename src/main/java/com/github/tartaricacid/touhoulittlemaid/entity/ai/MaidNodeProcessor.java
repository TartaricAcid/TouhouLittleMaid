package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class MaidNodeProcessor extends WalkNodeProcessor {
    @Override
    protected PathNodeType getPathNodeTypeRaw(IBlockAccess p_189553_1_, int p_189553_2_, int p_189553_3_, int p_189553_4_) {
        BlockPos blockpos = new BlockPos(p_189553_2_, p_189553_3_, p_189553_4_);
        IBlockState iblockstate = p_189553_1_.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();

        PathNodeType type = block.getAiPathNodeType(iblockstate, p_189553_1_, blockpos);
        if (type != null) {
            return type;
        }

        if (material == Material.AIR) {
            return PathNodeType.OPEN;
        } else if (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY) {
            // ===THLM patch===
            if (block == Blocks.COCOA) {
                return PathNodeType.BLOCKED;
            }
            // ======

            if (block == Blocks.FIRE) {
                return PathNodeType.DAMAGE_FIRE;
            } else if (block == Blocks.CACTUS) {
                return PathNodeType.DAMAGE_CACTUS;
            } else if (block instanceof BlockDoor && material == Material.WOOD && !iblockstate.getValue(BlockDoor.OPEN).booleanValue()) {
                return PathNodeType.DOOR_WOOD_CLOSED;
            } else if (block instanceof BlockDoor && material == Material.IRON && !iblockstate.getValue(BlockDoor.OPEN).booleanValue()) {
                return PathNodeType.DOOR_IRON_CLOSED;
            } else if (block instanceof BlockDoor && iblockstate.getValue(BlockDoor.OPEN).booleanValue()) {
                return PathNodeType.DOOR_OPEN;
            } else if (block instanceof BlockRailBase) {
                return PathNodeType.RAIL;
            } else if (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || iblockstate.getValue(BlockFenceGate.OPEN).booleanValue())) {
                if (material == Material.WATER) {
                    return PathNodeType.WATER;
                } else if (material == Material.LAVA) {
                    return PathNodeType.LAVA;
                } else {
                    return block.isPassable(p_189553_1_, blockpos) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
                }
            } else {
                return PathNodeType.FENCE;
            }
        } else {
            return PathNodeType.TRAPDOOR;
        }
    }
}
