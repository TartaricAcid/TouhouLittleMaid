package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.world.server.ServerWorld;

public abstract class MaidMoveToBlockTask extends Task<EntityMaid> {
    private static final int MAX_DELAY_TIME = 200;
    private final float movementSpeed;
    private final int searchLength;
    private final int closeEnoughDist;
    private int runDelay;

    public MaidMoveToBlockTask(float speedIn, int length, int closeEnoughDist) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
        this.movementSpeed = speedIn;
        this.searchLength = length;
        this.closeEnoughDist = closeEnoughDist;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        }
        this.runDelay = MAX_DELAY_TIME + owner.getRandom().nextInt(MAX_DELAY_TIME);
        return true;
    }

    protected final void searchForDestination(ServerWorld worldIn, EntityMaid entityIn) {
        BlockPos originPos = new BlockPos(entityIn.position());
        for (int y = 0; y <= 1; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < this.searchLength; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        BlockPos blockPos = originPos.offset(x, y - 1, z);
                        if (shouldMoveTo(worldIn, entityIn, blockPos)) {
                            BrainUtil.setWalkAndLookTargetMemories(entityIn, blockPos, this.movementSpeed,
                                    (int) Math.min(0, this.closeEnoughDist - 0.5));
                            entityIn.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(blockPos));
                            this.runDelay = 5;
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
}
