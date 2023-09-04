package com.github.tartaricacid.touhoulittlemaid.entity.ai.goal;

import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FairyAttackGoal extends Goal {
    private static final int MAX_WITH_IN_RANGE_TIME = 20;
    private final EntityFairy entityFairy;
    private final double minDistance;
    private final double speedIn;
    private Path path;
    private int withInRangeTime;

    public FairyAttackGoal(EntityFairy entityFairy, double minDistance, double speedIn) {
        this.entityFairy = entityFairy;
        this.minDistance = minDistance;
        this.speedIn = speedIn;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entityFairy.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        this.path = this.entityFairy.getNavigation().createPath(target, 0);
        return path != null;
    }

    @Override
    public void start() {
        this.entityFairy.getNavigation().moveTo(this.path, this.speedIn);
    }

    @Override
    public void tick() {
        LivingEntity target = this.entityFairy.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }
        this.entityFairy.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distance = this.entityFairy.distanceTo(target);
        if (this.entityFairy.getSensing().hasLineOfSight(target) && distance >= minDistance) {
            this.entityFairy.getNavigation().moveTo(target, this.speedIn);
            withInRangeTime = 0;
        } else if (distance < minDistance) {
            this.entityFairy.getNavigation().stop();
            withInRangeTime++;
            Vec3 motion = entityFairy.getDeltaMovement();
            entityFairy.setDeltaMovement(motion.x, 0, motion.z);
            entityFairy.setNoGravity(true);
            if (withInRangeTime > MAX_WITH_IN_RANGE_TIME) {
                float percent = (float) (distance / minDistance);
                entityFairy.performRangedAttack(target, 1 - percent);
                withInRangeTime = 0;
            }
        } else {
            withInRangeTime = 0;
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.entityFairy.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        } else {
            boolean isPlayerAndCanNotBeAttacked = target instanceof Player
                    && (target.isSpectator() || ((Player) target).isCreative());
            return !isPlayerAndCanNotBeAttacked;
        }
    }

    @Override
    public void stop() {
        LivingEntity target = this.entityFairy.getTarget();
        boolean isPlayerAndCanNotBeAttacked = target instanceof Player
                && (target.isSpectator() || ((Player) target).isCreative());
        if (isPlayerAndCanNotBeAttacked) {
            this.entityFairy.setTarget(null);
        }
        this.entityFairy.getNavigation().stop();
        withInRangeTime = 0;
    }
}
