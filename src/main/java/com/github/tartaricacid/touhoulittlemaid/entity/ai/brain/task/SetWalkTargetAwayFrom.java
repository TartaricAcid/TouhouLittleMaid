package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;


import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.RunAwayTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Function;

public class SetWalkTargetAwayFrom<T> extends Task<CreatureEntity> {
    private final MemoryModuleType<T> walkAwayFromMemory;
    private final float speedModifier;
    private final Function<T, Vector3d> toPosition;

    public SetWalkTargetAwayFrom(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, boolean ignoreOtherWalkTarget, Function<T, Vector3d> toPosition) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, ignoreOtherWalkTarget ? MemoryModuleStatus.REGISTERED : MemoryModuleStatus.VALUE_ABSENT, walkAwayFromMemory, MemoryModuleStatus.VALUE_PRESENT));
        this.walkAwayFromMemory = walkAwayFromMemory;
        this.speedModifier = speedModifier;
        this.toPosition = toPosition;
    }

    public static RunAwayTask<BlockPos> pos(MemoryModuleType<BlockPos> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean ignoreOtherWalkTarget) {
        return new RunAwayTask<>(walkAwayFromMemory, speedModifier, desiredDistance, ignoreOtherWalkTarget, Vector3d::atBottomCenterOf);
    }

    public static RunAwayTask<? extends Entity> entity(MemoryModuleType<? extends Entity> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean ignoreOtherWalkTarget) {
        return new RunAwayTask<>(walkAwayFromMemory, speedModifier, desiredDistance, ignoreOtherWalkTarget, Entity::position);
    }

    protected boolean checkExtraStartConditions(ServerWorld serverWorld, CreatureEntity creature) {
        int radius = (int) creature.getRestrictRadius();
        return !this.alreadyWalkingAwayFromPosWithSameSpeed(creature) && creature.position().closerThan(this.getPosToAvoid(creature), radius);
    }

    private Vector3d getPosToAvoid(CreatureEntity creature) {
        return this.toPosition.apply(creature.getBrain().getMemory(this.walkAwayFromMemory).get());
    }

    private boolean alreadyWalkingAwayFromPosWithSameSpeed(CreatureEntity creature) {
        if (!creature.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            return false;
        } else {
            WalkTarget walktarget = creature.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();
            if (walktarget.getSpeedModifier() != this.speedModifier) {
                return false;
            } else {
                Vector3d subtract = walktarget.getTarget().currentPosition().subtract(creature.position());
                Vector3d vector3d = this.getPosToAvoid(creature).subtract(creature.position());
                return subtract.dot(vector3d) < 0.0D;
            }
        }
    }

    protected void start(ServerWorld pLevel, CreatureEntity pEntity, long pGameTime) {
        moveAwayFrom(pEntity, this.getPosToAvoid(pEntity), this.speedModifier);
    }

    private static void moveAwayFrom(CreatureEntity creature, Vector3d vec, float speedModifier) {
        int radius = (int) creature.getRestrictRadius();
        for (int i = 0; i < 10; ++i) {
            Vector3d vector3d = RandomPositionGenerator.getLandPosAvoid(creature, radius, 7, vec);
            if (vector3d != null) {
                creature.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vector3d, speedModifier, 0));
                return;
            }
        }
    }
}
