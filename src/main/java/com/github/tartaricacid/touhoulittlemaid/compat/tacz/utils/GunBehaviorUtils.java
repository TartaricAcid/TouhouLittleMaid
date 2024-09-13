package com.github.tartaricacid.touhoulittlemaid.compat.tacz.utils;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.List;
import java.util.Optional;

public class GunBehaviorUtils {

    //可见性校验工具，来自于Sensor
    private static final TargetingConditions TARGET_CONDITIONS = TargetingConditions.forNonCombat().range(64.0D);

    //可见性方法，来自于Sensor类
    public static boolean canSee(LivingEntity pLivingEntity, LivingEntity pTarget) {
        return TARGET_CONDITIONS.test(pLivingEntity, pTarget);
    }

    //寻找第一个可见目标，使用独立的方法，区别于IAttackTask
    public static Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        if (maid.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).isPresent()) {
            List<LivingEntity> list = maid.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).get();
            return list.stream().filter((e) -> maid.canAttack(e) && maid.isWithinRestriction(e.blockPosition()) &&
                    GunBehaviorUtils.canSee(maid, e)).findAny();
        }
        return Optional.empty();
    }


}
