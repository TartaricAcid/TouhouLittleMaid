package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.Optional;

public class MaidFarmPlantTask extends Task<EntityMaid> {
    private final IFarmTask task;

    public MaidFarmPlantTask(IFarmTask task) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_PRESENT));
        this.task = task;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vector3d targetV3d = targetPos.currentPosition();
            if (owner.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (!walkTarget.isPresent() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    protected void start(ServerWorld world, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockPos cropPos = basePos.above();
            BlockState cropState = world.getBlockState(cropPos);
            if (maid.canDestroyBlock(cropPos) && task.canHarvest(maid, cropPos, cropState)) {
                task.harvest(maid, cropPos, cropState);
                maid.swing(Hand.MAIN_HAND);
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            }

            CombinedInvWrapper availableInv = maid.getAvailableInv(true);
            List<Integer> slots = ItemsUtil.getFilterStackSlots(availableInv, task::isSeed);
            if (!slots.isEmpty()) {
                for (int slot : slots) {
                    ItemStack seed = availableInv.getStackInSlot(slot);
                    BlockState baseState = world.getBlockState(basePos);
                    if (maid.canPlaceBlock(cropPos) && task.canPlant(maid, basePos, baseState, seed)) {
                        ItemStack remain = task.plant(maid, basePos, baseState, seed);
                        availableInv.setStackInSlot(slot, remain);
                        maid.swing(Hand.MAIN_HAND);
                        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                        return;
                    }
                }
            }
        });
    }
}
