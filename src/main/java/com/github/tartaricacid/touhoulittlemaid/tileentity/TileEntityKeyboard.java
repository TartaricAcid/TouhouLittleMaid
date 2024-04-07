package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityKeyboard extends TileEntityJoy {
    public TileEntityKeyboard(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }    public static final BlockEntityType<TileEntityKeyboard> TYPE = BlockEntityType.Builder.of(TileEntityKeyboard::new, InitBlocks.KEYBOARD.get()).build(null);


}