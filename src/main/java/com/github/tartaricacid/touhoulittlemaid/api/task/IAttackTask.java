package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;

import java.util.Optional;

public interface IAttackTask extends IMaidTask {
    /**
     * 寻找合适的第一个攻击目标
     *
     * @param maid 女仆
     * @return 合适的攻击目标
     */
    static Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).flatMap(
                mobs -> mobs.stream().filter(maid::canAttack).findFirst());
    }
}
