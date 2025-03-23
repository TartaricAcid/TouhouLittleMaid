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
            if (mode != Mode.WATER && maid.isInWater() && shouldStartOrStopSwim(5)) {
                // 对于一般寻路，当满足：女仆接触到水，前方有长水面时，切换到水中寻路
                if (switchToNavigation(Mode.WATER, waterNavigation)) {
                    maid.getSwimManager().setWantToSwim(true);
                    maid.getSwimManager().setReadyToLand(false);
                }
            } else if (mode != Mode.WATER && maid.isUnderWater() && mayBeStuckUnderWater(maid.blockPosition())) {
                // 如果女仆发现其所在位置不能上浮，那么也应该进入游泳状态（顶着头呆呆的）
                if (switchToNavigation(Mode.WATER, waterNavigation)) {
                    maid.getSwimManager().setWantToSwim(true);
                    maid.getSwimManager().setReadyToLand(false);
                }
            } else if (mode != Mode.WATER && maid.isInWater() && targetingUnderWater()) {
                // 如果目标在水下，那么女仆显然也应该进行水下寻路
                if (switchToNavigation(Mode.WATER, waterNavigation)) {
                    maid.getSwimManager().setWantToSwim(true);
                    maid.getSwimManager().setReadyToLand(false);
                }
            } else if (mode == Mode.WATER) {
                // 女仆当前正在使用水下寻路（不保证游泳的状态）
                // 如果满足使用水下寻路的附加条件，则不进行下面的判断（目标水下或者无法上浮），防止状态之间的闪烁
                boolean shouldUseWater = (maid.isInWater() && targetingUnderWater())
                                         || (maid.isUnderWater() && mayBeStuckUnderWater(maid.blockPosition()));
                // 要判断出水，需要当前存在路径
                BlockPos endPos = getEndPos(waterNavigation);
                if (!shouldUseWater && endPos != null) {
                    if (!shouldStartOrStopSwim(2)) {
                        // 即将走到水中寻路的尽头，你的女仆是否还需要游泳呢？
                        if (!level.isWaterAt(endPos) && !level.isWaterAt(endPos.below())) {
                            // a：如果最终女仆是要上岸的，那么这时就没必要继续游泳了。立刻停止并切换到常规模式
                            if (switchToNavigation(Mode.GROUND, basicNavigation)) {
                                maid.getSwimManager().setReadyToLand(true);
                                maid.getSwimManager().setWantToSwim(false);
                            }
                        } else if (isWaterSurface(endPos)) {
                            // b：女仆最终来到了水面上
                            maid.getSwimManager().setWantToSwim(false);
                            maid.getSwimManager().setReadyToLand(false);
                        } else {
                            // c：仅仅是走到头了（
                            maid.getSwimManager().setWantToSwim(true);
                            maid.getSwimManager().setReadyToLand(false);
                        }
                    } else if (!maid.isInWater()) {
                        // b：女仆上岸了，立刻切换到常规寻路
                        if (switchToNavigation(Mode.GROUND, basicNavigation)) {
                            maid.getSwimManager().setWantToSwim(false);
                            maid.getSwimManager().setReadyToLand(false);
                        }
                    } else if (!maid.isUnderWater()) {
                        // 女仆半身入水（那貌似游泳就不大礼貌了）
                        maid.getSwimManager().setWantToSwim(false);
                    } else {
                        maid.getSwimManager().setWantToSwim(true);
                        maid.getSwimManager().setSwimTarget(endPos);
                    }
                } else if (endPos == null && maid.getSwimManager().isGoingToBreath()) {
                    // 有一种走完路径的特殊情况：女仆是想要去呼吸的。此时依然走的是水中寻路，但是应该取消游泳状态
                    maid.getSwimManager().setWantToSwim(false);
                } else if (shouldUseWater) {
                    // 当前满足游泳的条件（见上）
                    maid.getSwimManager().setWantToSwim(true);
                    // 没有走完路径，更新终点
                    if (endPos != null) {
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

    private boolean targetingUnderWater() {
        // 判断 Target 是否在水下
        if (!maid.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            return false;
        }
        return isUnderWater(maid
                .getBrain()
                .getMemory(MemoryModuleType.WALK_TARGET)
                .get()
                .getTarget()
                .currentBlockPosition()
        );
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

    /**
     * 判断女仆是否可能被卡在水下（头顶方块）
     * 即判断在水中的女仆头顶有没有方块
     */
    private boolean mayBeStuckUnderWater(BlockPos pos) {
        return level.isWaterAt(pos) && !level.getBlockState(pos.above()).isPathfindable(level, pos, PathComputationType.LAND);
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

    /**
     * 判断目标位置是否两格或更深
     */
    private boolean isUnderWater(BlockPos blockPos) {
        return level.isWaterAt(blockPos)
               && level.isWaterAt(blockPos.above())
               && level.isWaterAt(blockPos.above(2));
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

    public enum Mode {
        GROUND,
        WATER
    }
}
