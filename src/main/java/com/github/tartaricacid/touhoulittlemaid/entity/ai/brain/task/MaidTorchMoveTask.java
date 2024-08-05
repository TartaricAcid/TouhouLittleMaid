package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import static net.minecraft.world.level.block.Block.canSupportCenter;

public class MaidTorchMoveTask extends MaidMoveToBlockTask {
    private static final int LOW_BRIGHTNESS = 9;

    public MaidTorchMoveTask(float movementSpeed) {
        super(movementSpeed, 2);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid entityIn, BlockPos pos) {
        BlockPos posUp = pos.above();
        if (worldIn.getMaxLocalRawBrightness(posUp) < LOW_BRIGHTNESS && entityIn.canPlaceBlock(posUp)) {
            BlockState stateUp = worldIn.getBlockState(posUp);
            //TODO : 火把放置逻辑略有变化，需要检测，现在这一段是从BaseTorchBlock.canSurvive抄来
            return canSupportCenter( worldIn, pos.below(), Direction.UP) && !stateUp.liquid();
        }
        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        if (!getTorchItem(maid).isEmpty()) {
            searchForDestination(worldIn, maid);
        }
    }

    private ItemStack getTorchItem(EntityMaid entityMaid) {
        CombinedInvWrapper itemHandler = entityMaid.getAvailableInv(false);
        return ItemsUtil.getStack(itemHandler, stack -> stack.getItem() == Items.TORCH);
    }
}
