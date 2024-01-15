package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class MaidMilkTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 40;
    private final float speedModifier;
    private LivingEntity milkTarget = null;

    public MaidMilkTask(float speedModifier) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = speedModifier;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        if (super.checkExtraStartConditions(worldIn, owner)) {
            CombinedInvWrapper availableInv = owner.getAvailableInv(true);
            return ItemsUtil.isStackIn(availableInv, stack -> stack.getItem() == Items.BUCKET) &&
                    ItemsUtil.isStackIn(availableInv, stack -> stack == ItemStack.EMPTY);
        }
        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        milkTarget = null;
        int maxDistToWalk = (int) maid.getRestrictRadius();
        this.getEntities(maid)
                .find(e -> e.closerThan(maid, maxDistToWalk))
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(Entity::isAlive)
                .filter(e -> e instanceof Cow)
                .filter(e -> !e.isBaby())
                .filter(maid::canPathReach)
                .findFirst()
                .ifPresent(e -> {
                    milkTarget = e;
                    BehaviorUtils.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0);
                });

        if (milkTarget != null && milkTarget.closerThan(maid, 2)) {
            CombinedInvWrapper availableInv = maid.getAvailableInv(false);
            ItemStack bucket = ItemsUtil.getStack(availableInv, stack -> stack.getItem() == Items.BUCKET);
            if (bucket != ItemStack.EMPTY) {
                bucket.shrink(1);
                ItemHandlerHelper.insertItemStacked(availableInv, new ItemStack(Items.MILK_BUCKET), false);
            }
            maid.swing(InteractionHand.MAIN_HAND);
            maid.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            milkTarget = null;
        }
    }

    private NearestVisibleLivingEntities getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
    }
}
