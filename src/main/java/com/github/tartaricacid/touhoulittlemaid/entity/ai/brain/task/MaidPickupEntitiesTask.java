package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;
import java.util.function.Predicate;

public class MaidPickupEntitiesTask extends Behavior<EntityMaid> {
    private final Predicate<EntityMaid> predicate;
    private final float speedModifier;

    public MaidPickupEntitiesTask(float speedModifier) {
        this(Predicates.alwaysTrue(), speedModifier);
    }

    public MaidPickupEntitiesTask(Predicate<EntityMaid> predicate, float speedModifier) {
        super(ImmutableMap.of(InitEntities.VISIBLE_PICKUP_ENTITIES.get(), MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.predicate = predicate;
        this.speedModifier = speedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return owner.isTame() && owner.canBrainMoving() && predicate.test(owner);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        getItems(maid).stream()
                .filter(e -> maid.isWithinRestriction(e.blockPosition()) && e.isAlive() && !e.isInWater())
                .filter(maid::canPathReach).findFirst()
                .ifPresent(e -> BehaviorUtils.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0));
    }

    private List<Entity> getItems(EntityMaid maid) {
        return maid.getBrain().getMemory(InitEntities.VISIBLE_PICKUP_ENTITIES.get()).orElse(Lists.newArrayList());
    }
}
