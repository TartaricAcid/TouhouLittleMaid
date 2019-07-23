package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityMaidAttack extends EntityAIAttackMelee {
    private final EntityMaid entityMaid;

    public EntityMaidAttack(EntityMaid entityMaid, double speedIn, boolean longMemoryIn) {
        super(entityMaid, speedIn, longMemoryIn);
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
