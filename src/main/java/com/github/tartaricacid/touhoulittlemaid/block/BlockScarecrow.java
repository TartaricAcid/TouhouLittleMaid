package com.github.tartaricacid.touhoulittlemaid.block;

import com.mojang.serialization.MapCodec;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.util.VoxelShapeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import java.util.List;

public class BlockScarecrow extends HorizontalDirectionalBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape LOWER_AABB = Shapes.or(
            Block.box(1, 0, 1, 15, 4, 15),
            Block.box(2, 4, 2, 14, 8, 14),
            Block.box(4.5, 8, 4.5, 11.5, 16, 11.5)
    );
    protected static final VoxelShape UPPER_AABB_NORTH = Block.box(4, 0, 7.5, 12, 6.5, 13.5);
    protected static final VoxelShape UPPER_AABB_SOUTH = VoxelShapeUtils.rotateHorizontal(UPPER_AABB_NORTH, Direction.SOUTH);
    protected static final VoxelShape UPPER_AABB_EAST = VoxelShapeUtils.rotateHorizontal(UPPER_AABB_NORTH, Direction.EAST);
    protected static final VoxelShape UPPER_AABB_WEST = VoxelShapeUtils.rotateHorizontal(UPPER_AABB_NORTH, Direction.WEST);


    public BlockScarecrow() {
        super(BlockBehaviour.Properties.of().sound(SoundType.GRASS).sound(SoundType.GRASS).strength(0.2F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(properties -> new BlockScarecrow());
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
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
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
        return super.playerWillDestroy(level, pos, state, player);
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
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case SOUTH -> {
                return UPPER_AABB_SOUTH;
            }
            case EAST -> {
                return UPPER_AABB_EAST;
            }
            case WEST -> {
                return UPPER_AABB_WEST;
            }
            default -> {
                return UPPER_AABB_NORTH;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        int range = MiscConfig.SCARECROW_RANGE.get();
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.scarecrow.desc", range, range).withStyle(ChatFormatting.GRAY));
    }
}
