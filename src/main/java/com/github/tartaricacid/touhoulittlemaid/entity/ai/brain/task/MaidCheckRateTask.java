package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public abstract class MaidCheckRateTask extends Behavior<EntityMaid> {
    private int maxCheckRate = 20;
    private int nextCheckTickCount;

    public MaidCheckRateTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn) {
        super(requiredMemoryStateIn);
    }

    public MaidCheckRateTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn, int duration) {
        super(requiredMemoryStateIn, duration);
    }

    public MaidCheckRateTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn, int durationMinIn, int durationMaxIn) {
        super(requiredMemoryStateIn, durationMinIn, durationMaxIn);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        if (this.nextCheckTickCount > 0) {
            --this.nextCheckTickCount;
            return false;
        }
        this.nextCheckTickCount = maxCheckRate + owner.getRandom().nextInt(maxCheckRate);
        return true;
    }

    protected void setMaxCheckRate(int maxCheckRate) {
        this.maxCheckRate = maxCheckRate;
    }

    protected void setNextCheckTickCount(int nextCheckTickCount) {
        this.nextCheckTickCount = nextCheckTickCount;
    }
}
