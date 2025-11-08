package com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation;

import com.github.tartaricacid.touhoulittlemaid.util.CenterOffsetBlockPosSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import org.jetbrains.annotations.Nullable;

/**
 * 水下的寻路节点计算器，修改了起点计算方法
 */
public class MaidUnderWaterNodeEvaluator extends AmphibiousNodeEvaluator implements ICachedEvaluator {
    protected CenterOffsetBlockPosSet vis;

    public MaidUnderWaterNodeEvaluator() {
        // 倾向于浅处游泳？那不是好事一桩？
        super(true);
    }

    @Override
    public void prepare(PathNavigationRegion pLevel, Mob pMob) {
        super.prepare(pLevel, pMob);
    }

    @Override
    public void done() {
        super.done();
        vis = null;
    }

    @Override
    public Node getStart() {
        int x = Mth.floor(this.mob.getBoundingBox().minX);
        int y = Mth.floor(this.mob.getBoundingBox().minY + 0.5);
        int z = Mth.floor(this.mob.getBoundingBox().minZ);
        return this.getStartNode(new BlockPos(x, y, z));
    }

    @Override
    public void init(int x, int y, int z, int cx, int cy, int cz) {
        vis = new CenterOffsetBlockPosSet(x, y, z, cx, cy, cz);
    }

    @Override
    public void markVis(BlockPos pPos) {
        vis.markVis(pPos);
    }

    /**
     * 使得出水路径能够在水面上一格生成路径点，使得女仆不容易卡在水底
     */
    @Override
    public int getNeighbors(Node[] outputArray, Node node) {
        int nodeId = super.getNeighbors(outputArray, node);
        BlockPos blockPos = node.asBlockPos();
        if (level.getFluidState(blockPos).is(FluidTags.WATER) && level.getFluidState(blockPos.above()).isEmpty()) {
            Node aboveNode = this.getNode(node.x, node.y + 1, node.z);
            if (!aboveNode.closed) {
                aboveNode.costMalus++;
                outputArray[nodeId++] = aboveNode;
            }
        }
        return nodeId;
    }

    /**
     * 删除斜向上岸的路径
     */
    @Override
    protected boolean isNeighborValid(@Nullable Node neighbor, Node node) {
        // 快速可达判断的缓存机制
        if (neighbor != null && vis != null && vis.isVis(neighbor.asBlockPos())) {
            return false;
        }
        BlockPos blockPos = node.asBlockPos();
        if (neighbor != null && level.getFluidState(blockPos).is(FluidTags.WATER)
            && level.getFluidState(neighbor.asBlockPos()).isEmpty()
            && node.y != neighbor.y && (node.x != neighbor.x || node.z != neighbor.z)) {
            return false;
        }
        return super.isNeighborValid(neighbor, node);
    }

    @Nullable
    @Override
    protected Node findAcceptedNode(int pX, int pY, int pZ, int verticalDeltaLimit, double nodeFloorLevel, Direction direction, BlockPathTypes pathType) {
        // 快速可达判断的缓存机制
        if (vis != null && vis.isVis(pX, pY, pZ)) {
            return null;
        }
        return super.findAcceptedNode(pX, pY, pZ, verticalDeltaLimit, nodeFloorLevel, direction, pathType);
    }
}