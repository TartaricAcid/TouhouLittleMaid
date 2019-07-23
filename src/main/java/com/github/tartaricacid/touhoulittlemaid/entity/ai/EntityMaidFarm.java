package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EntityMaidFarm extends EntityAIMoveToBlock {
    private final EntityMaid entityMaid;
    private final int searchLength;
    private boolean hasFarmItem;
    /**
     * 0 => 收获, 1 => 重新种植, -1 => 无
     */
    private int currentTask;

    public EntityMaidFarm(EntityMaid entityMaid, double speedIn) {
        super(entityMaid, speedIn, 16);
        searchLength = 16;
        this.entityMaid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        // 模式判定，如果模式不对，或者处于待命状态
        if (entityMaid.guiOpening || entityMaid.getMode() != MaidMode.FARM || entityMaid.isSitting()) {
            return false;
        }

        // 而后判定延迟，任务，还有是否有对应物品
        if (this.runDelay <= 0) {
            this.currentTask = -1;
            this.hasFarmItem = this.entityMaid.isFarmItemInInventory();
        }

        // 距离下次检索的延时
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        } else {
            this.runDelay = 150 + this.entityMaid.getRNG().nextInt(60);
            return searchForDestination();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !entityMaid.guiOpening && this.currentTask >= 0 && entityMaid.getMode() == MaidMode.FARM &&
                !entityMaid.isSitting() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        boolean shouldLook = true;

        // 先判定女仆是否在耕地上方
        if (this.getIsAboveDestination()) {
            World world = this.entityMaid.world;
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            // 如果当前任务为收获，并且方块为作物而且长到最大阶段，破坏它
            if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(iblockstate)) {
                // 手部动画
                entityMaid.swingArm(EnumHand.MAIN_HAND);
                world.destroyBlock(blockpos, true);
            }

            // 如果当前任务为种植，并且种植处为空气
            else if (this.currentTask == 1 && iblockstate.getMaterial() == Material.AIR) {
                // 先检查女仆的背包是不是 null
                IItemHandler itemHandler = entityMaid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (itemHandler == null) {
                    return;
                }

                shouldLook = false;
                // 开始遍历女仆背包，尝试种植作物
                for (int i = 0; i < itemHandler.getSlots(); ++i) {
                    ItemStack itemstack = itemHandler.getStackInSlot(i);

                    if (!itemstack.isEmpty() && itemstack.getItem() instanceof IPlantable) {
                        if (((IPlantable) itemstack.getItem()).getPlantType(world, blockpos) == EnumPlantType.Crop) {
                            IBlockState state = ((IPlantable) itemstack.getItem()).getPlant(world, blockpos);
                            if (state.getBlock() == Blocks.AIR) {
                                continue;
                            }
                            world.setBlockState(blockpos, state, 3);
                            // 种植成功！扣除物品
                            itemstack.shrink(1);
                            // 手部动画
                            entityMaid.swingArm(EnumHand.MAIN_HAND);
                            shouldLook = true;
                            break;
                        }
                    }
                }
            }

            this.currentTask = -1;
            this.runDelay = 2;
        }
        if (shouldLook) {
            // 女仆盯着耕地
            this.entityMaid.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5D, (double) (this.destinationBlock.getY() + 1),
                    (double) this.destinationBlock.getZ() + 0.5D, 10.0F, (float) this.entityMaid.getVerticalFaceSpeed());
        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();

        // 耕地方块？
        if (block == Blocks.FARMLAND) {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            boolean blockIsOkay;
            boolean taskIsOkay;

            // 上面有作物，达到了最大生长阶段，当前无其他任务
            blockIsOkay = block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(iblockstate);
            taskIsOkay = this.currentTask == 0 || this.currentTask < 0;
            if (blockIsOkay && taskIsOkay) {
                this.currentTask = 0;
                return true;
            }

            // 上面是空气，现在没有其他任务
            blockIsOkay = iblockstate.getMaterial() == Material.AIR && this.hasFarmItem;
            taskIsOkay = this.currentTask == 1 || this.currentTask < 0;
            if (blockIsOkay && taskIsOkay) {
                this.currentTask = 1;
                return true;
            }
        }
        return false;
    }

    /**
     * 检索指定范围内是否有合适方块的
     */
    private boolean searchForDestination() {
        BlockPos blockpos = new BlockPos(this.entityMaid);

        for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < this.searchLength; ++l) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);

                        // 如果方块在 Home 范围内，而且方块符合条件
                        if (this.entityMaid.isWithinHomeDistanceFromPosition(blockpos1) && this.shouldMoveTo(this.entityMaid.world, blockpos1)) {
                            this.destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
