package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.BowItem;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class MaidShootTargetTask extends Task<EntityMaid> {
    private final int attackCooldown;
    private int attackTime = -1;
    private int seeTime;

    public MaidShootTargetTask(int attackCooldown) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT), 1200);
        this.attackCooldown = attackCooldown;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        Optional<LivingEntity> memory = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (memory.isPresent()) {
            LivingEntity target = memory.get();
            return owner.isHolding(item -> item instanceof ShootableItem)
                    && BrainUtil.canSee(owner, target)
                    && BrainUtil.isWithinAttackRange(owner, target, 0);
        }
        return false;
    }

    @Override
    protected boolean canStillUse(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        return entityIn.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.setSwingingArms(true);
    }

    @Override
    protected void tick(ServerWorld worldIn, EntityMaid owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            boolean canSee = BrainUtil.canSee(owner, target);
            boolean seeTimeMoreThanZero = this.seeTime > 0;

            // 如果两者不一致，重置看见时间
            if (canSee != seeTimeMoreThanZero) {
                this.seeTime = 0;
            }
            // 如果看见了对方，增加看见时间，否则减少
            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            // 如果实体手部处于激活状态
            if (owner.isUsingItem()) {
                // 如果看不见对方超时 60，重置激活状态
                if (!canSee && this.seeTime < -60) {
                    owner.stopUsingItem();
                } else if (canSee) {
                    // 否则开始进行远程攻击
                    int ticksUsingItem = owner.getTicksUsingItem();

                    // 物品最大使用计数大于 20 才可以
                    // 这里大致解释下计数的意思，也就是蓄力，蓄力越长自然射的越远
                    // 只有蓄力超过 1 秒才会进行发射
                    if (ticksUsingItem >= 20) {
                        owner.stopUsingItem();
                        owner.performRangedAttack(target, BowItem.getPowerForTime(ticksUsingItem));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                // 非激活状态，但是时长合适，开始激活手部
                owner.startUsingItem(Hand.MAIN_HAND);
            }
        });
    }

    @Override
    protected void stop(ServerWorld worldIn, EntityMaid entityIn, long gameTimeIn) {
        this.seeTime = 0;
        this.attackTime = -1;
        entityIn.stopUsingItem();
    }
}
