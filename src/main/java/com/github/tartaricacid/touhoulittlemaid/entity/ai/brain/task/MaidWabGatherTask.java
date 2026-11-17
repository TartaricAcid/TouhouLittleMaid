package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabWorkJobs;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

/**
 * 玩家侧挖矿 / 伐木工作模式的 AI：定时检查这台女仆有没有活跃工作 job，没有就起一个。
 * 用 {@link MaidCheckRateTask} 做节流（本仓库验证过的写法），重活交给 {@link WabWorkJobs}。
 */
public class MaidWabGatherTask extends MaidCheckRateTask {
    private final String action;

    public MaidWabGatherTask(String action) {
        super(ImmutableMap.<MemoryModuleType<?>, MemoryStatus>of());
        this.action = action;
        this.setMaxCheckRate(40);
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
        WabWorkJobs.ensureJob(maid, action);
    }
}
