package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPotionGuide;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class EntityMaidBrewing extends EntityAIMoveToBlock {
    private static final int DISTANCE_SQ = 2 * 2;
    private final AbstractEntityMaid maid;

    public EntityMaidBrewing(AbstractEntityMaid entityMaid, float speed) {
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
            if (maid.getHeldItemMainhand().getItem() == MaidItems.POTION_GUIDE) {
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
            if (te instanceof TileEntityBrewingStand) {
                TileEntityBrewingStand brewingStand = (TileEntityBrewingStand) te;
                IItemHandler maidInv = maid.getAvailableInv(false);
                ItemStack guide = maid.getHeldItemMainhand();
                ItemStackHandler guideInv = ItemPotionGuide.getGuideInv(guide);
                @Nullable BlockPos guidePos = ItemPotionGuide.getGuidePos(guide);

                boolean swingHand = false;

                // 先判定坐标
                if (guidePos == null || !guidePos.equals(destinationBlock)) {
                    // 重置坐标和索引
                    ItemPotionGuide.setGuideIndex(guide, 0);
                    ItemPotionGuide.setGuidePos(guide, destinationBlock);
                }

                // 检查燃料
                int fuel = brewingStand.getField(1);
                if (fuel <= 0) {
                    ItemStack stack = ItemFindUtil.getStack(maidInv, s -> s.getItem() == Items.BLAZE_POWDER);
                    // 添加燃料
                    if (!stack.isEmpty()) {
                        brewingStand.setField(1, 20);
                        stack.shrink(1);
                        brewingStand.markDirty();
                        swingHand = true;
                    } else {
                        // 没有燃料？就不要进行酿造了
                        return;
                    }
                }

                // 检查当前酿造台是否完成了酿造
                int index = ItemPotionGuide.getGuideIndex(guide);
                int brewTime = brewingStand.getField(0);
                IItemHandler output = brewingStand.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

                if (index == 0 && brewTime <= 0 && output != null) {
                    for (int i = 0; i < output.getSlots(); i++) {
                        ItemStack stack = output.getStackInSlot(i);
                        if (brewingStand.canExtractItem(i, stack, EnumFacing.DOWN)) {
                            int beforeCount = stack.getCount();
                            int afterCount = ItemHandlerHelper.insertItemStacked(maidInv, stack.copy(), false).getCount();
                            if (beforeCount != afterCount) {
                                output.extractItem(i, beforeCount - afterCount, false);
                                swingHand = true;
                            }
                        }
                        // 完成了酿造，但是产物取不出来，结束酿造
                        if (!output.getStackInSlot(i).isEmpty()) {
                            return;
                        }
                    }
                }

                // 开始放入酿造物品
                IItemHandler side = brewingStand.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
                boolean noWaterBottle = true;

                // 如果是第一步，先放入水瓶
                if (index == 0 && side != null) {
                    for (int i = 0; i < maidInv.getSlots(); i++) {
                        ItemStack stack = maidInv.getStackInSlot(i);
                        if (stack.getItem() == Items.POTIONITEM && PotionUtils.getPotionFromItem(stack) == PotionTypes.WATER) {
                            int beforeCount = stack.getCount();
                            int afterCount = ItemHandlerHelper.insertItemStacked(side, stack.copy(), false).getCount();
                            if (beforeCount != afterCount) {
                                maidInv.extractItem(i, beforeCount - afterCount, false);
                                noWaterBottle = false;
                                swingHand = true;
                            }
                        }
                    }
                    // 没有放入任何水瓶，放弃酿造
                    if (noWaterBottle) {
                        return;
                    }
                }

                // 而后开始放药材
                IItemHandler up = brewingStand.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

                if (up != null && up.getStackInSlot(0).isEmpty()) {
                    ItemStack ingredient = guideInv.getStackInSlot(index);
                    // 如果标记的列表中有空，直接跳转到下一步
                    if (ingredient.isEmpty()) {
                        ItemPotionGuide.setGuideIndex(guide, (index + 1) % 6);
                        return;
                    }
                    int findSlot = ItemFindUtil.findStackSlot(maidInv, s -> s.isItemEqualIgnoreDurability(ingredient));
                    ItemStack findIngredient = maidInv.getStackInSlot(findSlot);
                    if (!findIngredient.isEmpty()) {
                        ItemStack copyFindIngredient = findIngredient.copy();
                        copyFindIngredient.setCount(1);
                        ItemHandlerHelper.insertItemStacked(up, copyFindIngredient, false);
                        maidInv.extractItem(findSlot, 1, false);
                        ItemPotionGuide.setGuideIndex(guide, (index + 1) % 6);
                        swingHand = true;
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
        if (te instanceof TileEntityBrewingStand) {
            IBlockState state = worldIn.getBlockState(pos);
            return state.getBlock() == Blocks.BREWING_STAND;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (maid.isSitting() || maid.isSleep()) {
            return false;
        }
        return super.shouldContinueExecuting() && maid.getHeldItemMainhand().getItem() == MaidItems.POTION_GUIDE;
    }
}
