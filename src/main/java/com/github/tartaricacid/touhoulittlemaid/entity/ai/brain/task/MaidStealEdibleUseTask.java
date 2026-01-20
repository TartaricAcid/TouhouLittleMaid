package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.block.IMaidEdibleBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.edible.MaidEdibleBlockAction;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.edible.MaidEdibleBlockManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.Optional;

public class MaidStealEdibleUseTask extends Behavior<EntityMaid> {
    private final double closeEnoughDist;

    public MaidStealEdibleUseTask(double closeEnoughDist) {
        super(ImmutableMap.of(
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT,
                InitEntities.MAID_EDIBLE_BLOCK_ACTION.get(), MemoryStatus.VALUE_PRESENT
        ));
        this.closeEnoughDist = closeEnoughDist;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            if (owner.distanceToSqr(targetV3d) > Math.pow(closeEnoughDist, 2)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    protected void start(ServerLevel world, EntityMaid maid, long gameTimeIn) {
        Brain<EntityMaid> brain = maid.getBrain();
        brain.getMemory(InitEntities.TARGET_POS.get())
                .ifPresent(posWrapper -> brain.getMemory(InitEntities.MAID_EDIBLE_BLOCK_ACTION.get())
                        .ifPresent(action -> handle(world, maid, posWrapper, action)));
    }

    private void handle(ServerLevel world, EntityMaid maid, PositionTracker posWrapper, MaidEdibleBlockAction action) {
        BlockPos blockPos = posWrapper.currentBlockPosition();
        BlockState blockState = world.getBlockState(blockPos);

        if (action == MaidEdibleBlockAction.TRY_STEAL) {
            for (IMaidEdibleBlock edibleBlock : MaidEdibleBlockManager.getEdibleBlocks()) {
                // 再进行一次方块确认
                if (edibleBlock.shouldMoveTo(maid, blockPos, blockState)) {
                    boolean result = edibleBlock.consume(maid, blockPos, blockState);
                    if (result) {
                        int points = edibleBlock.getFavorabilityPoints(maid, blockPos, blockState);
                        maid.getFavorabilityManager().apply(Type.STEAL_EDIBLE_BLOCK, points);
                        maid.swing(InteractionHand.MAIN_HAND);
                    }
                    maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                    maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                    return;
                }
            }
        } else {
            CombinedInvWrapper inv = maid.getAvailableInv(true);
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.isEmpty()) {
                    continue;
                }
                for (IMaidEdibleBlock edibleBlock : MaidEdibleBlockManager.getEdibleBlocks()) {
                    // 再进行一次方块确认
                    if (edibleBlock.canPlaceAsFood(maid, stack, i)) {
                        boolean result = edibleBlock.placeAsFood(maid, blockPos, stack, i);
                        if (result) {
                            maid.swing(InteractionHand.MAIN_HAND);
                        }
                        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                        return;
                    }
                }
            }
        }
    }
}
