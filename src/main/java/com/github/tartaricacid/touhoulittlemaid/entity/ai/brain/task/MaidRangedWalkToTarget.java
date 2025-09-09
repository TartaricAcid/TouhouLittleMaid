package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.kinds.IdF;
import com.mojang.datafixers.kinds.OptionalBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * 修改了原版的走向攻击目标的 AI，现在能够依据超远视距和 home 范围进行行走判断
 * <p>
 * 主要用于远程射击，近战的还是请走 SetWalkTargetFromAttackTargetIfTargetOutOfReach
 */
public class MaidRangedWalkToTarget {
    public static BehaviorControl<EntityMaid> create(float speedModifier) {
        return create(entity -> speedModifier);
    }

    public static BehaviorControl<EntityMaid> create(Function<LivingEntity, Float> speedModifier) {
        return BehaviorBuilder.create(maidInstance -> maidInstance.group(
                        maidInstance.registered(MemoryModuleType.WALK_TARGET),
                        maidInstance.registered(MemoryModuleType.LOOK_TARGET),
                        maidInstance.present(MemoryModuleType.ATTACK_TARGET),
                        maidInstance.registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
                .apply(maidInstance, (walkTargetMemory, positionMemory, entityMemory, livingEntitiesMemory)
                        -> setTarget(speedModifier, maidInstance, walkTargetMemory, positionMemory, entityMemory, livingEntitiesMemory)));
    }

    @NotNull
    private static Trigger<EntityMaid> setTarget(Function<LivingEntity, Float> speedModifier,
                                                 BehaviorBuilder.Instance<EntityMaid> maidInstance,
                                                 MemoryAccessor<OptionalBox.Mu, WalkTarget> walkTargetMemory,
                                                 MemoryAccessor<OptionalBox.Mu, PositionTracker> positionMemory,
                                                 MemoryAccessor<IdF.Mu, LivingEntity> entityMemory,
                                                 MemoryAccessor<OptionalBox.Mu, NearestVisibleLivingEntities> livingEntitiesMemory) {
        return (level, maid, gameTime) -> {
            LivingEntity target = maidInstance.get(entityMemory);
            if (maid.canSee(target) && shouldEraseWalkTarget(maid, target)) {
                walkTargetMemory.erase();
            } else {
                positionMemory.set(new EntityTracker(target, true));
                walkTargetMemory.set(new WalkTarget(new EntityTracker(target, false), speedModifier.apply(maid), 0));
            }
            return true;
        };
    }

    private static boolean shouldEraseWalkTarget(EntityMaid maid, LivingEntity target) {
        float restrictRadius = maid.getRestrictRadius() - 2;
        double checkRadius = 8;
        if (maid.hasRestriction()) {
            BlockPos center = maid.getRestrictCenter();
            checkRadius = Math.sqrt(target.distanceToSqr(center.getX(), center.getY(), center.getZ()));
        } else if (maid.getOwner() instanceof Player player) {
            checkRadius = target.distanceTo(player);
        }
        // 如果目标在 4 格外，在 最大限制范围-2 内，可以直接走过去，否则清除目标
        return checkRadius <= 4 || checkRadius >= restrictRadius;
    }
}
