package com.github.tartaricacid.touhoulittlemaid.entity.ai.behavior;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class SableMoveToTargetSink extends MoveToTargetSink {
    private boolean tryComputePathViaEntity(Mob mob, WalkTarget target, long time) {
        EntityTracker entityTracker = (EntityTracker) target.getTarget();
        this.path = mob.getNavigation().createPath(entityTracker.getEntity(), 0);
        this.speedModifier = target.getSpeedModifier();
        Brain<?> brain = mob.getBrain();
        if (this.reachedTarget(mob, target)) {
            brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        } else {
            boolean flag = this.path != null && this.path.canReach();
            if (flag) {
                brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            } else if (!brain.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
                brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, time);
            }

            if (this.path != null) {
                return true;
            }

            Vec3 vec3 = DefaultRandomPos.getPosTowards((PathfinderMob) mob, 10, 7, Vec3.atBottomCenterOf(entityTracker.currentBlockPosition()), (float) (Math.PI / 2));
            if (vec3 != null) {
                this.path = mob.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }

        return false;
    }

    public boolean tryComputePath(Mob mob, WalkTarget target, long time) {
        if (target.getTarget() instanceof EntityTracker) {
            return tryComputePathViaEntity(mob, target, time);
        }

        return super.tryComputePath(mob, target, time);
    }
}
