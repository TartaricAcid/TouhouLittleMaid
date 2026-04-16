package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public interface IMultiSelectTask extends IMaidTask {

    /**
     * Check if the current task can be activated
     */
    boolean mayActivate(EntityMaid maid);

    default boolean compatibleWith(EntityMaid maid, IMaidTask task) {
        return true;
    }

    /**
     * Handle activation of this task, such as switching to the correct tool
     */
    void activate(EntityMaid maid);

    /**
     * Return if this task is current having nothing to do, given this task is activated
     */
    default boolean isIdling(EntityMaid maid) {
        if (!maid.getBrain().checkMemory(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)) return false;
        if (!maid.getBrain().checkMemory(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)) return false;
        if (!maid.getBrain().checkMemory(MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT)) return false;
        if (!maid.getBrain().checkMemory(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT)) return false;
        return true;
    }

    /**
     * Handler deactivation of this task, such as erasing memory
     */
    default void onStop(EntityMaid maid) {
    }

}
