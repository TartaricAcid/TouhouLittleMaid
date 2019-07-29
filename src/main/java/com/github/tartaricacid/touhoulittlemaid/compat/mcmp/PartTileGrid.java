package com.github.tartaricacid.touhoulittlemaid.compat.mcmp;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;

import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.tileentity.TileEntity;

public class PartTileGrid implements IMultipartTile {

    private final TileEntityGrid grid;

    public PartTileGrid(TileEntityGrid grid) {
        this.grid = grid;
    }

    @Override
    public TileEntity getTileEntity() {
        return grid;
    }
}
