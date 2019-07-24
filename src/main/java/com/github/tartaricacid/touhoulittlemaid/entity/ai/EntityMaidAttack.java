package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityMaidAttack extends EntityAIAttackMelee {
    private final AbstractEntityMaid entityMaid;

    public EntityMaidAttack(AbstractEntityMaid entityMaid, double speedIn, boolean longMemoryIn) {
        super(entityMaid, speedIn, longMemoryIn);
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.isSitting() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.isSitting() && super.shouldContinueExecuting();
    }
}
