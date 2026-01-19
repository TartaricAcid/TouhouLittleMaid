package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitAttribute;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

/**
 * 原版的 CrossbowAttack 调用了 BehaviorUtils.canSee，导致射击限制在 16 格内
 * <p>
 * 故略作修改，让其适配自定义距离
 */
public class MaidCrossbowAttack extends CrossbowAttack<EntityMaid, EntityMaid> {
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET)
                .map(target -> maid.isHolding(this::isCrossbow) && maid.canSee(target))
                .orElse(false);
    }

    private boolean isCrossbow(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    /**
     * 重写一遍父类的 crossbowAttack 方法，添加自己的冷却时间判断
     */
    @Override
    public void crossbowAttack(EntityMaid shooter, LivingEntity target) {
        if (this.crossbowState == CrossbowAttack.CrossbowState.UNCHARGED) {
            shooter.startUsingItem(ProjectileUtil.getWeaponHoldingHand(shooter, item -> item instanceof CrossbowItem));
            this.crossbowState = CrossbowAttack.CrossbowState.CHARGING;
            shooter.setChargingCrossbow(true);
        } else if (this.crossbowState == CrossbowAttack.CrossbowState.CHARGING) {
            if (!shooter.isUsingItem()) {
                this.crossbowState = CrossbowAttack.CrossbowState.UNCHARGED;
            }

            int useTicks = shooter.getTicksUsingItem();
            ItemStack crossbowStack = shooter.getUseItem();
            if (useTicks >= CrossbowItem.getChargeDuration(crossbowStack)) {
                shooter.releaseUsingItem();
                this.crossbowState = CrossbowAttack.CrossbowState.CHARGED;
                this.attackDelay = 20 + shooter.getRandom().nextInt(20);
                AttributeInstance attribute = shooter.getAttribute(InitAttribute.MAID_CROSSBOW_ATTACK_SPEED.get());
                if (attribute != null) {
                    this.attackDelay = (int) (this.attackDelay / attribute.getValue());
                }
                shooter.setChargingCrossbow(false);
            }
        } else if (this.crossbowState == CrossbowAttack.CrossbowState.CHARGED) {
            --this.attackDelay;
            if (this.attackDelay <= 0) {
                this.crossbowState = CrossbowAttack.CrossbowState.READY_TO_ATTACK;
            }
        } else if (this.crossbowState == CrossbowAttack.CrossbowState.READY_TO_ATTACK) {
            shooter.performRangedAttack(target, 1.0F);
            ItemStack usedCrossbowStack = shooter.getItemInHand(ProjectileUtil.getWeaponHoldingHand(shooter, item -> item instanceof CrossbowItem));
            CrossbowItem.setCharged(usedCrossbowStack, false);
            this.crossbowState = CrossbowAttack.CrossbowState.UNCHARGED;
        }
    }
}
