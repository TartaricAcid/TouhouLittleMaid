package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityKeyboard;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockKeyboard extends BlockJoy {
    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 10, 12);

    @Override
    protected Vector3d sitPosition() {
        return new Vector3d(0.5, 0.625, 0.5);
    }

    @Override
    protected int sitYRot() {
        return 0;
    }

    @Override
    protected String getTypeName() {
        return Type.KEYBOARD.getTypeName();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityKeyboard();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
