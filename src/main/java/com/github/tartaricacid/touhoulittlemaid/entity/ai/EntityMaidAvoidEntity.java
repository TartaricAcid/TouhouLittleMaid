package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;

/**
 * @author TartaricAcid
 * @date 2019/9/10 18:43
 **/
public class EntityMaidAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T> {
    public EntityMaidAvoidEntity(EntityCreature entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public EntityMaidAvoidEntity(EntityCreature entityIn, Class<T> classToAvoidIn, Predicate<? super T> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidTargetSelectorIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean shouldExecute() {
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            if (maid.getTask().isAttack() || maid.isSleep()) {
                return false;
            }
        }
        return super.shouldExecute();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            if (maid.isSitting()) {
                maid.setSitting(false);
            }
            if (maid.getRidingEntity() != null) {
                maid.dismountRidingEntity();
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            if (maid.getTask().isAttack() || maid.isSleep()) {
                return false;
            }
        }
        return super.shouldContinueExecuting();
    }
}
