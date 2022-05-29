package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public class MaidPanicTask extends Behavior<EntityMaid> {
    public MaidPanicTask() {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_HOSTILE, MemoryStatus.REGISTERED,
                MemoryModuleType.HURT_BY, MemoryStatus.REGISTERED));
    }

    public static boolean hasHostile(EntityMaid maid) {
        return maid.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
    }

    public static boolean isHurt(EntityMaid maid) {
        return maid.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }

    public static boolean isAttack(EntityMaid maid) {
        return maid.getTask() instanceof IAttackTask;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        boolean hurtOrHostile = isHurt(maid) || hasHostile(maid);
        if (!isAttack(maid) && hurtOrHostile) {
            Brain<?> brain = maid.getBrain();
            if (!brain.isActive(Activity.PANIC)) {
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            }
            brain.setActiveActivityIfPossible(Activity.PANIC);
        }
    }
}
