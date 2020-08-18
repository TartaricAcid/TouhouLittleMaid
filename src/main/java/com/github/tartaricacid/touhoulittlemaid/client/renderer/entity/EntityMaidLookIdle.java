package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAILookIdle;

public class EntityMaidLookIdle extends EntityAILookIdle {
    private final EntityMaid maid;

    public EntityMaidLookIdle(EntityMaid maid) {
        super(maid);
        this.maid = maid;
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !maid.isSleep();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting() && !maid.isSleep();
    }
}
