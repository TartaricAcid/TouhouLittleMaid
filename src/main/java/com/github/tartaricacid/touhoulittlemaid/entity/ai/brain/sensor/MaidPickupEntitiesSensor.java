package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.world.server.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaidPickupEntitiesSensor extends Sensor<EntityMaid> {
    public MaidPickupEntitiesSensor() {
        super(30);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(InitEntities.VISIBLE_PICKUP_ENTITIES.get());
    }

    @Override
    protected void doTick(ServerWorld worldIn, EntityMaid maid) {
        List<Entity> allItemEntity = worldIn.getEntitiesOfClass(Entity.class,
                maid.getBoundingBox().inflate(8.0D, 4.0D, 8.0D), Predicates.alwaysTrue());
        allItemEntity.sort(Comparator.comparingDouble(maid::distanceToSqr));
        List<Entity> optional = allItemEntity.stream()
                .filter(maid::canPickUp)
                .filter((entity) -> entity.closerThan(maid, 9.0D))
                .filter(maid::canSee).collect(Collectors.toList());
        maid.getBrain().setMemory(InitEntities.VISIBLE_PICKUP_ENTITIES.get(), optional);
    }
}
