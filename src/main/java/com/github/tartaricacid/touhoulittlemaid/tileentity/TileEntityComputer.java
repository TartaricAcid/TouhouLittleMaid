package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityComputer extends TileEntityJoy {
    public static final TileEntityType<TileEntityComputer> TYPE = TileEntityType.Builder.of(TileEntityComputer::new, InitBlocks.COMPUTER.get()).build(null);

    public TileEntityComputer() {
        super(TYPE);
    }
}