package com.github.tartaricacid.touhoulittlemaid.compat.tacz.utils;

import com.github.tartaricacid.touhoulittlemaid.compat.tacz.task.TaskGunAttack;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.tacz.guns.api.item.IGun;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

public class GunNearestLivingEntitySensor {
    public static boolean isGunTask(EntityMaid maid) {
        return maid.getTask().getUid().equals(TaskGunAttack.UID) && maid.getScheduleDetail() == Activity.WORK && IGun.mainhandHoldGun(maid);
    }

    public static void doGunTick(ServerLevel world, EntityMaid maid) {
        AABB aabb;
        int searchRange = MaidConfig.MAID_GUN_LONG_DISTANCE.get();
        if (maid.hasRestriction()) {
            aabb = new AABB(maid.getRestrictCenter()).inflate(searchRange);
        } else {
            aabb = maid.getBoundingBox().inflate(searchRange);
        }
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, aabb, (entity) -> entity != maid && entity.isAlive());
        list.sort(Comparator.comparingDouble(maid::distanceToSqr));
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(maid, list));
    }
}
