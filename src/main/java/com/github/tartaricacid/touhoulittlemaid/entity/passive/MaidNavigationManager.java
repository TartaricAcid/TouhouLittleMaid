package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.INavigationMixin;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.navigation.MaidPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

public class MaidNavigationManager {
    public enum Mode {
        GROUND,
        WATER
    }

    private final MaidPathNavigation basicNavigation;
    private final AmphibiousPathNavigation waterNavigation;
    private final EntityMaid maid;
    private final Level level;
    private Mode mode = Mode.GROUND;

    public MaidNavigationManager(EntityMaid maid) {
        this.maid = maid;
        this.level = maid.level;
        this.basicNavigation = new MaidPathNavigation(maid, maid.level);
        this.waterNavigation = new AmphibiousPathNavigation(maid, maid.level);
        maid.setNavigation(basicNavigation);
    }

    public void tick() {
        if (!level.isClientSide && maid.isEffectiveAi()) {
            // 对于一般寻路，当满足：女仆接触到水，前方有长水面时，切换到水中寻路
            if (mode != Mode.WATER && maid.isInWater() && shouldStartOrStopSwim(5)) {
                if (switchToNavigation(Mode.WATER, waterNavigation)) {
                    maid.getSwimManager().setWantToSwim(true);
                    maid.getSwimManager().setReadyToLand(false);
                }
            } else if (mode == Mode.WATER) {
                BlockPos endPos = getEndPos(waterNavigation);
                if (endPos != null) {
                    if (!shouldStartOrStopSwim(2)) {
                        // 即将走到水中寻路的尽头，你的女仆是否还需要游泳呢？
                        // a：如果最终女仆是要上岸的，那么这时就没必要继续游泳了。立刻停止并切换到常规模式
                        if (!level.isWaterAt(endPos)) {
                            if (switchToNavigation(Mode.GROUND, basicNavigation)) {
                                maid.getSwimManager().setReadyToLand(true);
                                maid.getSwimManager().setWantToSwim(false);
                            }
                        } else if (isWaterSurface(endPos)) {
                            maid.getSwimManager().setWantToSwim(false);
                            maid.getSwimManager().setReadyToLand(false);
                        } else {
                            maid.getSwimManager().setWantToSwim(true);
                            maid.getSwimManager().setReadyToLand(false);
                        }
                    } else if (!maid.isInWater()) {
                        // b：女仆上岸了，立刻切换到常规寻路
                        if (switchToNavigation(Mode.GROUND, basicNavigation)) {
                            maid.getSwimManager().setWantToSwim(false);
                            maid.getSwimManager().setReadyToLand(false);
                        }
                    } else {
                        maid.getSwimManager().setWantToSwim(true);
                        maid.getSwimManager().setSwimTarget(endPos);
                    }
                }
            }
        }
        // 其他情况，如女仆进行一次传送，可能导致寻路中断，因此需要重新设置女仆是否要游泳
        if (mode != Mode.WATER) {
            maid.getSwimManager().setWantToSwim(false);
        }
    }

    @SuppressWarnings("all")
    private boolean switchToNavigation(Mode mode, PathNavigation navigation) {
        PathNavigation currentNavigation = maid.getNavigation();
        if (!currentNavigation.isDone()) {
            Path path = navigation.createPath(currentNavigation.getPath().getEndNode().asBlockPos(), 0);
            if (path != null && path.canReach()) {
                if (navigation.moveTo(path, ((INavigationMixin) currentNavigation).touhouLittleMaid$GetSpeedModifier())) {
                    // 删除第一个寻路节点，有助于路径切换更加平滑（第一个巡路点的 center 可能会出现在身后）
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

    public PathNavigation getBasicNavigation() {
        return basicNavigation;
    }

    public PathNavigation getWaterNavigation() {
        return waterNavigation;
    }

    public boolean isWaterSurface(BlockPos pos) {
        // 向上两层（主人浮在水上的话 target 可能是 -1Y 的）
        return (level.isWaterAt(pos) && level.getBlockState(pos.above()).isAir())
               || (level.isWaterAt(pos.above()) && level.getBlockState(pos.above(2)).isAir());
    }

    @Nullable
    public BlockPos getEndPos(PathNavigation navigation) {
        if (navigation.getPath() == null) {
            return null;
        }
        if (navigation.getPath().getEndNode() == null) {
            return null;
        }
        return navigation.getPath().getEndNode().asBlockPos();
    }

    public void resetNavigation() {
        maid.setNavigation(basicNavigation);
        basicNavigation.stop();
        waterNavigation.stop();
        maid.getSwimManager().setWantToSwim(false);
        maid.getSwimManager().setReadyToLand(false);
        mode = Mode.GROUND;
    }
}
