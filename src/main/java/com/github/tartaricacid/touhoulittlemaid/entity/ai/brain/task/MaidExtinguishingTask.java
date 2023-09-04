package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return super.checkExtraStartConditions(worldIn, owner) && isExtinguisher(owner.getMainHandItem());
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        Level world = maid.level();
        LivingEntity owner = maid.getOwner();
        ItemStack mainhandItem = maid.getMainHandItem();
        Brain<EntityMaid> brain = maid.getBrain();

        if (owner instanceof Player && owner.isAlive() && owner.isOnFire() && isExtinguisher(mainhandItem)
                && maid.isWithinRestriction(owner.blockPosition())) {
            if (maid.closerThan(owner, 2)) {
                brain.eraseMemory(MemoryModuleType.PATH);
                brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                world.addFreshEntity(new EntityExtinguishingAgent(worldIn, owner.position()));
                mainhandItem.hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                maid.swing(InteractionHand.MAIN_HAND);
            } else {
                BehaviorUtils.setWalkAndLookTargetMemories(maid, owner, speed, 2);
            }
        }

        if (maid.isOnFire() && isExtinguisher(mainhandItem)) {
            world.addFreshEntity(new EntityExtinguishingAgent(worldIn, maid.position()));
            mainhandItem.hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            maid.swing(InteractionHand.MAIN_HAND);
        }

        List<TamableAnimal> tameableEntities = world.getEntitiesOfClass(TamableAnimal.class, maid.getBoundingBox().inflate(2, 1, 2), Entity::isOnFire);
        if (!tameableEntities.isEmpty() && isExtinguisher(mainhandItem)) {
            world.addFreshEntity(new EntityExtinguishingAgent(worldIn, maid.position()));
            mainhandItem.hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            maid.swing(InteractionHand.MAIN_HAND);
        }
    }

    private boolean isExtinguisher(ItemStack stack) {
        return stack.getItem() == InitItems.EXTINGUISHER.get();
    }
}
