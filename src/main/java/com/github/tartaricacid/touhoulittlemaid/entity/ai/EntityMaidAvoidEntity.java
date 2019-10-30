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
public class EntityMaidAvoidEntity<T extends Entity> extends EntityAIAvoidEntity {
    @SuppressWarnings("unchecked")
    public EntityMaidAvoidEntity(EntityCreature entityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @SuppressWarnings("unchecked")
    public EntityMaidAvoidEntity(EntityCreature entityIn, Class classToAvoidIn, Predicate avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        super(entityIn, classToAvoidIn, avoidTargetSelectorIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    @Override
    public boolean shouldExecute() {
        if (entity instanceof EntityMaid) {
            boolean isAttack = ((EntityMaid) entity).getTask().isAttack();
            boolean isSitting = ((EntityMaid) entity).isSitting();
            if (isAttack || isSitting) {
                return false;
            }
        }
        return super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (entity instanceof EntityMaid) {
            boolean isAttack = ((EntityMaid) entity).getTask().isAttack();
            boolean isSitting = ((EntityMaid) entity).isSitting();
            if (isAttack || isSitting) {
                return false;
            }
        }
        return super.shouldContinueExecuting();
    }
}
