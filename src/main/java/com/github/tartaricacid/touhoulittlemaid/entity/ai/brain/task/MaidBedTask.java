package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitPoi;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Comparator;

public class MaidBedTask extends MaidCheckRateTask {
    private final int closeEnoughDist;
    private final float speed;

    public MaidBedTask(float movementSpeed, int closeEnoughDist) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
        this.closeEnoughDist = closeEnoughDist;
        this.speed = movementSpeed;
        this.setMaxCheckRate(10);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid) && maid.canBrainMoving()) {
            BlockPos bedPos = findBed(worldIn, maid);
            if (bedPos != null && maid.isWithinRestriction(bedPos)) {
                if (bedPos.distSqr(maid.blockPosition()) < Math.pow(this.closeEnoughDist, 2)) {
                    maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(bedPos));
                    return true;
                }
                BrainUtil.setWalkAndLookTargetMemories(maid, bedPos, speed, 1);
                this.setNextCheckTickCount(5);
            } else {
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            }
        }
        return false;
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(targetPos -> {
            BlockPos pos = targetPos.currentBlockPosition();
            BlockState blockState = worldIn.getBlockState(pos);
            if (blockState.is(InitBlocks.MAID_BED.get()) && blockState.getValue(BlockMaidBed.PART) == BedPart.HEAD && !blockState.getValue(BedBlock.OCCUPIED)) {
                maid.startSleeping(pos);
                maid.setPos(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
            }
        });
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Nullable
    private BlockPos findBed(ServerWorld world, EntityMaid maid) {
        BlockPos blockPos = maid.getBrainSearchPos();
        PointOfInterestManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type.equals(InitPoi.MAID_BED.get()), blockPos, range, PointOfInterestManager.Status.ANY)
                .map(PointOfInterest::getPos).min(Comparator.comparingDouble(pos -> pos.distSqr(maid.blockPosition()))).orElse(null);
    }
}
