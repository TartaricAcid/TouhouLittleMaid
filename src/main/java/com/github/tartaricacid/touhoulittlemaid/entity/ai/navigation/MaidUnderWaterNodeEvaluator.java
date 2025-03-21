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
        return this.getStartNode(new BlockPos(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5D), Mth.floor(this.mob.getBoundingBox().minZ)));
    }

    protected CenterOffsetBlockPosSet vis;

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
    public int getNeighbors(Node[] pOutputArray, Node pNode) {
        int nodeId = super.getNeighbors(pOutputArray, pNode);
        if (level.getFluidState(pNode.asBlockPos()).is(FluidTags.WATER)
                && level.getFluidState(pNode.asBlockPos().above()).isEmpty()
        ) {
            Node node = this.getNode(pNode.x, pNode.y + 1, pNode.z);
            if (!node.closed) {
                node.costMalus++;
                pOutputArray[nodeId++] = node;
            }
        }
        return nodeId;
    }

    /**
     * 删除斜向上岸的路径
     */
    @Override
    protected boolean isNeighborValid(@Nullable Node pNeighbor, Node pNode) {
        // 快速可达判断的缓存机制
        if (pNeighbor != null && vis != null && vis.isVis(pNeighbor.asBlockPos()))
            return false;
        if (pNeighbor != null
                && level.getFluidState(pNode.asBlockPos()).is(FluidTags.WATER)
                && level.getFluidState(pNeighbor.asBlockPos()).isEmpty()
                && pNode.y != pNeighbor.y
                && (pNode.x != pNeighbor.x || pNode.z != pNeighbor.z)
        )
            return false;
        return super.isNeighborValid(pNeighbor, pNode);
    }

    @Nullable
    @Override
    protected Node findAcceptedNode(int pX, int pY, int pZ, int pVerticalDeltaLimit, double pNodeFloorLevel, Direction pDirection, BlockPathTypes pPathType) {
        // 快速可达判断的缓存机制
        if (vis != null && vis.isVis(pX, pY, pZ)) return null;
        return super.findAcceptedNode(pX, pY, pZ, pVerticalDeltaLimit, pNodeFloorLevel, pDirection, pPathType);
    }
}