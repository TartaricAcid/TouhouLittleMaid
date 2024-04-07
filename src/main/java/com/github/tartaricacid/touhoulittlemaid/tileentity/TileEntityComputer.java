package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityComputer extends TileEntityJoy {
    public TileEntityComputer(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }    public static final BlockEntityType<TileEntityComputer> TYPE = BlockEntityType.Builder.of(TileEntityComputer::new, InitBlocks.COMPUTER.get()).build(null);


}