package com.github.tartaricacid.touhoulittlemaid.entity.ai.goal;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MaidClimbGoal extends Goal {

    private final EntityMaid maid;
    private Path path;

    public MaidClimbGoal(EntityMaid maid) {
        this.maid = maid;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public void start() {
        super.start();
        this.maid.setClimb(true);

        // 初始化女仆动量
        // 将女仆定格在楼梯中心
        // 取消掉x、z轴的动量，避免爬楼梯过程中摔死
        BlockPos currentPosition = this.maid.blockPosition().mutable();
        Vec3 centerPos = Vec3.atCenterOf(currentPosition);
        this.maid.moveTo(centerPos.x, currentPosition.getY(), centerPos.z);
        Vec3 deltaMovement = this.maid.getDeltaMovement();
        this.maid.setDeltaMovement(0, deltaMovement.y(), 0);
    }

    @Override
    public boolean canUse() {
        // 如果禁用了主动攀爬能力，就直接返回，不执行后续的操作
        if (!this.maid.getConfigManager().isActiveClimbing()) {
            return false;
        }
        if (!this.maid.getNavigation().isDone()) {
            this.path = this.maid.getNavigation().getPath();
            if (this.path != null && this.maid.onClimbable()) {
                this.maid.setClimb(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void stop() {
        super.stop();
        this.maid.setClimb(false);
    }

    @Override
    public void tick() {
        Level maidLevel = this.maid.level;
        // 获取基础信息:下一个要到达的节点、女仆当前所处坐标、方块
        int beGoNodeIndex = this.path.getNextNodeIndex();
        Node beGoNode = this.path.getNode(beGoNodeIndex);
        BlockPos maidFeetPos = this.maid.blockPosition();
        BlockState feetBlock = maidLevel.getBlockState(maidFeetPos);

        // 判断上行还是下行
        boolean up = true;
        if (beGoNodeIndex > 0) {
            Node currentNext = this.path.getNode(beGoNodeIndex - 1);
            Node pointNext = this.path.getNode(beGoNodeIndex);
            if (pointNext.y <= currentNext.y) {
                up = false;
            }
        }

        // 控制上行和下行楼梯的动量
        // 将水平方向的动量关掉,与上面的初始化，定格在方块中心，不然会摔死...
        // 先给一个大点的y轴向量，再拉回一点，这样能连续下去
        // 原版的爬楼梯数值为0.15，有些慢，加快点...
        // 而且速度太慢的话，爬楼梯时间过长，路径就被掐断了，
        // 就又会重新规划路线...这样控制比较麻烦，而且也还有其他的东西在干扰...
        // 最好的是一次路径控制完，这样的效果是最好的，
        if (maidFeetPos.getY() <= beGoNode.y && up && feetBlock.isLadder(maidLevel, maidFeetPos, this.maid)) {
            double yMotion0 = 1;
            double yMotion = 0.25;
            this.maid.setDeltaMovement(0, yMotion0, 0);
            this.maid.setDeltaMovement(0, yMotion, 0);
        } else {
            double yMotion0 = -1;
            double yMotion = -0.25;
            this.maid.setDeltaMovement(0, yMotion0, 0);
            this.maid.setDeltaMovement(0, yMotion, 0);
        }

        // 对下行做出额外处理
        // 下行的最近节点索引值很奇怪...
        // 女仆都还没到那个下一个节点附近，就启动切换到下一个节点了...
        // 上行的就没有这个问题...
        if (!up && beGoNode.y != maidFeetPos.getY()) {
            int nodeCount = this.path.getNodeCount();
            for (int i1 = 0; i1 < nodeCount; i1++) {
                Node node = this.path.getNode(i1);
                Node nextNode = this.path.getNode(Math.min(i1 + 1, nodeCount - 1));
                // 获取正确的节点信息
                if (node.y == maidFeetPos.getY() && node.x == maidFeetPos.getX() && node.z == maidFeetPos.getZ() && node.y == nextNode.y) {
                    beGoNodeIndex = i1;
                    beGoNode = node;
                    // 更正最近索引点
                    this.path.setNextNodeIndex(i1);
                    break;
                }
            }
        }
        // 控制正常情况下到达该段楼梯节点顶部或者底部向着平台进发
        if ((beGoNode.y - maidFeetPos.getY() >= 0 && beGoNode.y - maidFeetPos.getY() <= 1.2) && beGoNodeIndex + 1 < this.path.getNodeCount()) {
            Node currentNext = this.path.getNode(beGoNodeIndex);
            Node pointNext = this.path.getNode(beGoNodeIndex + 1);

            boolean beWalkSurface = pointNext.y == currentNext.y;
            if (beWalkSurface || pointNext == this.path.getEndNode() || maidFeetPos.getY() == currentNext.y) {
                this.maid.setClimb(false);

                // 给予女仆当前坐标与水平节点的x、z方向的差值向量，
                // 让其向着那个水平节点进发，脱离楼梯等可爬行物体，不再继续爬楼梯或者停留在上面
                int x1 = pointNext.x - currentNext.x;
                int z1 = pointNext.z - currentNext.z;
                double y = this.maid.getDeltaMovement().y();
                this.maid.setDeltaMovement(0.2, 1, 0.2);
                this.maid.setDeltaMovement(x1 * 0.3, y + 0.012, z1 * 0.3);
                //@todo 将身体转向下一个节点
//                this.maid.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pointNext.asVec3()));
                return;
            }
        }
    }
}
