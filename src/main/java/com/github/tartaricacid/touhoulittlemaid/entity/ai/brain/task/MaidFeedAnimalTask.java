package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class MaidFeedAnimalTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 12;
    private final int maxDistToWalk;
    private final float speedModifier;
    private final int maxAnimalCount;
    private AnimalEntity feedEntity = null;

    public MaidFeedAnimalTask(int maxDistToWalk, float speedModifier, int maxAnimalCount) {
        super(ImmutableMap.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
        this.maxDistToWalk = maxDistToWalk;
        this.speedModifier = speedModifier;
        this.maxAnimalCount = maxAnimalCount;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        long animalCount = this.getEntities(maid).stream()
                .filter(e -> e.closerThan(maid, maxDistToWalk))
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof AnimalEntity).count();

        if (animalCount < maxAnimalCount) {
            this.getEntities(maid).stream()
                    .filter(e -> e.closerThan(maid, maxDistToWalk))
                    .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                    .filter(Entity::isAlive)
                    .filter(e -> e instanceof AnimalEntity)
                    .filter(e -> ((AnimalEntity) e).getAge() == 0)
                    .filter(e -> ((AnimalEntity) e).canFallInLove())
                    .filter(e -> ItemsUtil.isStackIn(maid.getAvailableInv(false), ((AnimalEntity) e)::isFood))
                    .filter(maid::canPathReach)
                    .findFirst()
                    .ifPresent(e -> {
                        feedEntity = (AnimalEntity) e;
                        BrainUtil.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0);
                    });

            if (feedEntity != null && feedEntity.closerThan(maid, 2)) {
                ItemStack food = ItemsUtil.getStack(maid.getAvailableInv(false), feedEntity::isFood);
                if (!food.isEmpty()) {
                    food.shrink(1);
                    maid.swing(Hand.MAIN_HAND);
                    feedEntity.setInLove(null);
                }
                feedEntity = null;
            }
        }
    }

    private List<LivingEntity> getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Lists.newArrayList());
    }
}
