package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.INavigationMixin;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation.MaidPathNavigation;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation.MaidUnderWaterPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jetbrains.annotations.Nullable;

public class MaidNavigationManager {
    private final MaidPathNavigation basicNavigation;
    private final AmphibiousPathNavigation waterNavigation;
    private final EntityMaid maid;
    private final Level level;
    private Mode mode = Mode.GROUND;

    public MaidNavigationManager(EntityMaid maid) {
        this.maid = maid;
        this.level = maid.level;
        this.basicNavigation = new MaidPathNavigation(maid, maid.level);
        this.waterNavigation = new MaidUnderWaterPathNavigation(maid, maid.level);
        maid.setNavigation(basicNavigation);
    }

    public void tick() {
        if (!level.isClientSide && maid.isEffectiveAi()) {
            if (mode != Mode.WATER) {
                handleGroundMode();
            } else {
                handleWaterMode();
            }
        }

        // 确保非游泳模式下不会保持游泳状态
        if (mode != Mode.WATER) {
            maid.getSwimManager().setWantToSwim(false);
        }
    }

    /**
     * 处理地面模式下的导航逻辑
     */
    private void handleGroundMode() {
        // 检查是否需要切换到水中寻路
        boolean shouldSwitchToWater = false;

        if (maid.isInWater() && shouldStartOrStopSwim(5)) {
            // 前方有长水面时切换到水中寻路
            shouldSwitchToWater = true;
        } else if (maid.isUnderWater() && mayBeStuckUnderWater(maid.blockPosition())) {
            // 被卡在水下时切换到水中寻路
            shouldSwitchToWater = true;
        } else if (maid.isInWater() && targetingUnderWater()) {
            // 目标在水下时切换到水中寻路
            shouldSwitchToWater = true;
        }

        if (shouldSwitchToWater && switchToNavigation(Mode.WATER, waterNavigation)) {
            // 设置游泳状态
            setSwimmingState(true, false);
        }
    }

    /**
     * 处理水中模式下的导航逻辑
     */
    private void handleWaterMode() {
        boolean shouldUseWater = (maid.isInWater() && targetingUnderWater())
                || (maid.isUnderWater() && mayBeStuckUnderWater(maid.blockPosition()));
        BlockPos endPos = getEndPos(waterNavigation);

        if (!shouldUseWater && endPos != null) {
            if (!shouldStartOrStopSwim(2)) {
                handleWaterPathEnd(endPos);
            } else if (!maid.isInWater()) {
                // 女仆已上岸，切换到地面寻路
                switchToGroundNavigation();
            } else if (!maid.isUnderWater()) {
                // 女仆半身入水，取消游泳状态
                setSwimmingState(false, false);
            } else {
                // 女仆完全在水中，保持游泳状态并更新目标
                setSwimmingState(true, false);
                maid.getSwimManager().setSwimTarget(endPos);
            }
        } else if (endPos == null && maid.getSwimManager().isGoingToBreath()) {
            // 走到路径尽头且正在呼吸，取消游泳状态
            setSwimmingState(false, false);
        } else if (shouldUseWater) {
            // 满足游泳条件，保持游泳状态
            setSwimmingState(true, false);
            if (endPos != null) {
                maid.getSwimManager().setSwimTarget(endPos);
            }
        }
    }

    /**
     * 处理水中寻路路径尽头的逻辑
     */
    private void handleWaterPathEnd(BlockPos endPos) {
        if (!level.isWaterAt(endPos) && !level.isWaterAt(endPos.below())) {
            // 路径尽头是陆地，切换到地面寻路
            switchToGroundNavigation(true);
        } else if (isWaterSurface(endPos)) {
            // 路径尽头是水面，取消游泳状态
            setSwimmingState(false, false);
        } else {
            // 路径尽头仍在水中，保持游泳状态
            setSwimmingState(true, false);
        }
    }

    /**
     * 切换到地面寻路模式
     */
    private void switchToGroundNavigation() {
        switchToGroundNavigation(false);
    }

