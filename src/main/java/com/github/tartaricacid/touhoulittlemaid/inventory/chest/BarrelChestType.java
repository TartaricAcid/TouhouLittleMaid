package com.github.tartaricacid.touhoulittlemaid.inventory.chest;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BarrelChestType implements IChestType {
    @Override
    public boolean isChest(BlockEntity chest) {
        return chest instanceof BarrelBlockEntity;
    }

    @Override
    public boolean canOpenByPlayer(BlockEntity chest, Player player) {
        if (chest instanceof BarrelBlockEntity barrelBlockEntity) {
            return barrelBlockEntity.canOpen(player);
        }
        return false;
    }

    @Override
    public int getOpenCount(BlockGetter level, BlockPos pos, BlockEntity chest) {
        return ALLOW_COUNT;
    }
}
