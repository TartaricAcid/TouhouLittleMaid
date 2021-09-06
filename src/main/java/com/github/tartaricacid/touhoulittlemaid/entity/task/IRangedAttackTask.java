package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.LivingEntity;

public interface IRangedAttackTask extends IAttackTask {
    /**
     * 执行射击动作
     *
     * @param shooter        射击者
     * @param target         射击目标
     * @param distanceFactor 距离因素，即弓箭的蓄力值
     */
    void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor);
}
