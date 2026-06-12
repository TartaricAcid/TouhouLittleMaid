package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.sensor;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class MaidNearestLivingEntitySensor extends Sensor<EntityMaid> {
    @Override
    protected void doTick(ServerLevel world, EntityMaid maid) {
        AABB aabb = maid.searchDimension();
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, aabb, (entity) -> entity != maid && entity.isAlive());
        list.sort(Comparator.comparingDouble(maid::distanceToSqr));
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(maid, list));
        // 统一扫描椅子 + 船，使用相同的过滤条件：限制范围内 + 存活 + 无乘客 + 有视线（与椅子原管道一致）
        List<EntityChair> chairs = world.getEntitiesOfClass(EntityChair.class, aabb, chair ->
                chair.isAlive() && maid.isWithinRestriction(chair.blockPosition()) && chair.getPassengers().isEmpty());
        List<Boat> boats = world.getEntitiesOfClass(Boat.class, aabb, boat ->
                boat.isAlive() && maid.isWithinRestriction(boat.blockPosition()) && boat.getPassengers().isEmpty());
        Entity nearestSitTarget = Stream.concat(chairs.stream(), boats.stream())
                .filter(maid::hasLineOfSight)
                .min(Comparator.comparingDouble(maid::distanceToSqr))
                .orElse(null);
        brain.setMemory(InitEntities.NEAREST_SIT_TARGET.get(), nearestSitTarget);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, InitEntities.NEAREST_SIT_TARGET.get());
    }
}
