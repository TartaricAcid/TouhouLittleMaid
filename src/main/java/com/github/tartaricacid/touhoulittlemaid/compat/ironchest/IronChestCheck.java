package com.github.tartaricacid.touhoulittlemaid.compat.ironchest;

import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.ModList;

public final class IronChestCheck {
    private static final String IRON_CHEST_ID = "ironchest";

    public static boolean isIronChest(TileEntity blockEntity) {
        if (!ModList.get().isLoaded(IRON_CHEST_ID)) {
            return false;
        }
        return blockEntity instanceof GenericIronChestTileEntity;
    }

    public static int getOpenCount(IBlockReader level, BlockPos pos, TileEntity te) {
        if (te instanceof ChestTileEntity) {
            return ChestTileEntity.getOpenCount(level, pos);
        }
        if (ModList.get().isLoaded(IRON_CHEST_ID) && te instanceof GenericIronChestTileEntity) {
            return GenericIronChestTileEntity.getPlayersUsing(level, pos);
        }
        return Integer.MAX_VALUE;
    }
}
