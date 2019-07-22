package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;

public class EntityMaidWanderAvoidWater extends EntityAIWanderAvoidWater {
    private EntityMaid entityMaid;

    public EntityMaidWanderAvoidWater(EntityMaid entityMaid, double speed) {
        super(entityMaid, speed);
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.guiOpening && !entityMaid.isSitting() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.guiOpening && !entityMaid.isSitting() && super.shouldContinueExecuting();
    }
}
