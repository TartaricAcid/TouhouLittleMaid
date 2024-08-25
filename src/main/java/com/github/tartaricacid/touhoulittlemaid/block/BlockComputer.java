package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityComputer;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockComputer extends BlockJoy {
    public static final VoxelShape SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 3, 16),
            Block.box(6, 3, 6, 10, 9, 10),
            Block.box(1, 9, 1, 15, 12, 15),
            Block.box(0, 12, 0, 16, 14, 16));

    @Override
    protected Vec3 sitPosition() {
        return new Vec3(0.5, 1, 0.5);
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
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TileEntityComputer(pPos, pState);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec((properties) -> new BlockComputer());
    }
}
