package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import java.util.List;
import java.util.stream.Collectors;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class EntityMaidFarm extends EntityAIMoveToBlock {
    private final AbstractEntityMaid maid;
    private final int searchLength;
    private NonNullList<ItemStack> seeds = NonNullList.create();
    private List<FarmHandler> handlers;
    private FarmHandler activeHandler;
    private ItemStack activeSeed = ItemStack.EMPTY;
    /**
     * 0 => 收获, 1 => 重新种植, -1 => 无
     */
    private int currentTask;

    public EntityMaidFarm(AbstractEntityMaid entityMaid, double speedIn) {
        super(entityMaid, speedIn, 16);
        searchLength = 16;
        this.maid = entityMaid;
    }

    @Override
    public boolean shouldExecute() {
        // 模式判定，如果模式不对，或者处于待命状态
        if (maid.isSitting()) {
            return false;
        }

        // 距离下次检索的延时
        if (--this.runDelay > 0) {
            return false;
        }

        this.runDelay = 10 + this.maid.getRNG().nextInt(60);
        this.currentTask = -1;

        handlers = LittleMaidAPI.getFarmHandlers().stream().filter(h -> h.canExecute(maid)).collect(Collectors.toList());
        if (handlers.isEmpty()) {
            return false;
        }

        seeds.clear();
        IItemHandler inv = maid.getAvailableInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (handlers.stream().anyMatch(h -> h.isSeed(stack))) {
                seeds.add(stack);
            }
        }
        return searchForDestination();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return activeHandler != null && this.currentTask >= 0 && !maid.isSitting() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        if (activeHandler == null) {
            return; // 我也不知道为什么会这样
        }
        if (maid.getDistanceSqToCenter(this.destinationBlock.up()) > 1.5D) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0) {
                maid.getNavigator().tryMoveToXYZ((this.destinationBlock.getX()) + 0.5D, this.destinationBlock.getY() + 1, (this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        }
        else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
        boolean shouldLook = true;

        // 先判定女仆是否在耕地上方
        if (this.getIsAboveDestination()) {
            World world = this.maid.world;
            BlockPos pos = this.destinationBlock.up();
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            // 如果当前任务为收获，并且方块为作物而且长到最大阶段，破坏它
            if (this.currentTask == 0 && activeHandler.canHarvest(maid, world, pos, state)) {
                activeHandler.harvest(maid, world, pos, state);
                // 手部动画
                maid.swingArm(EnumHand.MAIN_HAND);
            }

            // 如果当前任务为种植，并且种植处为空气
            else if (this.currentTask == 1 && activeHandler.canPlant(maid, world, pos, activeSeed)) {
                shouldLook = false;
                // 先检查种子是否还在女仆的背包中
                IItemHandlerModifiable itemHandler = maid.getAvailableInv();
                int slot = ItemFindUtil.findItem(itemHandler, s -> s == activeSeed);

                // 尝试种植作物
                if (slot >= 0) {
                    ItemStack remain = activeHandler.plant(maid, world, pos, activeSeed);
                    itemHandler.setStackInSlot(slot, remain);
                    activeSeed = remain;
                    // 手部动画
                    maid.swingArm(EnumHand.MAIN_HAND);
                    shouldLook = true;
                }
            }

            this.currentTask = -1;
            this.runDelay = 2;
        }
        if (shouldLook) {
            // 女仆盯着耕地
            this.maid.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.maid.getVerticalFaceSpeed());
        }
    }

    // 目前的问题： 可可豆会挡住实体行走
    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        pos = pos.up();
        IBlockState stateUp = worldIn.getBlockState(pos);
        IBlockState stateUp2 = worldIn.getBlockState(pos.up());
        if (!stateUp2.getBlock().isPassable(worldIn, pos.up())) {
            return false; // 该位置不可到达
        }

        // 当前无其他任务，上面有可收获作物
        if (this.currentTask == 0 || this.currentTask == -1) {
            for (FarmHandler handler : handlers) {
                if (handler.canHarvest(maid, worldIn, pos, stateUp)) {
                    activeHandler = handler;
                    this.currentTask = 0;
                    return true;
                }
            }
        }

        // 当前无其他任务，上面可种植
        boolean taskIsOkay = this.currentTask == 1 || this.currentTask == -1;
        if (taskIsOkay && !seeds.isEmpty()) {
            for (FarmHandler handler : handlers) {
                for (ItemStack seed : seeds) {
                    if (handler.isSeed(seed) && handler.canPlant(maid, worldIn, pos, seed)) {
                        this.currentTask = 1;
                        activeHandler = handler;
                        activeSeed = seed;
                        return true;
                    }
                }
            }
        }
        return false;
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
}
