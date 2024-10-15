package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.vehicle.Boat;

public class MaidFindSitTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 12;
    private final float speedModifier;
    private Entity sitEntity = null;

    public MaidFindSitTask(float speedModifier) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = speedModifier;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return super.checkExtraStartConditions(worldIn, owner) && owner.getVehicle() == null;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        this.sitEntity = null;
        this.getEntities(maid)
                .find(e -> filterEntity(maid, e))
                .findFirst()
                .ifPresentOrElse(entity -> {
                    this.sitEntity = entity;
                    BehaviorUtils.setWalkAndLookTargetMemories(maid, this.sitEntity, this.speedModifier, 0);
                }, () -> ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.inner.fishing.no_sit"));

        if (sitEntity != null && sitEntity.isAlive() && sitEntity.closerThan(maid, 2)) {
            if (sitEntity.getPassengers().isEmpty()) {
                maid.startRiding(this.sitEntity, true);
            }
            this.sitEntity = null;
        }
    }

    private boolean filterEntity(EntityMaid maid, Entity entity) {
        if (!entity.isAlive()) {
            return false;
        }
        if (!maid.isWithinRestriction(entity.blockPosition())) {
            return false;
        }
        if (!entity.getPassengers().isEmpty()) {
            return false;
        }
        return entity instanceof EntityChair || entity instanceof Boat;
    }

    private NearestVisibleLivingEntities getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
    }
}
