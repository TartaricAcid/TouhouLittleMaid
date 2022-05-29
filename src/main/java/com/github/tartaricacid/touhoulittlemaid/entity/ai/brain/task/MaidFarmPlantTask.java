package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.Optional;

public class MaidFarmPlantTask extends Behavior<EntityMaid> {
    private final IFarmTask task;

    public MaidFarmPlantTask(IFarmTask task) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT));
        this.task = task;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            if (owner.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    protected void start(ServerLevel world, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockPos cropPos = basePos.above();
            BlockState cropState = world.getBlockState(cropPos);
            if (maid.canDestroyBlock(cropPos) && task.canHarvest(maid, cropPos, cropState)) {
                task.harvest(maid, cropPos, cropState);
                maid.swing(InteractionHand.MAIN_HAND);
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            }

            CombinedInvWrapper availableInv = maid.getAvailableInv(true);
            List<Integer> slots = ItemsUtil.getFilterStackSlots(availableInv, task::isSeed);
            if (!slots.isEmpty()) {
                for (int slot : slots) {
                    ItemStack seed = availableInv.getStackInSlot(slot);
                    BlockState baseState = world.getBlockState(basePos);
                    if (task.canPlant(maid, basePos, baseState, seed)) {
                        ItemStack remain = task.plant(maid, basePos, baseState, seed);
                        availableInv.setStackInSlot(slot, remain);
                        maid.swing(InteractionHand.MAIN_HAND);
                        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                        return;
                    }
                }
            }
        });
    }
}
