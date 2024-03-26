package com.github.tartaricacid.touhoulittlemaid.inventory.chest;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class VanillaChestType implements IChestType {
    @Override
    public boolean isChest(BlockEntity chest) {
        return chest instanceof ChestBlockEntity;
    }

    @Override
    public boolean canOpenByPlayer(BlockEntity chest, Player player) {
        if (chest instanceof ChestBlockEntity chestBlockEntity) {
            return chestBlockEntity.canOpen(player);
        }
        return false;
    }

    @Override
    public int getOpenCount(BlockGetter level, BlockPos pos, BlockEntity chest) {
        if (chest instanceof ChestBlockEntity) {
            return ChestBlockEntity.getOpenCount(level, pos);
        }
        return DENY_COUNT;
    }
}
