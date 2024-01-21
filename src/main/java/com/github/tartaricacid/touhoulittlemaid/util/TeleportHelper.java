package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class TeleportHelper {
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
        BlockPos.Mutable blockPos = new BlockPos.Mutable(x, y, z);
        while (blockPos.getY() > 0 && !maid.level.getBlockState(blockPos).getMaterial().blocksMotion()) {
            blockPos.move(Direction.DOWN);
        }
        BlockState blockState = maid.level.getBlockState(blockPos);
        boolean isMotion = blockState.getMaterial().blocksMotion();
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

    public static boolean teleportToRestrictCenter(EntityMaid maid) {
        BlockPos blockPos = maid.getRestrictCenter();
        if (!maid.level.isClientSide() && maid.isAlive()) {
            int x = blockPos.getX() + randomIntInclusive(maid.getRandom(), -3, 3);
            int y = blockPos.getY() + randomIntInclusive(maid.getRandom(), -1, 1);
            int z = blockPos.getZ() + randomIntInclusive(maid.getRandom(), -3, 3);
            return teleport(maid, x, y, z);
        } else {
            return false;
        }
    }

    private static int randomIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
