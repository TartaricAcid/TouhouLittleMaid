package com.github.tartaricacid.touhoulittlemaid.compat.tacz.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.kinds.IdF;
import com.mojang.datafixers.kinds.OptionalBox;
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
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public class GunWalkToTarget {
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
            Optional<NearestVisibleLivingEntities> optional = maidInstance.tryGet(livingEntitiesMemory);
            if (optional.isPresent() && optional.get().contains(target) && isWithinRestriction(maid, target)) {
                walkTargetMemory.erase();
            } else {
                positionMemory.set(new EntityTracker(target, true));
                walkTargetMemory.set(new WalkTarget(new EntityTracker(target, false), speedModifier.apply(maid), 0));
            }
            return true;
        };
    }

    private static boolean isWithinRestriction(EntityMaid maid, LivingEntity target) {
        float restrictRadius = maid.getRestrictRadius() * 0.65f;
        return target.distanceTo(maid) < restrictRadius;
    }
}
