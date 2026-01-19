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
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Optional;

public class MaidTridentTargetTask extends Behavior<EntityMaid> {
    /**
     * 一秒的间隔使用时间，模拟忠诚后的三叉戟击中目标返回来所需要的时间，不然就太 IMBA 了...
     *
     * @deprecated 该字段已弃用，现在已经使用属性 {@link InitAttribute#MAID_TRIDENT_COOLDOWN} 来调整攻击间隔
     */
    @Deprecated(since = "1.4.7")
    private final int attackCooldown = 20;

    private int attackTime = -1;
    private int seeTime;

    public MaidTridentTargetTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Optional<LivingEntity> memory = owner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if (memory.isPresent()) {
            LivingEntity target = memory.get();
            return hasTrident(owner) && owner.canSee(target);
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

                    // 物品最大使用计数大于 30 才可以
                    // 如果有引雷，必须 6 格之外（安全范围，以免波及自身）
                    boolean hasChanneling = EnchantmentHelper.hasChanneling(owner.getMainHandItem());
                    boolean canUseChanneling = owner.level.isThundering() && !owner.isUnderWater() && hasChanneling;
                    boolean tooClose = owner.closerThan(target, 6);
                    // 引雷附魔在生物处于雷雨处且不在水中时会触发，需要保证安全距离
                    boolean inSafeArea = !(canUseChanneling && tooClose);
                    if (ticksUsingItem >= 30 && inSafeArea) {
                        owner.stopUsingItem();
                        owner.performRangedAttack(target, 0);
                        // 通过属性获取攻击间隔时间
                        AttributeInstance attributeInstance = owner.getAttribute(InitAttribute.MAID_TRIDENT_COOLDOWN.get());
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

    private boolean hasTrident(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof TridentItem;
    }
}