    /**
     * 切换到地面寻路模式并设置是否准备上岸
     */
    private void switchToGroundNavigation(boolean readyToLand) {
        if (switchToNavigation(Mode.GROUND, basicNavigation)) {
            setSwimmingState(false, readyToLand);
        }
    }

    /**
     * 设置游泳状态
     */
    private void setSwimmingState(boolean wantToSwim, boolean readyToLand) {
        maid.getSwimManager().setWantToSwim(wantToSwim);
        maid.getSwimManager().setReadyToLand(readyToLand);
    }

    /**
     * 判断目标是否在水下
     */
    private boolean targetingUnderWater() {
        if (!maid.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            return false;
        }
        BlockPos targetPos = maid.getBrain().getMemory(MemoryModuleType.WALK_TARGET)
                .get().getTarget().currentBlockPosition();
        return isUnderWater(targetPos);
    }

    /**
     * 切换导航模式
     */
    @SuppressWarnings("all")
    private boolean switchToNavigation(Mode mode, PathNavigation navigation) {
        PathNavigation currentNavigation = maid.getNavigation();
        if (!currentNavigation.isDone()) {
            Path path = navigation.createPath(currentNavigation.getPath().getEndNode().asBlockPos(), 0);
            if (path != null && path.canReach()) {
                if (navigation.moveTo(path, ((INavigationMixin) currentNavigation).touhouLittleMaid$GetSpeedModifier())) {
                    // 删除第一个寻路节点，有助于路径切换更加平滑
                    path.advance();
                    maid.setNavigation(navigation);
                    this.mode = mode;
                    currentNavigation.stop();
                    return true;
                }
            }
        } else {
            maid.setNavigation(navigation);
            navigation.stop();
            currentNavigation.stop();
            return true;
        }
        return false;
    }

    /**
     * 判断是否应该开始或停止游泳
     */
    private boolean shouldStartOrStopSwim(int minimumDistance) {
        Path path = maid.getNavigation().getPath();
        if (path == null || path.isDone() || path.getNextNodeIndex() > path.getNodeCount() - minimumDistance) {
            return false;
        }
        for (int i = path.getNextNodeIndex(), c = 0; c < minimumDistance; c++, i++) {
            if (!level.isWaterAt(path.getNode(i).asBlockPos())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断女仆是否可能被卡在水下（头顶方块）
     */
    private boolean mayBeStuckUnderWater(BlockPos pos) {
        return level.isWaterAt(pos) && !level.getBlockState(pos.above()).isPathfindable(PathComputationType.LAND);
    }

    /**
     * 判断目标位置是否在水下两格或更深
     */
    private boolean isUnderWater(BlockPos blockPos) {
        return level.isWaterAt(blockPos)
                && level.isWaterAt(blockPos.above())
                && level.isWaterAt(blockPos.above(2));
    }

    public PathNavigation getBasicNavigation() {
        return basicNavigation;
    }

    public PathNavigation getWaterNavigation() {
        return waterNavigation;
    }

    public boolean isWaterSurface(BlockPos pos) {
        // 向上两层（主人浮在水上的话 target 可能是 -1Y 的），向上一层（寻路规则）
        return (level.isWaterAt(pos) && level.getBlockState(pos.above()).isAir())
                || (level.isWaterAt(pos.below()) && level.getBlockState(pos).isAir())
                || (level.isWaterAt(pos.above()) && level.getBlockState(pos.above(2)).isAir());
    }

    @Nullable
    public BlockPos getEndPos(PathNavigation navigation) {
        if (navigation.getPath() == null || navigation.getPath().getEndNode() == null) {
            return null;
        }
        return navigation.getPath().getEndNode().asBlockPos();
    }

    public void resetNavigation() {
        maid.setNavigation(basicNavigation);
        basicNavigation.stop();
        waterNavigation.stop();
        setSwimmingState(false, false);
        mode = Mode.GROUND;
    }

    public enum Mode {
        GROUND,
        WATER
    }
}
