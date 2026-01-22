package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.edible.MaidEdibleBlockAction;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.edible.MaidEdibleBlockManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.Optional;

public class MaidStealEdibleMoveBlockTask extends MaidMoveToBlockTask {
    /**
     * 女仆偷吃方块的水平搜索范围比较小
     */
    private static final int HORIZONTAL_SEARCH_RANGE = 5;
    /**
     * 当搜索成功后，女仆下一次偷吃的检查间隔
     */
    private static final int NEXT_CHECK_TICK_COUNT = 45 * 20;
    /**
     * 检查方块可达性的范围，默认检查寻路点周围 3x3x3 范围内的方块的可达性
     */
    private static final BoundingBox CHECK_RANGE = new BoundingBox(-1, -1, -1, 1, 1, 1);
    /**
     * 仅供开发调试，用来缩短女仆偷吃方块的冷却时间
     */
    @VisibleForDebug
    public static boolean DEBUG = false;

    private final MemoryModuleType<MaidEdibleBlockAction> action;

    private @Nullable ItemStack placedStack;

    public MaidStealEdibleMoveBlockTask(float movementSpeed) {
        super(movementSpeed, 2);
        this.setMaxCheckRate(NEXT_CHECK_TICK_COUNT);
        this.action = InitEntities.MAID_EDIBLE_BLOCK_ACTION.get();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        if (DEBUG) {
            return true;
        }
        return super.checkExtraStartConditions(worldIn, owner);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        Optional<MaidEdibleBlockAction> memory = maid.getBrain().getMemory(this.action);

        if (memory.isPresent() && memory.get() == MaidEdibleBlockAction.TRY_STEAL) {
            // 检查背包内有可放置食物么，有就切放置状态
            CombinedInvWrapper inv = maid.getAvailableInv(true);
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.isEmpty()) {
                    continue;
                }
                for (IMaidEdibleBlock edibleBlock : MaidEdibleBlockManager.getEdibleBlocks()) {
                    if (edibleBlock.canPlaceAsFood(maid, stack, i)) {
                        this.placedStack = stack;
                        maid.getBrain().setMemory(this.action, MaidEdibleBlockAction.TRY_PLACE);
                        this.searchForDestination(worldIn, maid);
                        return;
                    }
                }
            }

            // 如果没有，那么切偷吃状态
            maid.getBrain().setMemory(this.action, MaidEdibleBlockAction.TRY_STEAL);
        } else {
            // 初始状态为偷吃
            maid.getBrain().setMemory(this.action, MaidEdibleBlockAction.TRY_STEAL);
        }

        // 尝试搜索目标位置
        this.searchForDestination(worldIn, maid);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos);
        MaidEdibleBlockAction memory = maid.getBrain().getMemory(this.action).orElse(MaidEdibleBlockAction.TRY_STEAL);
        for (IMaidEdibleBlock edibleBlock : MaidEdibleBlockManager.getEdibleBlocks()) {
            if (memory == MaidEdibleBlockAction.TRY_PLACE) {
                if (this.placedStack == null) {
                    return false;
                }
                if (edibleBlock.shouldPlaceTo(maid, pos, blockState, this.placedStack)) {
                    return true;
                }
            } else {
                if (edibleBlock.shouldMoveTo(maid, pos, blockState)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos) {
        for (int x = CHECK_RANGE.minX(); x <= CHECK_RANGE.maxX(); x++) {
            for (int y = CHECK_RANGE.minY(); y <= CHECK_RANGE.maxY(); y++) {
                for (int z = CHECK_RANGE.minZ(); z <= CHECK_RANGE.maxZ(); z++) {
                    if (pathFinding.canPathReach(pos.offset(x, y, z))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected int getHorizontalSearchRange(EntityMaid maid) {
        int defaultRange = super.getHorizontalSearchRange(maid);
        return Math.min(defaultRange, HORIZONTAL_SEARCH_RANGE);
    }

    @Override
    protected void setNextCheckTickCount(int nextCheckTickCount) {
        super.setNextCheckTickCount(NEXT_CHECK_TICK_COUNT);
    }
}
