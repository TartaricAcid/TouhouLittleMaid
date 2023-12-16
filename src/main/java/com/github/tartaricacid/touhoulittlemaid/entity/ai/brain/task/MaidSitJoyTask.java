package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockJoy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

public class MaidSitJoyTask extends Task<EntityMaid> {
    private static final int CLOSED_DISTANCE = 2;

    public MaidSitJoyTask() {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        if (owner.getVehicle() != null && owner.isInSittingPose()) {
            brain.eraseMemory(MemoryModuleType.PATH);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            return false;
        }
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            BlockPos blockPos = targetPos.currentBlockPosition();
            if (!blockPos.closerThan(owner.blockPosition(), CLOSED_DISTANCE)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (!walkTarget.isPresent() || !walkTarget.get().getTarget().currentBlockPosition().equals(blockPos)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            BlockState blockstate = worldIn.getBlockState(blockPos);
            return blockstate.getBlock() instanceof BlockJoy && !this.isOccupied(worldIn, blockPos);
        }).orElse(false);
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent((targetPos) -> {
            BlockPos pos = targetPos.currentBlockPosition();
            BlockState blockState = worldIn.getBlockState(pos);
            if (blockState.getBlock() instanceof BlockJoy) {
                BlockJoy blockJoy = (BlockJoy) blockState.getBlock();
                blockJoy.startMaidSit(maid, blockState, worldIn, pos);
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
            }
        });
    }

    private boolean isOccupied(ServerWorld worldIn, BlockPos pos) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityJoy) {
            TileEntityJoy joy = (TileEntityJoy) te;
            return worldIn.getEntity(joy.getSitId()) != null;
        }
        return true;
    }
}
