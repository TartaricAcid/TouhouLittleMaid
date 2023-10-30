package com.github.tartaricacid.touhoulittlemaid.entity.ai.goal;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MaidTemptGoal extends Goal {
    private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10).ignoreLineOfSight();
    private final TargetingConditions targetingConditions;
    protected final PathfinderMob mob;
    private final double speedModifier;
    private double px;
    private double py;
    private double pz;
    @Nullable
    protected EntityMaid maid;
    private int calmDown;
    private boolean isRunning;
    private final Ingredient items;
    private final boolean canScare;

    public MaidTemptGoal(PathfinderMob mob, double speedModifier, Ingredient items, boolean canScare) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.items = items;
        this.canScare = canScare;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
    }

    public boolean canUse() {
        if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        } else {
            this.maid = this.mob.level.getNearestEntity(EntityMaid.class, this.targetingConditions, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(10));
            return this.maid != null;
        }
    }

    private boolean shouldFollow(LivingEntity livingEntity) {
        return this.items.test(livingEntity.getMainHandItem()) || this.items.test(livingEntity.getOffhandItem());
    }

    public boolean canContinueToUse() {
        if (this.canScare()) {
            if (this.mob.distanceToSqr(this.maid) < 36) {
                if (this.maid.distanceToSqr(this.px, this.py, this.pz) > 0.01) {
                    return false;
                }
            } else {
                this.px = this.maid.getX();
                this.py = this.maid.getY();
                this.pz = this.maid.getZ();
            }
        }
        return this.canUse();
    }

    protected boolean canScare() {
        return this.canScare;
    }

    public void start() {
        this.px = this.maid.getX();
        this.py = this.maid.getY();
        this.pz = this.maid.getZ();
        this.isRunning = true;
    }

    public void stop() {
        this.maid = null;
        this.mob.getNavigation().stop();
        this.calmDown = reducedTickDelay(100);
        this.isRunning = false;
    }


    public void tick() {
        this.mob.getLookControl().setLookAt(this.maid, this.mob.getMaxHeadYRot() + 20, this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.maid) < 6.25) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.maid, this.speedModifier);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
