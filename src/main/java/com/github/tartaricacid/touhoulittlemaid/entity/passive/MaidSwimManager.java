package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

public class MaidSwimManager {
    /**
     * 游泳碰撞箱
     */
    private static final EntityDimensions SWIMMING_DIMENSIONS = EntityDimensions.scalable(0.6F, 0.6F);
    private final EntityMaid maid;
    /**
     * 正在食用可提供水下呼吸的食物标志位
     */
    private boolean isEatBreatheItem = false;
    /**
     * 主动游泳标志位
     */
    private boolean wantToSwim = false;

    /**
     * 游泳目标点，控制视角用
     */
    private BlockPos swimTarget = null;
    /**
     * 是否已经准备登陆，登陆时基于额外的加速度
     */
    private boolean readyToLand = false;
    /**
     * 是否准备前去呼吸，用于屏蔽距离传送
     */
    private boolean isGoingToBreath = false;


    public MaidSwimManager(EntityMaid maid) {
        this.maid = maid;
        maid.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    /**
     * 重置状态，从而控制饮用药水
     */
    public void resetEatBreatheItem() {
        if (this.isEatBreatheItem) {
            this.isEatBreatheItem = false;
        }
    }

    /**
     * 依据游泳状态，切换成游泳的寻路
     */
    public void updateSwimming() {
        if (!maid.level.isClientSide) {
            this.updatePose();
        }
    }

    /**
     * 更新游泳姿势同时更新碰撞箱
     */
    private void updatePose() {
        if (this.wantToSwim() && !maid.onGround()) {
            maid.setSwimming(true);
            maid.setPose(Pose.SWIMMING);
        } else {
            maid.setSwimming(false);
            // 也许有更好的方式?
            if (!maid.isSleeping()) {
                maid.setPose(Pose.STANDING);
            }
        }
    }

    public void setWantToSwim(boolean pSearchingForLand) {
        this.wantToSwim = pSearchingForLand;
    }

    public boolean wantToSwim() {
        return this.wantToSwim;
    }

    public boolean isEatBreatheItem() {
        return isEatBreatheItem;
    }

    public void setEatBreatheItem(boolean eatBreatheItem) {
        isEatBreatheItem = eatBreatheItem;
    }

    public EntityDimensions getSwimmingDimensions() {
        return SWIMMING_DIMENSIONS;
    }

    public void setSwimTarget(BlockPos pos) {
        this.swimTarget = pos;
    }

    @Nullable
    public BlockPos getSwimTarget() {
        if (!wantToSwim()) {
            return null;
        }
        return swimTarget;
    }

    public void setReadyToLand(boolean readyToLand) {
        this.readyToLand = readyToLand;
    }

    public boolean isReadyToLand() {
        return readyToLand;
    }

    public void setGoingToBreath(boolean goingToBreath) {
        isGoingToBreath = goingToBreath;
    }

    public boolean isGoingToBreath() {
        return isGoingToBreath;
    }
}
