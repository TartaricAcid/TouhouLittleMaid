package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.Optional;

public class MaidTorchPlaceTask extends Task<EntityMaid> {
    private final double closeEnoughDist;

    public MaidTorchPlaceTask(double closeEnoughDist) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryModuleStatus.VALUE_PRESENT));
        this.closeEnoughDist = closeEnoughDist;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vector3d targetV3d = targetPos.currentPosition();
            if (owner.distanceToSqr(targetV3d) > Math.pow(closeEnoughDist, 2)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (!walkTarget.isPresent() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    protected void start(ServerWorld world, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            ItemStack torch = getTorchItem(maid);
            if (!torch.isEmpty()) {
                BlockPos pos = posWrapper.currentBlockPosition().above();
                BlockState torchState = Blocks.TORCH.defaultBlockState();
                world.setBlock(pos, torchState, Constants.BlockFlags.DEFAULT_AND_RERENDER);
                SoundType soundType = torchState.getSoundType(world, pos, maid);
                world.playSound(null, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
                        (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                torch.shrink(1);
                maid.swing(Hand.MAIN_HAND);
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            }
        });
    }

    private ItemStack getTorchItem(EntityMaid entityMaid) {
        CombinedInvWrapper itemHandler = entityMaid.getAvailableInv(false);
        return ItemsUtil.getStack(itemHandler, stack -> stack.getItem() == Items.TORCH);
    }
}
