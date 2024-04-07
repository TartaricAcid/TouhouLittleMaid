package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityBookshelf extends TileEntityJoy {
    public TileEntityBookshelf(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }    public static final BlockEntityType<TileEntityBookshelf> TYPE = BlockEntityType.Builder.of(TileEntityBookshelf::new, InitBlocks.BOOKSHELF.get()).build(null);


}