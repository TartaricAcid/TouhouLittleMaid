package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityMaidStorage extends EntityAIMoveToBlock {
    private final EntityMaid maid;
    private TASK currentTask;

    public EntityMaidStorage(EntityMaid entityMaid, double speedIn) {
        super(entityMaid, speedIn, 16);
        this.maid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        // 模式判定，如果模式不对，或者处于待命状态
        if (maid.guiOpening || maid.isSitting() || maid.isSleep()) {
            return false;
        }

        // 检查背包
        if (maid.getBackLevel() == EntityMaid.EnumBackPackLevel.EMPTY) {
            return false;
        }

        // 距离下次检索的延时
        if (--this.runDelay > 0) {
            return false;
        }

        // 随机设置 50 - 100 tick 的延时
        this.runDelay = 50 + this.maid.getRNG().nextInt(50);
        this.currentTask = TASK.NONE;

        // 背包半满了才进行放置
        IItemHandler handler = maid.getAvailableInv(true);
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (handler.getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        if (count > handler.getSlots() / 2) {
            return false;
        }

        // 最后选取方块
        return searchForDestination();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !maid.guiOpening && this.currentTask != TASK.NONE && !maid.isSitting() && !maid.isSleep() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        // 先尝试移动到此处
        tryMoveToDestination(1d, 40);

        // 先判定女仆是否在范围内
        if (this.getIsAboveDestination()) {
            World world = this.maid.world;
            BlockPos pos = this.destinationBlock;

            // 如果当前任务为移动
            if (this.currentTask == TASK.MOVING) {
                IItemHandler handler = maid.getAvailableInv(true);
                TileEntity te = world.getTileEntity(pos);
                if (handler != null && te instanceof TileEntityHopper) {
                    List<EntityItemFrame> itemFrames = world.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.up()));
                    int emptyCount = getHopperEmptySlotCount((TileEntityHopper) te);
                    if (itemFrames.size() > 0) {
                        hasFramesLogic(handler, itemFrames, emptyCount);
                    } else {
                        normalLogic(handler, emptyCount);
                    }
                }
            }
            this.currentTask = TASK.NONE;
            maid.swingArm(EnumHand.MAIN_HAND);
        }

        // 女仆盯着交互面板
        this.maid.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.maid.getVerticalFaceSpeed());
    }

    private void normalLogic(IItemHandler handler, int emptyCount) {
        for (int i = 7; i < handler.getSlots(); i++) {
            if (dropItemOnHopper(handler, i)) {
                // 如果空位小于 0，说明已经满了，先不要丢了
                emptyCount--;
                if (emptyCount <= 0) {
                    return;
                }
            }
        }
    }

    private void hasFramesLogic(IItemHandler handler, List<EntityItemFrame> itemFrames, int emptyCount) {
        ItemStack itemStack = itemFrames.get(0).getDisplayedItem();
        if (!itemStack.isEmpty()) {
            for (int i = 0; i < handler.getSlots(); i++) {
                if (itemStack.getItem() == handler.getStackInSlot(i).getItem()
                        && dropItemOnHopper(handler, i)) {
                    // 如果空位小于 0，说明已经满了，先不要丢了
                    emptyCount--;
                    if (emptyCount <= 0) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, @Nonnull BlockPos pos) {
        IBlockState stateUp2 = worldIn.getBlockState(pos.up());

        // 上方两格处不可通过时
        if (!stateUp2.getBlock().isPassable(worldIn, pos.up())) {
            return false;
        }

        // 可通过时，并且能够塞入
        if (canThrowIn(worldIn, pos)) {
            this.currentTask = TASK.MOVING;
            return true;
        }

        return false;
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

    private boolean canThrowIn(World worldIn, @Nonnull BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock() instanceof BlockHopper && BlockHopper.isEnabled(state.getBlock().getMetaFromState(state))) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityHopper) {
                List<EntityItemFrame> itemFrames = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.up()));
                if (itemFrames.size() > 0) {
                    ItemStack itemStack = itemFrames.get(0).getDisplayedItem();
                    if (itemStack.isEmpty()) {
                        return false;
                    }
                    IItemHandler handler = maid.getAvailableInv(true);
                    for (int i = 0; i < handler.getSlots(); i++) {
                        if (handler.getStackInSlot(i).getItem() == itemStack.getItem()) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    return !isHopperFull((TileEntityHopper) te);
                }
            }
        }
        return false;
    }

    private boolean isHopperFull(TileEntityHopper hopper) {
        return getHopperEmptySlotCount(hopper) == 0;
    }

    private int getHopperEmptySlotCount(TileEntityHopper hopper) {
        IItemHandler handler = hopper.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (handler == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack itemStack = handler.getStackInSlot(i);
            if (itemStack.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private boolean dropItemOnHopper(IItemHandler handler, int slotIndex) {
        World world = this.maid.world;
        BlockPos pos = this.destinationBlock;
        ItemStack stack = handler.extractItem(slotIndex, handler.getStackInSlot(slotIndex).getCount(), false);
        if (!stack.isEmpty()) {
            EntityItem entityitem = new EntityItem(this.maid.world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
            entityitem.motionX = 0;
            entityitem.motionY = 0;
            entityitem.motionZ = 0;
            entityitem.setPickupDelay(40);
            world.spawnEntity(entityitem);
            return true;
        }
        return false;
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
