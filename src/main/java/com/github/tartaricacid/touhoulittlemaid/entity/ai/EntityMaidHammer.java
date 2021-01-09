package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHammer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class EntityMaidHammer extends EntityAIBase {
    private static final int DISTANCE_SQ = 2 * 2;
    private final AbstractEntityMaid maid;
    private final float speed;

    private int countTime;

    public EntityMaidHammer(AbstractEntityMaid entityMaid, float speed) {
        this.maid = entityMaid;
        this.speed = speed;
        this.countTime = 5;
        setMutexBits(1 | 2);
    }

    @Override
    public boolean shouldExecute() {
        // 一些状态判定，如果状态不对，不进行拾取
        if (maid.isSitting() || maid.isSleep() || !maid.isTamed()) {
            return false;
        }

        // 计时判定，时间不到不进行拾取
        if (countTime > 0) {
            countTime--;
            return false;
        }
        countTime = 5;

        // 检查锤子
        return hasHammerWithPos(maid);
    }

    @Override
    public void updateTask() {
        BlockPos pos = ItemHammer.getStoreBlockPos(maid.getHeldItemOffhand());
        if (pos == null) {
            return;
        }
        if (maid.world.isBlockLoaded(pos) && !maid.world.isAirBlock(pos) && maid.isWithinHomeDistanceFromPosition(pos)) {
            if (maid.getDistanceSqToCenter(pos) <= DISTANCE_SQ) {
                // FIXME: 2021/1/4 目前任意方块都能破坏
                if (canHarvestBlock(maid, maid.world, pos)) {
                    maid.destroyBlock(pos, true);
                    maid.getHeldItemMainhand().damageItem(1, maid);
                    maid.swingArm(EnumHand.MAIN_HAND);
                }
            } else {
                if (maid.getNavigator().getPathToPos(pos) != null) {
                    maid.getLookHelper().setLookPosition(pos.getX(), pos.getY(), pos.getZ(), 30f, maid.getVerticalFaceSpeed());
                    maid.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), speed);
                }
            }
        }
    }

    @Override
    public void resetTask() {
        maid.getNavigator().clearPath();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (maid.isSitting() || maid.isSleep()) {
            return false;
        }
        BlockPos pos = ItemHammer.getStoreBlockPos(maid.getHeldItemOffhand());
        if (pos == null) {
            return false;
        }
        return maid.world.isBlockLoaded(pos) && maid.isWithinHomeDistanceFromPosition(pos);
    }

    private boolean hasHammerWithPos(AbstractEntityMaid maid) {
        ItemStack stack = maid.getHeldItemOffhand();
        if (stack.getItem() != MaidItems.HAMMER) {
            return false;
        }
        return ItemHammer.getStoreBlockPos(stack) != null;
    }

    private boolean canHarvestBlock(@Nonnull AbstractEntityMaid maid, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        state = state.getBlock().getActualState(state, world, pos);

        if (state.getMaterial().isToolNotRequired()) {
            return true;
        }

        ItemStack stack = maid.getHeldItemMainhand();
        Block block = state.getBlock();
        String tool = block.getHarvestTool(state);
        if (stack.isEmpty() || tool == null) {
            return false;
        }

        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, null, state);
        if (toolLevel < 0) {
            return false;
        }

        return toolLevel >= block.getHarvestLevel(state);
    }
}
