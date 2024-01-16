package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class MaidNearestLivingEntitySensor extends Sensor<EntityMaid> {
    private static final int VERTICAL_SEARCH_RANGE = 4;

    protected void doTick(ServerLevel world, EntityMaid maid) {
        float radius = maid.getRestrictRadius();
        AABB aabb = maid.getBoundingBox().inflate(radius, VERTICAL_SEARCH_RANGE, radius);
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, aabb, (entity) -> entity != maid && entity.isAlive());
        list.sort(Comparator.comparingDouble(maid::distanceToSqr));
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(maid, list));
    }

    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }
}
