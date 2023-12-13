package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityBookshelf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockBookshelf extends BlockJoy {
    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 5, 15);

    @Override
    protected Vector3d sitPosition() {
        return new Vector3d(0.5, 0.375, 0.5);
    }

    @Override
    protected int sitYRot() {
        return -90;
    }

    @Override
    protected String getTypeName() {
        return Type.BOOKSHELF.getTypeName();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBookshelf();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
