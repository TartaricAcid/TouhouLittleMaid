package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import com.github.tartaricacid.touhoulittlemaid.util.ItemFindUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class EntityMaidFarm extends EntityAIMoveToBlock {
    private final AbstractEntityMaid maid;
    /**
     * 当前可种植的种子列表
     */
    private NonNullList<ItemStack> seeds = NonNullList.create();
    /**
     * 判定当前种植逻辑可用的 FarmHandler
     */
    private List<FarmHandler> handlers;
    /**
     * 当前执行的 FarmHandler
     */
    private FarmHandler activeHandler;
    /**
     * 当前执行种植的种子
     */
    private ItemStack activeSeed = ItemStack.EMPTY;
    /**
     * 当前的农场模式
     */
    private FarmHandler.Mode mode;
    private TASK currentTask;

    public EntityMaidFarm(AbstractEntityMaid entityMaid, double speedIn, FarmHandler.Mode mode) {
        super(entityMaid, speedIn, 16);
        this.maid = entityMaid;
        this.mode = mode;
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

        // 随机设置 10 - 70 tick 的延时
        this.runDelay = 10 + this.maid.getRNG().nextInt(60);
        this.currentTask = TASK.NONE;

        // 通过 LittleMaidAPI 获取开启了 canExecute 且模式正确的 FarmHandlers
        handlers = LittleMaidAPI.getFarmHandlers().stream().filter(h -> h.canExecute(maid) && h.getMode() == mode).collect(Collectors.toList());
        if (handlers.isEmpty()) {
            return false;
        }

        // 先清空种子列表
        seeds.clear();
        // 而后通过 FarmHandlers 获取到玩家背包的内所有可以种植的种子
        IItemHandler inv = maid.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (handlers.stream().anyMatch(h -> h.isSeed(stack))) {
                seeds.add(stack);
            }
        }

        // 最后选取合适的种植方块
        return searchForDestination();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return activeHandler != null && this.currentTask != TASK.NONE && !maid.isSitting() && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        if (activeHandler == null) {
            // 我也不知道为什么会这样
            return;
        }

        // 先尝试移动到此处的判定
        tryMoveToDestination(activeHandler.getMinDistanceSq(), 40);
        boolean shouldLook = true;

        // 先判定女仆是否在范围内
        if (this.getIsAboveDestination()) {
            World world = this.maid.world;
            BlockPos pos = this.destinationBlock.up();
            IBlockState state = world.getBlockState(pos);

            // 如果当前任务为收获，并且 canHarvest，执行收获逻辑
            if (this.currentTask == TASK.HARVEST && activeHandler.canHarvest(maid, world, pos, state)) {
                harvestLogic(world, pos, state);
            }

            // 如果当前任务为种植，并且种植处 canPlant，执行种植逻辑
            else if (this.currentTask == TASK.PLANT && activeHandler.canPlant(maid, world, pos, activeSeed)) {
                shouldLook = plantLogic(world, pos);
            }

            this.currentTask = TASK.NONE;
            this.runDelay = 5;
        }

        // 女仆头部朝向逻辑
        if (shouldLook) {
            // 女仆盯着耕地
            this.maid.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1,
                    this.destinationBlock.getZ() + 0.5D, 10.0F, this.maid.getVerticalFaceSpeed());
        }
    }


    @Override
    protected boolean shouldMoveTo(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        pos = pos.up();
        IBlockState stateUp = worldIn.getBlockState(pos);
        IBlockState stateUp2 = worldIn.getBlockState(pos.up());

        // 该位置不可到达时返回 false
        if (!stateUp2.getBlock().isPassable(worldIn, pos.up())) {
            return false;
        }

        boolean taskDestroyIsOkay = this.currentTask == TASK.HARVEST || this.currentTask == TASK.NONE;
        if (taskDestroyIsOkay && maid.canDestroyBlock(pos)) {
            // 遍历 FarmHandler 找到能够处理此作物的 FarmHandler
            for (FarmHandler handler : handlers) {
                if (handler.canHarvest(maid, worldIn, pos, stateUp) && maid.getNavigator().getPathToPos(pos) != null) {
                    activeHandler = handler;
                    this.currentTask = TASK.HARVEST;
                    return true;
                }
            }
        }

        boolean taskPlaceIsOkay = this.currentTask == TASK.PLANT || this.currentTask == TASK.NONE;
        if (taskPlaceIsOkay && !seeds.isEmpty() && maid.canPlaceBlock(pos, stateUp)) {
            // 遍历 FarmHandler 的 seeds 找到能够处理种植 FarmHandler 和 Seed
            for (FarmHandler handler : handlers) {
                for (ItemStack seed : seeds) {
                    if (handler.isSeed(seed) && handler.canPlant(maid, worldIn, pos, seed) && maid.getNavigator().getPathToPos(pos) != null) {
                        this.currentTask = TASK.PLANT;
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
     * 种植逻辑
     *
     * @return 女仆是否看向此方块
     */
    private boolean plantLogic(World world, BlockPos pos) {
        boolean shouldLook = false;
        // 先检查种子是否还在女仆的背包中
        IItemHandlerModifiable itemHandler = maid.getAvailableInv(true);
        int slot = ItemFindUtil.findStackSlot(itemHandler, s -> s == activeSeed);

        // 尝试种植作物
        if (slot >= 0) {
            ItemStack remain = activeHandler.plant(maid, world, pos, activeSeed);
            itemHandler.setStackInSlot(slot, remain);
            activeSeed = remain;
            // 手部动画
            maid.swingArm(EnumHand.MAIN_HAND);
            shouldLook = true;
        }

        return shouldLook;
    }

    /**
     * 收获逻辑
     */
    private void harvestLogic(World world, BlockPos pos, IBlockState state) {
        activeHandler.harvest(maid, world, pos, state);
        // 手部动画
        maid.swingArm(EnumHand.MAIN_HAND);
    }

    /**
     * 种植状态
     */
    private enum TASK {
        // 种植状态
        PLANT,
        // 收获状态
        HARVEST,
        // 无状态
        NONE
    }
}
