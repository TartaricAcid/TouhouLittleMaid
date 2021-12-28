package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class MaidExtinguishingTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 12;
    private final float speed;

    public MaidExtinguishingTask(float speed) {
        super(ImmutableMap.of());
        this.speed = speed;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        return super.checkExtraStartConditions(worldIn, owner) && isExtinguisher(owner.getMainHandItem());
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        World world = maid.level;
        LivingEntity owner = maid.getOwner();
        ItemStack mainhandItem = maid.getMainHandItem();
        Brain<EntityMaid> brain = maid.getBrain();

        if (owner instanceof PlayerEntity && owner.isAlive() && owner.isOnFire() && isExtinguisher(mainhandItem)
                && maid.isWithinRestriction(owner.blockPosition())) {
            if (maid.closerThan(owner, 2)) {
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                world.addFreshEntity(new EntityExtinguishingAgent(worldIn, owner.position()));
                mainhandItem.hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(Hand.MAIN_HAND));
                maid.swing(Hand.MAIN_HAND);
            } else {
                BrainUtil.setWalkAndLookTargetMemories(maid, owner, speed, 2);
            }
        }

        if (maid.isOnFire() && isExtinguisher(mainhandItem)) {
            world.addFreshEntity(new EntityExtinguishingAgent(worldIn, maid.position()));
            mainhandItem.hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(Hand.MAIN_HAND));
            maid.swing(Hand.MAIN_HAND);
        }

        List<TameableEntity> tameableEntities = world.getEntitiesOfClass(TameableEntity.class, maid.getBoundingBox().inflate(2, 1, 2), Entity::isOnFire);
        if (!tameableEntities.isEmpty() && isExtinguisher(mainhandItem)) {
            world.addFreshEntity(new EntityExtinguishingAgent(worldIn, maid.position()));
            mainhandItem.hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(Hand.MAIN_HAND));
            maid.swing(Hand.MAIN_HAND);
        }
    }

    private boolean isExtinguisher(ItemStack stack) {
        return stack.getItem() == InitItems.EXTINGUISHER.get();
    }
}
