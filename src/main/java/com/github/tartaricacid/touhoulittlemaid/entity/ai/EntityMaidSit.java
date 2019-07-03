package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityMaidSit extends EntityAIBase {
    private final EntityMaid entityMaid;

    public EntityMaidSit(EntityMaid entityMaid) {
        this.entityMaid = entityMaid;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {
        return this.entityMaid.isTamed() && this.entityMaid.isSitting() && !this.entityMaid.isInWater() && !this.entityMaid.onGround;
    }

    @Override
    public void startExecuting() {
        this.entityMaid.getNavigator().clearPath();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.entityMaid.isSitting();
    }

    @Override
    public void resetTask() {
        this.entityMaid.setSitting(false);
    }
}
