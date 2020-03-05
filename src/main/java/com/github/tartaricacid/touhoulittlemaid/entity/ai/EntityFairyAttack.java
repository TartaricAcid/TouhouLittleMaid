package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;

/**
 * @author TartaricAcid
 * @date 2019/9/12 15:06
 **/
public class EntityFairyAttack extends EntityAIBase {
    private EntityFairy entityFairy;
    private double minDistance;
    private double speedIn;
    private Path path;
    private int withInRangeTime;

    public EntityFairyAttack(EntityFairy entityFairy, double minDistance, double speedIn) {
        this.entityFairy = entityFairy;
        this.minDistance = minDistance;
        this.speedIn = speedIn;
        this.setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = this.entityFairy.getAttackTarget();
        if (target == null || !target.isEntityAlive()) {
            return false;
        }
        this.path = this.entityFairy.getNavigator().getPathToEntityLiving(target);
        return path != null;
    }

    @Override
    public void startExecuting() {
        this.entityFairy.getNavigator().setPath(this.path, this.speedIn);
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.entityFairy.getAttackTarget();
        if (target == null || !target.isEntityAlive()) {
            return;
        }
        this.entityFairy.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        double distance = this.entityFairy.getDistance(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        if (this.entityFairy.getEntitySenses().canSee(target) && distance >= minDistance) {
            this.entityFairy.getNavigator().tryMoveToEntityLiving(target, this.speedIn);
            withInRangeTime = 0;
        } else if (distance < minDistance) {
            this.entityFairy.getNavigator().clearPath();
            withInRangeTime++;
            entityFairy.motionY = 0;
            entityFairy.setNoGravity(true);
            if (withInRangeTime > 20) {
                float percent = (float) (distance / minDistance);
                entityFairy.attackEntityWithRangedAttack(target, 1 - percent);
                withInRangeTime = 0;
            }
        } else {
            withInRangeTime = 0;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase target = this.entityFairy.getAttackTarget();
        if (target == null || !target.isEntityAlive()) {
            return false;
        } else {
            boolean isPlayerAndCanNotBeAttacked = target instanceof EntityPlayer
                    && (((EntityPlayer) target).isSpectator() || ((EntityPlayer) target).isCreative());
            return !isPlayerAndCanNotBeAttacked;
        }
    }

    @Override
    public void resetTask() {
        EntityLivingBase target = this.entityFairy.getAttackTarget();
        boolean isPlayerAndCanNotBeAttacked = target instanceof EntityPlayer
                && (((EntityPlayer) target).isSpectator() || ((EntityPlayer) target).isCreative());
        if (isPlayerAndCanNotBeAttacked) {
            this.entityFairy.setAttackTarget(null);
        }
        this.entityFairy.getNavigator().clearPath();
        withInRangeTime = 0;
    }
}
