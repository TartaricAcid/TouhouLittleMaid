package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitAttribute;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Optional;

public class MaidShootTargetTask extends Behavior<EntityMaid> {
    /**
     * @deprecated 该字段已弃用，现在已经使用属性 {@link InitAttribute#MAID_SHOOT_COOLDOWN} 来调整攻击间隔
     */
    @Deprecated(since = "1.4.7")
    private final int attackCooldown = 2;

    private int attackTime = -1;
    private int seeTime;

    public MaidShootTargetTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    /**
     * @deprecated 现在已经使用属性 {@link InitAttribute#MAID_SHOOT_COOLDOWN} 来调整攻击间隔
     */
    @Deprecated(since = "1.4.7")
    public MaidShootTargetTask(int attackCooldown) {
        this();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Optional<LivingEntity> memory = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (memory.isPresent()) {
            LivingEntity target = memory.get();
            return owner.isHolding(item -> item.getItem() instanceof ProjectileWeaponItem)
                   && owner.canSee(target);
        }
        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        return entityIn.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(worldIn, entityIn);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        entityIn.setSwingingArms(true);
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid owner, long gameTime) {
        owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((target) -> {
            // 强行看见并朝向
            owner.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
            boolean canSee = owner.canSee(target);
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
                    // 拿到快速射击附魔的等级
                    int level = owner.getMainHandItem().getEnchantmentLevel(Enchantments.QUICK_CHARGE);

                    // 物品最大使用计数大于 20 才可以
                    // 这里大致解释下计数的意思，也就是蓄力，蓄力越长自然射的越远
                    // 只有蓄力超过 1 秒才会进行发射
                    // 当有快速射击附魔时，加快拉弓速度
                    if (level > 4 || ticksUsingItem >= (20 - level * 5)) {
                        owner.stopUsingItem();
                        int powerTime = Math.max(ticksUsingItem, 20);
                        owner.performRangedAttack(target, BowItem.getPowerForTime(powerTime));
                        // 依据属性调整攻击间隔
                        AttributeInstance attributeInstance = owner.getAttribute(InitAttribute.MAID_SHOOT_COOLDOWN.get());
                        if (attributeInstance != null) {
                            this.attackTime = (int) attributeInstance.getValue();
                        } else {
                            this.attackTime = this.attackCooldown;
                        }
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                // 非激活状态，但是时长合适，开始激活手部
                owner.startUsingItem(InteractionHand.MAIN_HAND);
            }
        });
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        this.seeTime = 0;
        this.attackTime = -1;
        entityIn.stopUsingItem();
    }
}
