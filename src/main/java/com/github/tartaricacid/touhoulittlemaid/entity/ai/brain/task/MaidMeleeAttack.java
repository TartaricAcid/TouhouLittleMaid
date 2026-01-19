package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;

public class MaidMeleeAttack {
    /**
     * 修改原版的近战攻击，将冷却时间修改为 Attribute
     */
    public static OneShot<EntityMaid> create(int cooldownBetweenAttacks) {
        return BehaviorBuilder.create((context) -> context.group(
                context.registered(MemoryModuleType.LOOK_TARGET),
                context.present(MemoryModuleType.ATTACK_TARGET),
                context.absent(MemoryModuleType.ATTACK_COOLING_DOWN),
                context.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
        ).apply(context, (lookTarget,
                          attackTarget,
                          attackCoolingDown,
                          nearestVisibleLivingEntities
        ) -> (level, maid, gameTime) -> {
            LivingEntity target = context.get(attackTarget);
            if (!isHoldingUsableProjectileWeapon(maid)
                && maid.isWithinMeleeAttackRange(target)
                && context.get(nearestVisibleLivingEntities).contains(target)
            ) {
                lookTarget.set(new EntityTracker(target, true));
                maid.swing(InteractionHand.MAIN_HAND);
                maid.doHurtTarget(target);
                double attackSpeed = maid.getAttributeValue(Attributes.ATTACK_SPEED);
                if (attackSpeed > 0) {
                    attackCoolingDown.setWithExpiry(true, (long) (cooldownBetweenAttacks / attackSpeed));
                } else {
                    attackCoolingDown.setWithExpiry(true, cooldownBetweenAttacks);
                }
                return true;
            } else {
                return false;
            }
        }));
    }

    private static boolean isHoldingUsableProjectileWeapon(EntityMaid maid) {
        return maid.isHolding((itemStack) -> {
            Item item = itemStack.getItem();
            return item instanceof ProjectileWeaponItem projectile && maid.canFireProjectileWeapon(projectile);
        });
    }
}
