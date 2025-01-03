package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class MaidClimbTask extends Behavior<EntityMaid> {
    public MaidClimbTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel pLevel, EntityMaid maid, long pGameTime) {
        // 初始化女仆动量
        // 将女仆定格在楼梯中心，取消掉 x、z 轴的动量，避免爬楼梯过程中摔死
        BlockPos currentPosition = maid.blockPosition().mutable();
        Vec3 centerPos = Vec3.atCenterOf(currentPosition);
        maid.moveTo(centerPos.x, currentPosition.getY(), centerPos.z);
        maid.setDeltaMovement(0, maid.getDeltaMovement().y(), 0);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityMaid maid) {
        // 如果禁用了主动攀爬能力，就直接返回，不执行后续的操作
        if (!maid.getConfigManager().isActiveClimbing()) {
            return false;
        }
        return maid.onClimbable();
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        return this.checkExtraStartConditions(pLevel, pEntity);
    }

    @Override
    protected void tick(ServerLevel level, EntityMaid maid, long pGameTime) {
        Path path = maid.getNavigation().getPath();
        if (path == null) {
            return;
        }

        // 获取基础信息：下一个要到达的节点、女仆当前所处坐标、方块
        int beGoNodeIndex = path.getNextNodeIndex();
        Node beGoNode = path.getNode(beGoNodeIndex);
        BlockPos maidFeetPos = maid.blockPosition();
        BlockState feetBlock = level.getBlockState(maidFeetPos);

        // 判断上行还是下行
        boolean up = true;
        if (beGoNodeIndex > 0) {
            Node currentNext = path.getNode(beGoNodeIndex - 1);
            Node pointNext = path.getNode(beGoNodeIndex);
            if (pointNext.y <= currentNext.y) {
                up = false;
            }
        }

        // 如果是下行，添加 shift 行为
        maid.setShiftKeyDown(!up);

        // 控制上行和下行楼梯的动量
        // 将水平方向的动量关掉，与上面的初始化，定格在方块中心，不然会摔死……
        // 先给一个大点的 y 轴向量，再拉回一点，这样能连续下去
        // 原版的爬楼梯数值为 0.15，有些慢，加快点……
        // 而且速度太慢的话，爬楼梯时间过长，路径就被掐断了，
        // 就又会重新规划路线……这样控制比较麻烦，而且也还有其他的东西在干扰……
        // 最好的是一次路径控制完，这样的效果是最好的
        if (maidFeetPos.getY() <= beGoNode.y && up && feetBlock.isLadder(level, maidFeetPos, maid)) {
            double yMotion0 = 1;
            double yMotion = 0.25;
            maid.setDeltaMovement(0, yMotion0, 0);
            maid.setDeltaMovement(0, yMotion, 0);
        } else {
            double yMotion0 = -1;
            double yMotion = -0.25;
            maid.setDeltaMovement(0, yMotion0, 0);
            maid.setDeltaMovement(0, yMotion, 0);
        }

        // 对下行做出额外处理
        // 下行的最近节点索引值很奇怪……
        // 女仆都还没到那个下一个节点附近，就启动切换到下一个节点了……
        // 上行的就没有这个问题……
        if (!up && beGoNode.y != maidFeetPos.getY()) {
            int nodeCount = path.getNodeCount();
            for (int i1 = 0; i1 < nodeCount; i1++) {
                Node node = path.getNode(i1);
                Node nextNode = path.getNode(Math.min(i1 + 1, nodeCount - 1));
                // 获取正确的节点信息
                if (node.y == maidFeetPos.getY() && node.x == maidFeetPos.getX() && node.z == maidFeetPos.getZ() && node.y == nextNode.y) {
                    beGoNodeIndex = i1;
                    beGoNode = node;
                    // 更正最近索引点
                    path.setNextNodeIndex(i1);
                    break;
                }
            }
        }
        // 控制正常情况下到达该段楼梯节点顶部或者底部向着平台进发
        if ((beGoNode.y - maidFeetPos.getY() >= 0 && beGoNode.y - maidFeetPos.getY() <= 1.2) && beGoNodeIndex + 1 < path.getNodeCount()) {
            Node currentNext = path.getNode(beGoNodeIndex);
            Node pointNext = path.getNode(beGoNodeIndex + 1);

            boolean beWalkSurface = pointNext.y == currentNext.y;
            if (beWalkSurface || pointNext == path.getEndNode() || maidFeetPos.getY() == currentNext.y) {
                // 给予女仆当前坐标与水平节点的x、z方向的差值向量，
                // 让其向着那个水平节点进发，脱离楼梯等可爬行物体，不再继续爬楼梯或者停留在上面
                int x1 = pointNext.x - currentNext.x;
                int z1 = pointNext.z - currentNext.z;
                double y = maid.getDeltaMovement().y();
                maid.setDeltaMovement(0.2, 1, 0.2);
                maid.setDeltaMovement(x1 * 0.3, y + 0.012, z1 * 0.3);
                // TODO：将身体转向下一个节点
                maid.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pointNext.asVec3()));
            }
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid maid, long pGameTime) {
        maid.getNavigation().stop();
        maid.setShiftKeyDown(false);
    }
}
