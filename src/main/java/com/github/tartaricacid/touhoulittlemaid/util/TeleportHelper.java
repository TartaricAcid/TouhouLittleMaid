package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;

public final class TeleportHelper {
    public static boolean teleport(EntityMaid maid) {
        if (!maid.level.isClientSide() && maid.isAlive()) {
            double x = maid.getX() + (maid.getRandom().nextDouble() - 0.5) * 16;
            double y = maid.getY() + maid.getRandom().nextInt(16) - 8;
            double z = maid.getZ() + (maid.getRandom().nextDouble() - 0.5) * 16;
            return teleport(maid, x, y, z);
        } else {
            return false;
        }
    }

    public static boolean teleport(EntityMaid maid, double x, double y, double z) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(x, y, z);
        while (blockPos.getY() > maid.level.getMinBuildHeight() && !maid.level.getBlockState(blockPos).blocksMotion()) {
            blockPos.move(Direction.DOWN);
        }
        BlockState blockState = maid.level.getBlockState(blockPos);
        boolean isMotion = blockState.blocksMotion();
        boolean isWater = blockState.getFluidState().is(FluidTags.WATER);
        if (isMotion && !isWater) {
            boolean teleportIsSuccess = maid.randomTeleport(x, y, z, true);
            if (teleportIsSuccess && !maid.isSilent()) {
                maid.level.playSound(null, maid.xo, maid.yo, maid.zo, SoundEvents.ENDERMAN_TELEPORT, maid.getSoundSource(), 1.0F, 1.0F);
                maid.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            return teleportIsSuccess;
        } else {
            return false;
        }
    }
}
