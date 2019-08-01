package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.mcmp.MCMPCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid.Mode;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntityMaidGridInteract extends EntityAIMoveToBlock {
    private final EntityMaid maid;
    private final int searchLength;
    private TASK currentTask;

    public EntityMaidGridInteract(EntityMaid entityMaid, double speedIn) {
        super(entityMaid, speedIn, 16);
        searchLength = 16;
        this.maid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        // 模式判定，如果模式不对，或者处于待命状态
        if (maid.guiOpening || maid.isSitting()) {
            return false;
        }

        // 距离下次检索的延时
        if (--this.runDelay > 0) {
            return false;
        }

        // 随机设置 50 - 100 tick 的延时
        this.runDelay = 50 + this.maid.getRNG().nextInt(50);
        this.currentTask = TASK.NONE;

        // 最后选取方块
        return searchForDestination();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !maid.guiOpening && this.currentTask != TASK.NONE && !maid.isSitting() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        // 先尝试移动到此处
        tryMoveToDestination(2.25d, 40);

        // 先判定女仆是否在范围内
        if (this.getIsAboveDestination()) {
            World world = this.maid.world;
            BlockPos pos = this.destinationBlock.up();

            // 如果当前任务为移动
            if (this.currentTask == TASK.MOVING) {
                IBlockState state = world.getBlockState(pos);
                // 为 MCMultiPart 做的兼容，搜索一个坐标内的多个 Grid
                for (TileEntityGrid grid : getTilesIn(world, pos, state)) {
                    // 只要无法交互，直接跳出
                    if (grid.interact(maid.getAvailableInv(false), maid, false)) {
                        break;
                    }
                }
            }
            this.currentTask = TASK.NONE;
        }

        // 女仆盯着交互面板
        this.maid.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.maid.getVerticalFaceSpeed());
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        pos = pos.up();
        IBlockState stateUp2 = worldIn.getBlockState(pos.up());

        // 上方两格处不可通过时
        if (!stateUp2.getBlock().isPassable(worldIn, pos.up())) {
            return false;
        }

        // 可通过时，并且能够塞入
        IBlockState stateUp = worldIn.getBlockState(pos);
        for (TileEntityGrid grid : getTilesIn(worldIn, pos, stateUp)) {
            if (grid.updateMode(null) != Mode.UNKNOWN && grid.interact(maid.getAvailableInv(false), maid, true)) {
                this.currentTask = TASK.MOVING;
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前坐标内所有的 grid
     */
    private List<TileEntityGrid> getTilesIn(World worldIn, BlockPos pos, IBlockState state) {
        List<TileEntityGrid> grids = Lists.newArrayList();
        if (TouhouLittleMaid.MCMPCompat) {
            MCMPCompat.getPartTiles(worldIn, pos, state, grids);
        }
        if (state.getBlock() == MaidBlocks.GRID) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileEntityGrid) {
                grids.add((TileEntityGrid) tile);
            }
        }
        return grids;
    }

    /**
     * 女仆尝试移动到此处
     *
     * @param minDistanceSq 最小移动距离
     * @param interval      尝试移动的间隔时间
     */
    private void tryMoveToDestination(double minDistanceSq, int interval) {
        if (maid.getDistanceSqToCenter(this.destinationBlock.up()) > Math.sqrt(minDistanceSq)) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % interval == 0) {
                maid.getNavigator().tryMoveToXYZ((this.destinationBlock.getX()) + 0.5D, this.destinationBlock.getY() + 1, (this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        } else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }

    /**
     * 种植状态
     */
    private enum TASK {
        // 移动状态
        MOVING,
        // 无状态
        NONE
    }
}
