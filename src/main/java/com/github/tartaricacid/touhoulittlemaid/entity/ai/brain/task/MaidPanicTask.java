package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IAttackTask;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

public class MaidPanicTask extends Task<EntityMaid> {
    public MaidPanicTask() {
        super(ImmutableMap.of());
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
    protected boolean canStillUse(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        return isHurt(maid) || hasHostile(maid);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        boolean hurtOrHostile = isHurt(maid) || hasHostile(maid);
        if (!isAttack(maid) && hurtOrHostile) {
            Brain<?> brain = maid.getBrain();
            if (!brain.isActive(Activity.PANIC)) {
                // todo: 待确定这些参数是否有用
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            }
            brain.setActiveActivityIfPossible(Activity.PANIC);
        }
    }
}
