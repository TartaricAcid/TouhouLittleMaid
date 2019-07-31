package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import java.util.List;

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

        // 最后选取合适的种植方块
        return searchForDestination();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !maid.guiOpening && this.currentTask != TASK.NONE && !maid.isSitting() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {

        // 先尝试移动到此处
        tryMoveToDestination(9.0d, 40);
        boolean shouldLook = true;

        // 先判定女仆是否在耕地上方
        if (this.getIsAboveDestination()) {
            World world = this.maid.world;
            BlockPos pos = this.destinationBlock.up();

            // 如果当前任务为收获，并且 canHarvest，执行收获逻辑
            if (this.currentTask == TASK.MOVING) {
                IBlockState state = world.getBlockState(pos);
                for (TileEntityGrid grid : getTilesIn(world, pos, state)) {
                    if (grid.interact(maid.getAvailableInv(false), maid, false)) {
                        break;
                    }
                }
            }

            this.currentTask = TASK.NONE;
            this.runDelay = 2;
        }

        // 女仆头部朝向逻辑
        if (shouldLook) {
            // 女仆盯着耕地
            this.maid.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.maid.getVerticalFaceSpeed());
        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        pos = pos.up();

        IBlockState stateUp2 = worldIn.getBlockState(pos.up());

        // 该位置不可到达时返回 false
        if (!stateUp2.getBlock().isPassable(worldIn, pos.up())) {
            return false;
        }

        IBlockState stateUp = worldIn.getBlockState(pos);
        for (TileEntityGrid grid : getTilesIn(worldIn, pos, stateUp)) {
            if (grid.updateMode(null) != Mode.UNKNOWN && grid.interact(maid.getAvailableInv(false), maid, true)) {
                this.currentTask = TASK.MOVING;
                return true;
            }
        }
        return false;
    }

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
     * 检索指定范围内是否有合适方块
     */
    private boolean searchForDestination() {
        BlockPos blockpos = new BlockPos(this.maid);

        for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < this.searchLength; ++l) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);

                        // 如果方块在 Home 范围内，而且方块符合条件
                        if (this.maid.isWithinHomeDistanceFromPosition(blockpos1) && this.shouldMoveTo(this.maid.world, blockpos1)) {
                            this.destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * 女仆尝试移动到此处
     *
     * @param minDistanceSq
     *            最小移动距离
     * @param interval
     *            尝试移动的间隔时间
     */
    private void tryMoveToDestination(double minDistanceSq, int interval) {
        if (maid.getDistanceSqToCenter(this.destinationBlock.up()) > Math.sqrt(minDistanceSq)) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % interval == 0) {
                maid.getNavigator().tryMoveToXYZ((this.destinationBlock.getX()) + 0.5D, this.destinationBlock.getY() + 1, (this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        }
        else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }

    /**
     * 种植状态
     */
    private enum TASK {
        MOVING,
        // 无状态
        NONE
    }
}
