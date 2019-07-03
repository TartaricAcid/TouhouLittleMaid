package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIFollowOwner;

public class EntityMaidFollowOwner extends EntityAIFollowOwner {
    private EntityMaid entityMaid;

    public EntityMaidFollowOwner(EntityMaid entityMaid, double followSpeedIn, float minDistIn, float maxDistIn) {
        super(entityMaid, followSpeedIn, minDistIn, maxDistIn);
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.isHome() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.isHome() && super.shouldContinueExecuting();
    }
}
