package com.github.tartaricacid.touhoulittlemaid.compat.ironchest;

import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.fml.ModList;

public final class IronChestCheck {
    private static final String IRON_CHEST_ID = "ironchest";

    public static boolean isIronChest(BlockEntity blockEntity) {
        if (!ModList.get().isLoaded(IRON_CHEST_ID)) {
            return false;
        }
        return blockEntity instanceof AbstractIronChestBlockEntity;
    }

    public static int getOpenCount(BlockGetter level, BlockPos pos, BlockEntity te) {
        if (te instanceof ChestBlockEntity) {
            return ChestBlockEntity.getOpenCount(level, pos);
        }
        if (ModList.get().isLoaded(IRON_CHEST_ID) && te instanceof AbstractIronChestBlockEntity) {
            return AbstractIronChestBlockEntity.getOpenCount(level, pos);
        }
        return Integer.MAX_VALUE;
    }
}
