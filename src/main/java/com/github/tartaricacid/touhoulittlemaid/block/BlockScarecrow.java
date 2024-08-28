package com.github.tartaricacid.touhoulittlemaid.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockScarecrow extends HorizontalDirectionalBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape LOWER_AABB = Shapes.or(
            Block.box(3, 0, 3, 13, 2, 13),
            Block.box(4.5, 2, 4.5, 11.5, 16, 11.5)
    );
    protected static final VoxelShape UPPER_AABB = Shapes.or(
            Block.box(4.5, 0, 4.5, 11.5, 2, 11.5),
            Block.box(4, 2, 4, 12, 10, 12),
            Block.box(3.5, 7.5, 3.5, 12.5, 10.5, 12.5)
    );

    public BlockScarecrow() {
        super(BlockBehaviour.Properties.of().sound(SoundType.GRASS).sound(SoundType.GRASS).strength(0.2F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        DoubleBlockHalf currentHalf = state.getValue(HALF);
        boolean isLower = currentHalf == DoubleBlockHalf.LOWER && direction == Direction.UP;
        boolean isUpper = currentHalf == DoubleBlockHalf.UPPER && direction == Direction.DOWN;
        if (isLower || isUpper) {
            if (neighborState.is(this) && neighborState.getValue(HALF) != currentHalf) {
                return state.setValue(FACING, neighborState.getValue(FACING));
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // 阻止创造模式破坏掉落双份物品
        if (!level.isClientSide && player.isCreative()) {
            DoubleBlockHalf half = state.getValue(HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);
                if (belowState.is(state.getBlock()) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    BlockState empty = belowState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    level.setBlock(belowPos, empty, Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, belowPos, Block.getId(belowState));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        BlockPos abovePos = clickedPos.above();
        if (clickedPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(abovePos).canBeReplaced(context)) {
            Direction horizontalDirection = context.getHorizontalDirection();
            return this.defaultBlockState().setValue(FACING, horizontalDirection).setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        DoubleBlockHalf blockHalf = state.getValue(HALF);
        if (blockHalf == DoubleBlockHalf.LOWER) {
            return LOWER_AABB;
        }
        return UPPER_AABB;
    }
}
