package com.github.tartaricacid.touhoulittlemaid.compat.tacz.utils;

import com.github.tartaricacid.touhoulittlemaid.compat.tacz.task.TaskGunAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

public class GunNearestLivingEntitySensor {
    private static final int VERTICAL_SEARCH_RANGE = 64;
    private static final int RADIUS_SEARCH_RANGE = 128;

    public static boolean isGunTask(EntityMaid maid) {
        return maid.getTask().getUid().equals(TaskGunAttack.UID);
    }

    public static void doGunTick(ServerLevel world, EntityMaid maid) {
        AABB aabb;
        if (maid.hasRestriction()) {
            aabb = new AABB(maid.getRestrictCenter()).inflate(RADIUS_SEARCH_RANGE, VERTICAL_SEARCH_RANGE, RADIUS_SEARCH_RANGE);
        } else {
            aabb = maid.getBoundingBox().inflate(RADIUS_SEARCH_RANGE, VERTICAL_SEARCH_RANGE, RADIUS_SEARCH_RANGE);
        }
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, aabb, (entity) -> entity != maid && entity.isAlive());
        list.sort(Comparator.comparingDouble(maid::distanceToSqr));
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(maid, list));
    }

    public static int getRadiusSearchRange() {
        return RADIUS_SEARCH_RANGE;
    }
}
