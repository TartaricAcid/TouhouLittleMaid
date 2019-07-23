package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidMode;
import net.minecraft.entity.ai.EntityAIAttackMelee;

public class EntityMaidAttack extends EntityAIAttackMelee {
    private final EntityMaid entityMaid;
    private int raiseArmTicks;

    public EntityMaidAttack(EntityMaid entityMaid, double speedIn, boolean longMemoryIn) {
        super(entityMaid, speedIn, longMemoryIn);
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        return !entityMaid.guiOpening && entityMaid.getMode() == MaidMode.ATTACK && !entityMaid.isSitting() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.guiOpening && entityMaid.getMode() == MaidMode.ATTACK && !entityMaid.isSitting() && super.shouldContinueExecuting();
    }
}
