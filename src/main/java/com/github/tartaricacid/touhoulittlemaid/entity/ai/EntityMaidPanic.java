package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidMode;
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
        boolean notExecutedMode = entityMaid.getMode() == MaidMode.ATTACK || entityMaid.getMode() == MaidMode.RANGE_ATTACK || entityMaid.getMode() == MaidMode.DANMAKU_ATTACK;
        // TODO：弓兵模式下没有箭的慌乱跑
        return !entityMaid.guiOpening && !notExecutedMode && super.shouldExecute();
    }

    /**
     * 处于攻击状态下不需要乱跑
     * 但是你弓兵模式下没箭了，就赶紧跑啊，等死呢
     */
    @Override
    public boolean shouldContinueExecuting() {
        boolean notExecutedMode = entityMaid.getMode() == MaidMode.ATTACK || entityMaid.getMode() == MaidMode.RANGE_ATTACK || entityMaid.getMode() == MaidMode.DANMAKU_ATTACK;
        // TODO：弓兵模式下没有箭的慌乱跑
        return !entityMaid.guiOpening && !notExecutedMode && super.shouldContinueExecuting();
    }
}
