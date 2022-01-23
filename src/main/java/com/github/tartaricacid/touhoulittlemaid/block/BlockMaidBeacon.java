package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenBeaconGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Locale;

public class BlockMaidBeacon extends Block {
    public static final EnumProperty<Position> POSITION = EnumProperty.create("position", Position.class);
    private static final VoxelShape UP_AABB = Block.box(3, 1, 3, 13, 16, 13);
    private static final VoxelShape DOWN_AABB = Block.box(6.5, 0, 6.5, 9.5, 26, 9.5);

    public BlockMaidBeacon() {
        super(AbstractBlock.Properties.of(Material.WOOD).strength(2, 2).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(POSITION, Position.DOWN));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(BlockMaidBeacon.POSITION) != Position.DOWN;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityMaidBeacon();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityMaidBeacon) {
            if (!worldIn.isClientSide) {
                NetworkHandler.sendToClientPlayer(new OpenBeaconGuiMessage(pos), player);
            }
            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Position position = state.getValue(POSITION);
        return position == Position.DOWN ? DOWN_AABB : UP_AABB;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing.getAxis() == Direction.Axis.Y) {
            Position position = stateIn.getValue(POSITION);
            if (position == Position.DOWN && facing == Direction.UP) {
                if (!facingState.is(this) || facingState.getValue(POSITION) == Position.DOWN) {
                    return Blocks.AIR.defaultBlockState();
                }
            }
            if (position != Position.DOWN && facing == Direction.DOWN) {
                if (!facingState.is(this) || facingState.getValue(POSITION) == Position.UP_W_E || facingState.getValue(POSITION) == Position.UP_N_S) {
                    return Blocks.AIR.defaultBlockState();
                }
            }
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isClientSide && player.isCreative()) {
            Position position = state.getValue(POSITION);
            if (position != Position.DOWN) {
                BlockPos belowPos = pos.below();
                BlockState belowState = worldIn.getBlockState(belowPos);
                if (belowState.is(this) && belowState.getValue(POSITION) == Position.DOWN) {
                    worldIn.setBlock(belowPos, Blocks.AIR.defaultBlockState(), 35);
                    worldIn.levelEvent(player, Constants.WorldEvents.BREAK_BLOCK_EFFECTS, belowPos, Block.getId(belowState));
                }
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(InitItems.MAID_BEACON.get());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getClickedPos();
        World world = context.getLevel();
        int maxHeight = world.getMaxBuildHeight() - 1;
        if (blockpos.getY() < maxHeight && world.getBlockState(blockpos.above()).canBeReplaced(context)) {
            return super.getStateForPlacement(context);
        }
        return null;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = getHorizontalDirection(placer);
        BlockState stateUp;
        if (facing == Direction.SOUTH || facing == Direction.NORTH) {
            stateUp = this.defaultBlockState().setValue(BlockMaidBeacon.POSITION, BlockMaidBeacon.Position.UP_N_S);
        } else {
            stateUp = this.defaultBlockState().setValue(BlockMaidBeacon.POSITION, BlockMaidBeacon.Position.UP_W_E);
        }
        worldIn.setBlock(pos.above(), stateUp, Constants.BlockFlags.DEFAULT);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        switch (direction) {
            default:
                return state;
            case CLOCKWISE_90:
            case COUNTERCLOCKWISE_90:
                if (state.getValue(POSITION) == Position.UP_N_S) {
                    return state.setValue(POSITION, Position.UP_W_E);
                }
                if (state.getValue(POSITION) == Position.UP_W_E) {
                    return state.setValue(POSITION, Position.UP_N_S);
                }
                return state;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POSITION);
    }

    private Direction getHorizontalDirection(@Nullable LivingEntity placer) {
        return placer == null ? Direction.NORTH : placer.getDirection();
    }

    public enum Position implements IStringSerializable {
        // Beacon State
        UP_N_S, UP_W_E, DOWN;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.US);
        }

        @Override
        public String toString() {
            return getSerializedName();
        }
    }
}
