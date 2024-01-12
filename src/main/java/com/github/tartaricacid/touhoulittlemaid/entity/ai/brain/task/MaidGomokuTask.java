package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockGomoku;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitPoi;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Comparator;

public class MaidGomokuTask extends MaidCheckRateTask {
    private final int closeEnoughDist;
    private final float speed;

    public MaidGomokuTask(float movementSpeed, int closeEnoughDist) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
        this.closeEnoughDist = closeEnoughDist;
        this.speed = movementSpeed;
        this.setMaxCheckRate(10);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid) && this.maidStateConditions(maid)) {
            BlockPos gomokuPos = findGomoku(worldIn, maid);
            if (gomokuPos != null && maid.isWithinRestriction(gomokuPos)) {
                if (gomokuPos.distSqr(maid.blockPosition()) < Math.pow(this.closeEnoughDist, 2)) {
                    maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(gomokuPos));
                    return true;
                }
                BrainUtil.setWalkAndLookTargetMemories(maid, gomokuPos, speed, 1);
                this.setNextCheckTickCount(5);
            } else {
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            }
        }
        return false;
    }

    @Override
    protected void start(ServerWorld worldIn, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent((targetPos) -> {
            BlockPos pos = targetPos.currentBlockPosition();
            BlockState blockState = worldIn.getBlockState(pos);
            if (blockState.getBlock() instanceof BlockGomoku) {
                BlockGomoku gomoku = (BlockGomoku) blockState.getBlock();
                gomoku.startMaidSit(maid, blockState, worldIn, pos);
            }
        });
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Nullable
    private BlockPos findGomoku(ServerWorld world, EntityMaid maid) {
        BlockPos blockPos = maid.blockPosition();
        PointOfInterestManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type.equals(InitPoi.JOY_BLOCK.get()), blockPos, range, PointOfInterestManager.Status.ANY)
                .map(PointOfInterest::getPos).filter(pos -> !isOccupied(world, pos))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(blockPos))).orElse(null);
    }

    private boolean isOccupied(ServerWorld worldIn, BlockPos pos) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityGomoku) {
            TileEntityGomoku gomoku = (TileEntityGomoku) te;
            return worldIn.getEntity(gomoku.getSitId()) != null;
        }
        return true;
    }

    private boolean maidStateConditions(EntityMaid maid) {
        return !maid.isInSittingPose() && !maid.isSleeping() && !maid.isLeashed() && !maid.isPassenger();
    }
}
