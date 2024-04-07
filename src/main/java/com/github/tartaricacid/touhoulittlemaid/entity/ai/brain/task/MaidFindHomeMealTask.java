package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.block.BlockPicnicMat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitPoi;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.UUID;

public class MaidFindHomeMealTask extends MaidCheckRateTask {
    private final int closeEnoughDist;
    private final float speed;

    public MaidFindHomeMealTask(float movementSpeed, int closeEnoughDist) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.closeEnoughDist = closeEnoughDist;
        this.speed = movementSpeed;
        this.setMaxCheckRate(10);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid) && maid.canBrainMoving()) {
            BlockPos picnicMatPos = findPicnicMat(worldIn, maid);
            if (picnicMatPos != null && maid.isWithinRestriction(picnicMatPos)) {
                if (picnicMatPos.distToCenterSqr(maid.position()) < Math.pow(this.closeEnoughDist, 3)) {
                    maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(picnicMatPos));
                    return true;
                }
                BehaviorUtils.setWalkAndLookTargetMemories(maid, picnicMatPos, speed, 2);
                this.setNextCheckTickCount(5);
            } else {
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent((targetPos) -> {
            BlockPos pos = targetPos.currentBlockPosition();
            BlockState blockState = worldIn.getBlockState(pos);
            if (blockState.getBlock() instanceof BlockPicnicMat picnicMat) {
                picnicMat.startMaidSit(maid, blockState, worldIn, pos);
            }
        });
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Nullable
    private BlockPos findPicnicMat(ServerLevel world, EntityMaid maid) {
        BlockPos blockPos = maid.getBrainSearchPos();
        PoiManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type.equals(InitPoi.HOME_MEAL_BLOCK.get()), blockPos, range, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos).filter(pos -> !isOccupied(world, pos))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(maid.blockPosition()))).orElse(null);
    }

    private boolean isOccupied(ServerLevel worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityPicnicMat picnicMat) {
            for (UUID uuid : picnicMat.getSitIds()) {
                if (uuid.equals(Util.NIL_UUID)) {
                    return false;
                }
                if (worldIn.getEntity(uuid) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
