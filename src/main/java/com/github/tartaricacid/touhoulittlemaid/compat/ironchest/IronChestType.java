package com.github.tartaricacid.touhoulittlemaid.compat.ironchest;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;

public final class IronChestType implements IChestType {
    private static final String IRON_CHEST_ID = "ironchest";

    public static void register(ChestManager manager) {
        if (ModList.get().isLoaded(IRON_CHEST_ID)) {
            manager.add(new IronChestType());
        }
    }

    @Override
    public boolean isChest(BlockEntity chest) {
        if (ModList.get().isLoaded(IRON_CHEST_ID)) {
            return chest instanceof AbstractIronChestBlockEntity;
        }
        return false;
    }

    @Override
    public boolean canOpenByPlayer(BlockEntity chest, Player player) {
        if (ModList.get().isLoaded(IRON_CHEST_ID) && chest instanceof AbstractIronChestBlockEntity ironChestBlock) {
            return ironChestBlock.canOpen(player);
        }
        return false;
    }

    @Override
    public int getOpenCount(BlockGetter level, BlockPos pos, BlockEntity chest) {
        if (ModList.get().isLoaded(IRON_CHEST_ID) && chest instanceof AbstractIronChestBlockEntity) {
            return AbstractIronChestBlockEntity.getOpenCount(level, pos);
        }
        return DENY_COUNT;
    }
}
