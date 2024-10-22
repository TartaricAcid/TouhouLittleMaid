package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameBlock;
import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameEntityBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitPoi;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

public class MaidBoardGameTask extends MaidCheckRateTask {
    private final int closeEnoughDist;
    private final float speed;

    public MaidBoardGameTask(float movementSpeed, int closeEnoughDist) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.closeEnoughDist = closeEnoughDist;
        this.speed = movementSpeed;
        this.setMaxCheckRate(10);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid) && maid.canBrainMoving()) {
            BlockPos gamePos = findGameBlock(worldIn, maid);
            if (gamePos != null && maid.isWithinRestriction(gamePos)) {
                if (gamePos.distToCenterSqr(maid.position()) < Math.pow(this.closeEnoughDist, 2)) {
                    maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(gamePos));
                    return true;
                }
                BehaviorUtils.setWalkAndLookTargetMemories(maid, gamePos, speed, 1);
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
            if (blockState.getBlock() instanceof IBoardGameBlock gameBlock) {
                gameBlock.startMaidSit(maid, blockState, worldIn, pos);
                if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                    InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.MAID_SIT_JOY);
                }
            }
        });
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Nullable
    private BlockPos findGameBlock(ServerLevel world, EntityMaid maid) {
        BlockPos blockPos = maid.getBrainSearchPos();
        PoiManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type.value().equals(InitPoi.JOY_BLOCK.get()), blockPos, range, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos).filter(pos -> !isOccupied(world, pos))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(maid.blockPosition()))).orElse(null);
    }

    private boolean isOccupied(ServerLevel worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof IBoardGameEntityBlock gameBlock) {
            return worldIn.getEntity(gameBlock.getSitId()) != null;
        }
        return true;
    }
}
