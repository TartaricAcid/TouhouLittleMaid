package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class MaidSleepTask extends Task<EntityMaid> {
    private static final int CLOSED_DISTANCE = 2;

    public MaidSleepTask() {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        if (owner.isSleeping()) {
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            return false;
        }
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            BlockPos blockPos = targetPos.currentBlockPosition();
            if (!blockPos.closerThan(owner.position(), CLOSED_DISTANCE)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (!walkTarget.isPresent() || !walkTarget.get().getTarget().currentBlockPosition().equals(blockPos)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            BlockState blockstate = worldIn.getBlockState(blockPos);
            return blockstate.getBlock().is(InitBlocks.MAID_BED.get())
                    && blockstate.getValue(BlockMaidBed.PART) == BedPart.HEAD
                    && !blockstate.getValue(BedBlock.OCCUPIED);
        }).orElse(false);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent((targetPos) -> {
            BlockPos pos = targetPos.currentBlockPosition();
            maid.startSleeping(pos);
            maid.setPos(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
            maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        });
    }
}
