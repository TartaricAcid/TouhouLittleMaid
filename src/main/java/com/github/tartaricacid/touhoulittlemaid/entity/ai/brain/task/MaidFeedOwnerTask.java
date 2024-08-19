package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IFeedTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

public class MaidFeedOwnerTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 20;
    private final IFeedTask task;
    private final float walkSpeed;
    private final int closeEnoughDist;

    public MaidFeedOwnerTask(IFeedTask task, int closeEnoughDist, float walkSpeed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.task = task;
        this.walkSpeed = walkSpeed;
        this.closeEnoughDist = closeEnoughDist;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid)) {
            LivingEntity owner = maid.getOwner();
            if (owner instanceof Player && owner.isAlive() && maid.isWithinRestriction(owner.blockPosition())) {
                if (owner.closerThan(maid, closeEnoughDist)) {
                    return true;
                }
                BehaviorUtils.setWalkAndLookTargetMemories(maid, owner, walkSpeed, 1);
            }
            return false;
        }
        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        LivingEntity owner = maid.getOwner();
        if (owner instanceof Player player && owner.isAlive()) {
            boolean dying = player.getHealth() / player.getMaxHealth() < 0.5f;
            IntList lowestFoods = new IntArrayList();
            IntList lowFoods = new IntArrayList();
            IntList highFoods = new IntArrayList();

            CombinedInvWrapper inv = maid.getAvailableInv(true);
            for (int i = 0; i < inv.getSlots(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                if (task.isFood(stack, player)) {
                    IFeedTask.Priority priority = task.getPriority(stack, player);
                    if (priority == IFeedTask.Priority.HIGH) {
                        highFoods.add(i);
                        break;
                    }
                    if (priority == IFeedTask.Priority.LOW) {
                        lowFoods.add(i);
                        break;
                    }
                    if (dying && priority == IFeedTask.Priority.LOWEST) {
                        lowestFoods.add(i);
                        break;
                    }
                }
            }

            if (highFoods.isEmpty() && lowFoods.isEmpty() && lowestFoods.isEmpty()) {
                return;
            }

            IntList map = !highFoods.isEmpty() ? highFoods : !lowFoods.isEmpty() ? lowFoods : lowestFoods;
            map.stream().skip(maid.getRandom().nextInt(map.size())).findFirst().ifPresent(slot -> {
                inv.setStackInSlot(slot, task.feed(inv.getStackInSlot(slot), player));
                maid.swing(InteractionHand.MAIN_HAND);
                this.setNextCheckTickCount(5);
            });
        }
    }
}
