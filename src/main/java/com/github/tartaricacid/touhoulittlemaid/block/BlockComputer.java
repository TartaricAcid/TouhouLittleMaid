package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityComputer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockComputer extends BlockJoy {
    public static final VoxelShape SHAPE = VoxelShapes.or(Block.box(0, 0, 0, 16, 3, 16),
            Block.box(6, 3, 6, 10, 9, 10),
            Block.box(1, 9, 1, 15, 12, 15),
            Block.box(0, 12, 0, 16, 14, 16));

    @Override
    protected Vector3d sitPosition() {
        return new Vector3d(0.5, 1, 0.5);
    }

    @Override
    protected int sitYRot() {
        return 180;
    }

    @Override
    protected String getTypeName() {
        return Type.COMPUTER.getTypeName();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityComputer();
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
