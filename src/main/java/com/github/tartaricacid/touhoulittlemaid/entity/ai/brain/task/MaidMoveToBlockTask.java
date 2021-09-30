package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;

public abstract class MaidMoveToBlockTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int searchLength;

    public MaidMoveToBlockTask(float movementSpeed, int searchLength) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
        this.movementSpeed = movementSpeed;
        this.searchLength = searchLength;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    protected final void searchForDestination(ServerWorld worldIn, EntityMaid maid) {
        BlockPos originPos = maid.blockPosition();
        for (int y = 0; y <= 1; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < this.searchLength; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        BlockPos blockPos = originPos.offset(x, y - 1, z);
                        if (shouldMoveTo(worldIn, maid, blockPos) && checkPathReach(maid, blockPos)) {
                            BrainUtil.setWalkAndLookTargetMemories(maid, blockPos, this.movementSpeed, 0);
                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(blockPos));
                            this.setNextCheckTickCount(5);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 判定条件
     *
     * @param worldIn  当前实体所处的 world
     * @param entityIn 当前需要移动的实体
     * @param pos      当前检索的 pos
     * @return 是否符合判定条件
     */
    protected abstract boolean shouldMoveTo(ServerWorld worldIn, EntityMaid entityIn, BlockPos pos);

    protected boolean checkPathReach(EntityMaid maid, BlockPos pos) {
        return maid.canPathReach(pos);
    }
}
