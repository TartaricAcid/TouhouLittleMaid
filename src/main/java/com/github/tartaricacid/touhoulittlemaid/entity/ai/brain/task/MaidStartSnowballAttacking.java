package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class MaidStartSnowballAttacking<E extends Mob> extends Behavior<E> {
    private final Predicate<E> canAttackPredicate;
    private final Function<E, Optional<? extends LivingEntity>> targetFinderFunction;

    public MaidStartSnowballAttacking(Predicate<E> predicate, Function<E, Optional<? extends LivingEntity>> function) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
        this.canAttackPredicate = predicate;
        this.targetFinderFunction = function;
    }

    public MaidStartSnowballAttacking(Function<E, Optional<? extends LivingEntity>> function) {
        this((f) -> true, function);
    }

    protected boolean checkExtraStartConditions(ServerLevel pLevel, E owner) {
        if (!this.canAttackPredicate.test(owner)) {
            return false;
        } else {
            Optional<? extends LivingEntity> optional = this.targetFinderFunction.apply(owner);
            return optional.isPresent() && optional.get().isAlive();
        }
    }

    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        this.targetFinderFunction.apply(pEntity).ifPresent((entity) -> this.setAttackTarget(pEntity, entity));
    }

    private void setAttackTarget(E owner, LivingEntity entity) {
        owner.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, entity);
        owner.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}
