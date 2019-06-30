package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;

import java.util.List;

public class EntityMaidPickup extends EntityAIBase {
    private EntityMaid entityMaid;
    private float speed;
    private Entity entityPickup;
    private int countTime;

    public EntityMaidPickup(EntityMaid entityMaid, float speed) {
        this.entityMaid = entityMaid;
        this.speed = speed;
        this.countTime = 10;
        setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        if (!entityMaid.isPickup() || entityMaid.isSitting()) {
            return false;
        }

        if (countTime > 0) {
            countTime--;
            return false;
        }

        countTime = 10;

        List<Entity> list = this.entityMaid.world.getEntitiesInAABBexcluding(entityMaid,
                this.entityMaid.getEntityBoundingBox().expand(8, 2, 8).expand(-8, -2, -8),
                EntityMaid.IS_PICKUP);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof EntityItem && entityMaid.canEntityBeSeen(entity)) {
                    this.entityPickup = entity;
                    if (entityMaid.canInsertSlot(((EntityItem) entity).getItem())) {
                        return true;
                    }
                }

                if (entity instanceof EntityXPOrb && entityMaid.canEntityBeSeen(entity)) {
                    entityPickup = entity;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateTask() {
        if (entityPickup != null) {
            entityMaid.getLookHelper().setLookPositionWithEntity(entityPickup, 30f, entityMaid.getVerticalFaceSpeed());
            entityMaid.getNavigator().tryMoveToXYZ(entityPickup.posX, entityPickup.posY, entityPickup.posZ, speed);
        }
    }

    @Override
    public void resetTask() {
        entityPickup = null;
        entityMaid.getNavigator().clearPath();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entityPickup != null && entityPickup.isEntityAlive() && !entityMaid.isSitting() && entityMaid.isPickup()
                && ((entityPickup instanceof EntityItem && entityMaid.canInsertSlot(((EntityItem) entityPickup).getItem()))
                || (entityPickup instanceof EntityXPOrb))
                && entityMaid.canEntityBeSeen(entityPickup);
    }
}
