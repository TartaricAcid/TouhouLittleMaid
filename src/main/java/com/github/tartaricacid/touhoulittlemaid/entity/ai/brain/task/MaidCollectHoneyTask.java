package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.util.Comparator;

public class MaidCollectHoneyTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 100;
    private final float speed;
    private final int closeEnoughDist;

    public MaidCollectHoneyTask(float speed, int closeEnoughDist) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.speed = speed;
        this.closeEnoughDist = closeEnoughDist;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (super.checkExtraStartConditions(worldIn, maid) && this.maidStateConditions(maid)) {
            BlockPos beehivePos = findBeehive(worldIn, maid);
            if (beehivePos != null && maid.isWithinRestriction(beehivePos)) {
                if (beehivePos.distToCenterSqr(maid.position()) < Math.pow(this.closeEnoughDist, 2)) {
                    maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(beehivePos));
                    return true;
                }
                BehaviorUtils.setWalkAndLookTargetMemories(maid, beehivePos, speed, 1);
                this.setNextCheckTickCount(5);
            } else {
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
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
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private void collectHoneyBottle(ServerLevel level, EntityMaid maid, CombinedInvWrapper maidAvailableInv, BlockState hiveBlockState, BlockPos hivePos) {
        ItemStack bottle = ItemsUtil.getStack(maidAvailableInv, stack -> stack.is(Items.GLASS_BOTTLE));
        if (!bottle.isEmpty()) {
            ItemStack honeyBottle = new ItemStack(Items.HONEY_BOTTLE);
            ItemStack result = ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyBottle, true);
            // 背包满了就不收集了
            if (!result.isEmpty()) {
                return;
            }
            bottle.shrink(1);
            level.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyBottle, false);
            resetHoneyLevel(level, hiveBlockState, hivePos);
            maid.swing(InteractionHand.MAIN_HAND);
        }
    }

    private boolean collectHoneyComb(ServerLevel level, EntityMaid maid, CombinedInvWrapper maidAvailableInv, BlockState hiveBlockState, BlockPos hivePos) {
        boolean hasShears = maid.getMainHandItem().canPerformAction(ToolActions.SHEARS_HARVEST);
        if (hasShears) {
            ItemStack honeyComb = new ItemStack(Items.HONEYCOMB, 3);
            ItemStack result = ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyComb, true);
            // 背包满了就不收集了
            if (!result.isEmpty()) {
                return false;
            }
            level.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
            ItemHandlerHelper.insertItemStacked(maidAvailableInv, honeyComb, false);
            resetHoneyLevel(level, hiveBlockState, hivePos);
            maid.swing(InteractionHand.MAIN_HAND);
            maid.getMainHandItem().hurtAndBreak(1, maid, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            return true;
        }
        return false;
    }

    @Nullable
    private BlockPos findBeehive(ServerLevel world, EntityMaid maid) {
        BlockPos blockPos = maid.blockPosition();
        PoiManager poiManager = world.getPoiManager();
        int range = (int) maid.getRestrictRadius();
        return poiManager.getInRange(type -> type.is(PoiTypeTags.BEE_HOME), blockPos, range, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos).filter(pos -> canCollectHoney(world, pos))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(blockPos))).orElse(null);
    }

    private boolean canCollectHoney(ServerLevel world, BlockPos hivePos) {
        return world.getBlockState(hivePos).getValue(BeehiveBlock.HONEY_LEVEL) >= 5;
    }

    public void resetHoneyLevel(Level level, BlockState state, BlockPos pos) {
        level.setBlock(pos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0), Block.UPDATE_ALL);
    }

    private boolean maidStateConditions(EntityMaid maid) {
        return !maid.isInSittingPose() && !maid.isSleeping() && !maid.isLeashed() && !maid.isPassenger();
    }
}
