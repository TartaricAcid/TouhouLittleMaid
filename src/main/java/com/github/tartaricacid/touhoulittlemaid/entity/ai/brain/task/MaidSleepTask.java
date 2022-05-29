package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.Optional;

public class MaidSleepTask extends Behavior<EntityMaid> {
    private static final int CLOSED_DISTANCE = 2;

    public MaidSleepTask() {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        if (owner.isSleeping()) {
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            return false;
        }
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            BlockPos blockPos = targetPos.currentBlockPosition();
            if (!blockPos.closerThan(owner.blockPosition(), CLOSED_DISTANCE)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentBlockPosition().equals(blockPos)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            BlockState blockstate = worldIn.getBlockState(blockPos);
            return blockstate.is(InitBlocks.MAID_BED.get())
                    && blockstate.getValue(BlockMaidBed.PART) == BedPart.HEAD
                    && !blockstate.getValue(BedBlock.OCCUPIED);
        }).orElse(false);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
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
