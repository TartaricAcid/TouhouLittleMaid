package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHammer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

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
        BlockPos pos = ItemHammer.getStoreBlockPos(maid.getHeldItemMainhand());
        if (pos == null) {
            return;
        }
        if (maid.world.isBlockLoaded(pos) && !maid.world.isAirBlock(pos) && maid.isWithinHomeDistanceFromPosition(pos)) {
            if (maid.getDistanceSqToCenter(pos) <= DISTANCE_SQ) {
                // FIXME: 2021/1/4 目前任意方块都能破坏
                maid.destroyBlock(pos, true);
                maid.swingArm(EnumHand.MAIN_HAND);
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
        BlockPos pos = ItemHammer.getStoreBlockPos(maid.getHeldItemMainhand());
        if (pos == null) {
            return false;
        }
        return maid.world.isBlockLoaded(pos) && maid.isWithinHomeDistanceFromPosition(pos);
    }

    private boolean hasHammerWithPos(AbstractEntityMaid maid) {
        ItemStack stack = maid.getHeldItemMainhand();
        if (stack.getItem() != MaidItems.HAMMER) {
            return false;
        }
        return ItemHammer.getStoreBlockPos(stack) != null;
    }
}
