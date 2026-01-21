package com.github.tartaricacid.touhoulittlemaid.inventory.chest;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntitySnackCabinet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SnackCabinetType implements IChestType {
    @Override
    public boolean isChest(BlockEntity chest) {
        return chest instanceof TileEntitySnackCabinet;
    }

    @Override
    public boolean canOpenByPlayer(BlockEntity chest, Player player) {
        if (chest instanceof TileEntitySnackCabinet cabinet) {
            return cabinet.canOpen(player);
        }
        return false;
    }

    @Override
    public int getOpenCount(BlockGetter level, BlockPos pos, BlockEntity chest) {
        return ALLOW_COUNT;
    }
}
