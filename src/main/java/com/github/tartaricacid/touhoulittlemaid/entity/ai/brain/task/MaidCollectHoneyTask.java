package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.Comparator;

public class MaidCollectHoneyTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 100;
    private final float speed;
    private final int closeEnoughDist;

    public MaidCollectHoneyTask(float speed, int closeEnoughDist) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_ABSENT));
        this.speed = speed;
        this.closeEnoughDist = closeEnoughDist;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid) && this.maidStateConditions(maid)) {
            BlockPos beehivePos = findBeehive(worldIn, maid);
            if (beehivePos != null && maid.isWithinRestriction(beehivePos)) {
                if (beehivePos.distSqr(maid.blockPosition()) < Math.pow(this.closeEnoughDist, 2)) {
                    maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosWrapper(beehivePos));
                    return true;
                }
                BrainUtil.setWalkAndLookTargetMemories(maid, beehivePos, speed, 1);
                this.setNextCheckTickCount(5);
            } else {
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            }
        }
        return false;
    }

    @Override
    protected void start(ServerWorld level, EntityMaid maid, long gameTime) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(target -> {
            BlockPos hivePos = target.currentBlockPosition();
            BlockState hiveBlockState = level.getBlockState(hivePos);
            if (hiveBlockState.getValue(BeehiveBlock.HONEY_LEVEL) < 5) {
                return;
            }
            CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);
            if (!this.collectHoneyComb(level, maid, maidAvailableInv, hiveBlockState, hivePos)) {
                this.collectHoneyBottle(level, maid, maidAvailableInv, hiveBlockState, hivePos);
            }
        });
    }

    private void collectHoneyBottle(ServerWorld level, EntityMaid maid, CombinedInvWrapper maidAvailableInv, BlockState hiveBlockState, BlockPos hivePos) {
        ItemStack bottle = ItemsUtil.getStack(maidAvailableInv, stack -> stack.getItem() == Items.GLASS_BOTTLE);
        if (!bottle.isEmpty()) {
            ItemStack honeyBottle = new ItemStack(Items.HONEY_BOTTLE);
            ItemStack result = ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyBottle, true);
            // 背包满了就不收集了
            if (!result.isEmpty()) {
                return;
            }
            bottle.shrink(1);
            level.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyBottle, false);
            resetHoneyLevel(level, hiveBlockState, hivePos);
            maid.swing(Hand.MAIN_HAND);
            maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }

    private boolean collectHoneyComb(ServerWorld level, EntityMaid maid, CombinedInvWrapper maidAvailableInv, BlockState hiveBlockState, BlockPos hivePos) {
        boolean hasShears = maid.getMainHandItem().getItem() == Items.SHEARS;
        if (hasShears) {
            ItemStack honeyComb = new ItemStack(Items.HONEYCOMB, 3);
            ItemStack result = ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyComb, true);
            // 背包满了就不收集了
            if (!result.isEmpty()) {
                return false;
            }
            level.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyComb, false);
            resetHoneyLevel(level, hiveBlockState, hivePos);
            maid.swing(Hand.MAIN_HAND);
            maid.getMainHandItem().hurtAndBreak(1, maid, e -> e.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            return true;
        }
        return false;
    }

    @Nullable
    private BlockPos findBeehive(ServerWorld world, EntityMaid maid) {
        BlockPos blockPos = maid.blockPosition();
        PointOfInterestManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type == PointOfInterestType.BEEHIVE || type == PointOfInterestType.BEE_NEST, blockPos, range, PointOfInterestManager.Status.ANY)
                .map(PointOfInterest::getPos).filter(pos -> canCollectHoney(world, pos))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(blockPos))).orElse(null);
    }

    private boolean canCollectHoney(ServerWorld world, BlockPos hivePos) {
        return world.getBlockState(hivePos).getValue(BeehiveBlock.HONEY_LEVEL) >= 5;
    }

    public void resetHoneyLevel(World level, BlockState state, BlockPos pos) {
        level.setBlock(pos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0), Constants.BlockFlags.DEFAULT);
    }

    private boolean maidStateConditions(EntityMaid maid) {
        return !maid.isInSittingPose() && !maid.isSleeping() && !maid.isLeashed() && !maid.isPassenger();
    }
}
