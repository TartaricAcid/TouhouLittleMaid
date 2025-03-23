package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public abstract class MaidMoveToBlockTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    protected int verticalSearchStart;
    /**
     * 最近工作点标志位（用于记录当前工作的方块位置，缓存下来便于下次在该点附近工作）
     */
    private BlockPos currentWorkPos;

    public MaidMoveToBlockTask(float movementSpeed) {
        this(movementSpeed, 1);
    }

    public MaidMoveToBlockTask(float movementSpeed, int verticalSearchRange) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
        MaidPathFindingBFS pathFinding = getOrCreateArrivalMap(worldIn, maid);
        BlockPos centrePos = this.getWorkSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y - 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos) && checkPathReach(maid, pathFinding, mutableBlockPos)
                            && checkOwnerPos(maid, mutableBlockPos)) {
                            BehaviorUtils.setWalkAndLookTargetMemories(maid, mutableBlockPos, this.movementSpeed, 0);
                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(mutableBlockPos));
                            this.currentWorkPos = mutableBlockPos;
                            this.setNextCheckTickCount(5);
                            this.clearCurrentArrivalMap(pathFinding);
                            return;
                        }
                    }
                }
            }
        }
        this.currentWorkPos = null;
        this.clearCurrentArrivalMap(pathFinding);
    }

    protected void clearCurrentArrivalMap(MaidPathFindingBFS pathFinding) {
        pathFinding.finish();
    }

    /**
     * 获取可达性地图的寻路对象
     */
    protected MaidPathFindingBFS getOrCreateArrivalMap(ServerLevel worldIn, EntityMaid maid) {
        return new MaidPathFindingBFS(maid.getNavigation().getNodeEvaluator(), worldIn, maid);
    }

    // 获取工作的搜寻中心点
    private BlockPos getWorkSearchPos(EntityMaid maid) {
        if (maid.hasRestriction()) {
            // 当且仅当开启home模式，并且工作点在工作范围内才返回最近工作点
            if (this.currentWorkPos != null && maid.isWithinRestriction(currentWorkPos)) {
                return this.currentWorkPos;
            } else {
                return maid.getRestrictCenter();
            }
        } else {
            return maid.blockPosition();
        }
    }

    private boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    /**
     * 判定条件
     *
     * @param worldIn  当前实体所处的 world
     * @param entityIn 当前需要移动的实体
     * @param pos      当前检索的 pos
     * @return 是否符合判定条件
     */
    protected abstract boolean shouldMoveTo(ServerLevel worldIn, EntityMaid entityIn, BlockPos pos);

    @Deprecated(forRemoval = true)
    protected boolean checkPathReach(EntityMaid maid, BlockPos pos) {
        return maid.canPathReach(pos);
    }

    protected boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos) {
        return pathFinding.canPathReach(pos);
    }
}
