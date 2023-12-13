package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityBookshelf extends TileEntityJoy {
    public static final TileEntityType<TileEntityBookshelf> TYPE = TileEntityType.Builder.of(TileEntityBookshelf::new, InitBlocks.BOOKSHELF.get()).build(null);

    public TileEntityBookshelf() {
        super(TYPE);
    }
}