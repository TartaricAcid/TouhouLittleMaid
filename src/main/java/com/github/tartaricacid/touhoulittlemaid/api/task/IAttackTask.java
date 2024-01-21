package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

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
                mobs -> mobs.stream().filter(maid::canAttack)
                        .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                        .findFirst());
    }

    static boolean checkCanAttackEntity(LivingEntity target) {
        // 不能攻击玩家
        if (target instanceof PlayerEntity) {
            return false;
        }
        // 有主的宠物也不攻击
        if (target instanceof TameableEntity) {
            TameableEntity tamableAnimal = (TameableEntity) target;
            return tamableAnimal.getOwnerUUID() == null;
        }
        return true;
    }

    /**
     * 能否攻击该对象
     *
     * @param maid   女仆
     * @param target 攻击的目标
     * @return 能否攻击？
     */
    default boolean canAttack(EntityMaid maid, LivingEntity target) {
        if (maid.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) maid.getOwner();
            LivingEntity lastHurtByMob = player.getLastHurtByMob();
            if (target.equals(lastHurtByMob) && checkCanAttackEntity(lastHurtByMob)) {
                return true;
            }
            LivingEntity lastHurtMob = player.getLastHurtMob();
            if (target.equals(lastHurtMob) && checkCanAttackEntity(lastHurtMob)) {
                return true;
            }
        }
        LivingEntity maidLastHurtByMob = maid.getLastHurtByMob();
        if (target.equals(maidLastHurtByMob) && checkCanAttackEntity(maidLastHurtByMob)) {
            return true;
        }
        ResourceLocation key = ForgeRegistries.ENTITIES.getKey(target.getType());
        if (key != null && MaidConfig.MAID_ATTACK_IGNORE.get().contains(key.toString())) {
            return false;
        }
        return target instanceof IMob;
    }

    default boolean hasExtraAttack(EntityMaid maid, Entity target) {
        return false;
    }

    default boolean doExtraAttack(EntityMaid maid, Entity target) {
        return false;
    }
}
