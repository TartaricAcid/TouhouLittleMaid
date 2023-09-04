package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;


import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class SetWalkTargetAwayFrom<T> extends Behavior<PathfinderMob> {
    private final MemoryModuleType<T> walkAwayFromMemory;
    private final float speedModifier;
    private final int desiredDistance;
    private final Function<T, Vec3> toPosition;

    public SetWalkTargetAwayFrom(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean flag, Function<T, Vec3> toPosition) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, flag ? MemoryStatus.REGISTERED : MemoryStatus.VALUE_ABSENT, walkAwayFromMemory, MemoryStatus.VALUE_PRESENT));
        this.walkAwayFromMemory = walkAwayFromMemory;
        this.speedModifier = speedModifier;
        this.desiredDistance = desiredDistance;
        this.toPosition = toPosition;
    }

    public static SetWalkTargetAwayFrom<BlockPos> pos(MemoryModuleType<BlockPos> memoryModuleType, float speedModifier, int desiredDistance, boolean flag) {
        return new SetWalkTargetAwayFrom<>(memoryModuleType, speedModifier, desiredDistance, flag, Vec3::atBottomCenterOf);
    }

    public static SetWalkTargetAwayFrom<? extends Entity> entity(MemoryModuleType<? extends Entity> moduleType, float speedModifier, int desiredDistance, boolean flag) {
        return new SetWalkTargetAwayFrom<>(moduleType, speedModifier, desiredDistance, flag, Entity::position);
    }

    public boolean checkExtraStartConditions(ServerLevel pLevel, PathfinderMob pOwner) {
        return !this.alreadyWalkingAwayFromPosWithSameSpeed(pOwner) && pOwner.position().closerThan(this.getPosToAvoid(pOwner), this.desiredDistance);
    }

    private Vec3 getPosToAvoid(PathfinderMob mob) {
        return this.toPosition.apply(mob.getBrain().getMemory(this.walkAwayFromMemory).get());
    }

    private boolean alreadyWalkingAwayFromPosWithSameSpeed(PathfinderMob mob) {
        if (!mob.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            return false;
        } else {
            WalkTarget walktarget = mob.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();
            if (walktarget.getSpeedModifier() != this.speedModifier) {
                return false;
            } else {
                Vec3 vec3 = walktarget.getTarget().currentPosition().subtract(mob.position());
                Vec3 vec31 = this.getPosToAvoid(mob).subtract(mob.position());
                return vec3.dot(vec31) < 0.0D;
            }
        }
    }

    protected void start(ServerLevel pLevel, PathfinderMob pEntity, long pGameTime) {
        moveAwayFrom(pEntity, this.getPosToAvoid(pEntity), this.speedModifier);
    }

    private static void moveAwayFrom(PathfinderMob mob, Vec3 vec31, float speed) {
        for (int i = 0; i < 10; ++i) {
            Vec3 vec3 = LandRandomPos.getPosAway(mob, 16, 7, vec31);
            if (vec3 != null) {
                mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, speed, 0));
                return;
            }
        }
    }
}
