package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaidPickupEntitiesSensor extends Sensor<EntityMaid> {
    private static final int VERTICAL_SEARCH_RANGE = 4;

    public MaidPickupEntitiesSensor() {
        super(30);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(InitEntities.VISIBLE_PICKUP_ENTITIES.get());
    }

    @Override
    protected void doTick(ServerLevel worldIn, EntityMaid maid) {
        if (!maid.isTame()) {
            return;
        }
        float radius = maid.getRestrictRadius();
        List<Entity> allEntities = worldIn.getEntitiesOfClass(Entity.class,
                maid.getBoundingBox().inflate(radius, VERTICAL_SEARCH_RANGE, radius),
                Entity::isAlive);
        allEntities.sort(Comparator.comparingDouble(maid::distanceToSqr));
        List<Entity> optional = allEntities.stream()
                .filter(e -> maid.canPickup(e, true))
                .filter(e -> e.closerThan(maid, radius + 1))
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(maid::hasLineOfSight).collect(Collectors.toList());
        maid.getBrain().setMemory(InitEntities.VISIBLE_PICKUP_ENTITIES.get(), optional);
    }
}
