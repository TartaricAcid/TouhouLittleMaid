package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaidNearestLivingEntitySensor extends Sensor<EntityMaid> {
    private static final int VERTICAL_SEARCH_RANGE = 4;

    protected void doTick(ServerWorld world, EntityMaid maid) {
        float radius = maid.getRestrictRadius();
        AxisAlignedBB aabb;
        if (maid.hasRestriction()) {
            aabb = new AxisAlignedBB(maid.getRestrictCenter()).inflate(radius, VERTICAL_SEARCH_RANGE, radius);
        } else {
            aabb = maid.getBoundingBox().inflate(radius, VERTICAL_SEARCH_RANGE, radius);
        }
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, aabb, (entity) -> entity != maid && entity.isAlive());
        list.sort(Comparator.comparingDouble(maid::distanceToSqr));
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.LIVING_ENTITIES, list);
        brain.setMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES, list.stream()
                .filter(entity -> isEntityTargetable(maid, entity)).collect(Collectors.toList()));
    }

    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES);
    }
}
