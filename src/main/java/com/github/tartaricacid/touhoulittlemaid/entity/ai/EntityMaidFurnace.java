package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemFurnaceGuide;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.ParametersAreNonnullByDefault;

public class EntityMaidFurnace extends EntityAIMoveToBlock {
    private static final int DISTANCE_SQ = 2 * 2;
    private final AbstractEntityMaid maid;

    public EntityMaidFurnace(AbstractEntityMaid entityMaid, float speed) {
        super(entityMaid, speed, 8);
        this.maid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        // 一些状态判定，如果状态不对，不进行拾取
        if (maid.isSitting() || maid.isSleep() || !maid.isTamed()) {
            return false;
        }
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        } else {
            this.runDelay = 80 + maid.getRNG().nextInt(40);
            if (maid.getHeldItemMainhand().getItem() == MaidItems.FURNACE_GUIDE) {
                return this.searchForDestination();
            } else {
                return false;
            }
        }
    }

    @Override
    public void updateTask() {
        if (maid.getDistanceSqToCenter(this.destinationBlock.up()) > DISTANCE_SQ) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;
            if (this.timeoutCounter % 40 == 0) {
                maid.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        } else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }

        if (this.getIsAboveDestination()) {
            TileEntity te = maid.world.getTileEntity(destinationBlock);
            if (te instanceof TileEntityFurnace) {
                TileEntityFurnace furnace = (TileEntityFurnace) te;
                IItemHandler maidInv = maid.getAvailableInv(false);
                ItemStackHandler guideInv = ItemFurnaceGuide.getGuideInv(maid.getHeldItemMainhand());
                IItemHandler output = furnace.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                IItemHandler fuel = furnace.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.WEST);
                IItemHandler input = furnace.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

                boolean swingHand = false;

                // 先把产物清空
                if (output != null) {
                    for (int i = 0; i < output.getSlots(); i++) {
                        int[] slotIndex = furnace.getSlotsForFace(EnumFacing.DOWN);
                        if (furnace.canExtractItem(slotIndex[i], output.getStackInSlot(i), EnumFacing.DOWN)) {
                            int beforeCount = output.getStackInSlot(i).getCount();
                            int afterCount = ItemHandlerHelper.insertItemStacked(maidInv, output.getStackInSlot(i).copy(), false).getCount();
                            if (beforeCount != afterCount) {
                                output.extractItem(i, beforeCount - afterCount, false);
                                swingHand = true;
                            }
                        }
                    }
                }

                // 塞入燃料
                if (fuel != null) {
                    for (int i = 0; i < fuel.getSlots(); i++) {
                        for (int j = 8; j < guideInv.getSlots(); j++) {
                            ItemStack fuelList = guideInv.getStackInSlot(j);
                            int slot = ItemFindUtil.findStackSlot(maidInv, s -> s.isItemEqualIgnoreDurability(fuelList));
                            if (slot != -1) {
                                int beforeCount = maidInv.getStackInSlot(slot).getCount();
                                int afterCount = ItemHandlerHelper.insertItemStacked(fuel, maidInv.getStackInSlot(slot).copy(), false).getCount();
                                if (beforeCount != afterCount) {
                                    maidInv.extractItem(slot, beforeCount - afterCount, false);
                                    swingHand = true;
                                }
                            }
                        }
                    }
                }

                // 接着开始塞入烧制物品
                if (input != null) {
                    for (int i = 0; i < input.getSlots(); i++) {
                        // 塞入烧制物品
                        for (int j = 0; j < 8; j++) {
                            ItemStack inputList = guideInv.getStackInSlot(j);
                            int slot = ItemFindUtil.findStackSlot(maidInv, s -> s.isItemEqualIgnoreDurability(inputList));
                            if (slot != -1) {
                                int beforeCount = maidInv.getStackInSlot(slot).getCount();
                                int afterCount = ItemHandlerHelper.insertItemStacked(input, maidInv.getStackInSlot(slot).copy(), false).getCount();
                                if (beforeCount != afterCount) {
                                    maidInv.extractItem(slot, beforeCount - afterCount, false);
                                    swingHand = true;
                                }
                            }
                        }
                    }
                }

                if (swingHand) {
                    maid.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityFurnace) {
            IBlockState state = worldIn.getBlockState(pos);
            return state.getBlock() == Blocks.FURNACE || state.getBlock() == Blocks.LIT_FURNACE;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (maid.isSitting() || maid.isSleep()) {
            return false;
        }
        return super.shouldContinueExecuting() && maid.getHeldItemMainhand().getItem() == MaidItems.FURNACE_GUIDE;
    }
}
