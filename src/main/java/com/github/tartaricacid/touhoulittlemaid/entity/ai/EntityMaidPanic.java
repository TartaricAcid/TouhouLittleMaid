package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.EntityAIPanic;

public class EntityMaidPanic extends EntityAIPanic {
    private EntityMaid entityMaid;

    public EntityMaidPanic(EntityMaid entityMaid, double speedIn) {
        super(entityMaid, speedIn);
        this.entityMaid = entityMaid;
    }

    /**
     * 处于攻击状态下不需要乱跑
     * 但是你弓兵模式下没箭了，就赶紧跑啊，等死呢
     */
    @Override
    public boolean shouldExecute() {
        // TODO：弓兵模式下没有箭的慌乱跑
        return !entityMaid.guiOpening && !entityMaid.getTask().isAttack() && super.shouldExecute();
    }

    /**
     * 处于攻击状态下不需要乱跑
     * 但是你弓兵模式下没箭了，就赶紧跑啊，等死呢
     */
    @Override
    public boolean shouldContinueExecuting() {
        // TODO：弓兵模式下没有箭的慌乱跑
        return !entityMaid.guiOpening && !entityMaid.getTask().isAttack() && super.shouldContinueExecuting();
    }
}
