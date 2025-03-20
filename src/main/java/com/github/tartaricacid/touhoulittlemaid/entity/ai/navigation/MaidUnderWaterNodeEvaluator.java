package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 水下的寻路节点计算器，修改了起点计算方法
 */
public class MaidUnderWaterNodeEvaluator extends AmphibiousNodeEvaluator {
    public MaidUnderWaterNodeEvaluator() {
        // 倾向于浅处游泳？那不是好事一桩？
        super(false);
    }
    @Override
    public Node getStart() {
        return this.getStartNode(new BlockPos(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5D), Mth.floor(this.mob.getBoundingBox().minZ)));
    }
}