package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class BlockPicnicMat extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape AABB = Block.box(0, 0, 0, 16, 1, 16);

    public BlockPicnicMat() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.PASS;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (!(worldIn.getBlockEntity(pos) instanceof TileEntityPicnicMat picnicMat)) {
            return InteractionResult.FAIL;
        }
        BlockPos centerPos = picnicMat.getCenterPos();
        if (!(worldIn.getBlockEntity(centerPos) instanceof TileEntityPicnicMat picnicMatCenter)) {
            return InteractionResult.FAIL;
        }
        ItemStack itemInHand = playerIn.getItemInHand(hand);
        if (itemInHand.isEdible()) {
            return placeFood(itemInHand, playerIn, picnicMatCenter);
        }
        if (itemInHand.isEmpty() && playerIn.isDiscrete()) {
            return takeFood(playerIn, picnicMatCenter);
        }
        return InteractionResult.PASS;
    }


    private static InteractionResult placeFood(ItemStack food, Player playerIn, TileEntityPicnicMat picnicMatCenter) {
        int count = food.getCount();
        ItemStack resultStack = ItemHandlerHelper.insertItemStacked(picnicMatCenter.getHandler(), food.copy(), false);
        picnicMatCenter.refresh();
        int shrinkCount = count - resultStack.getCount();
        if (shrinkCount <= 0) {
            return InteractionResult.FAIL;
        }
        food.shrink(shrinkCount);
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult takeFood(Player playerIn, TileEntityPicnicMat picnicMatCenter) {
        ItemStackHandler handler = picnicMatCenter.getHandler();
        int size = handler.getSlots() - 1;
        for (int i = size; i >= 0; i--) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ItemStack outputStack = handler.extractItem(i, handler.getSlotLimit(i), false);
                picnicMatCenter.refresh();
                ItemHandlerHelper.giveItemToPlayer(playerIn, outputStack);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        handlePicnicMatRemove(world, pos, state);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        handlePicnicMatRemove(world, pos, state);
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos centerPos = context.getClickedPos();
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                BlockPos searchPos = centerPos.offset(i, 0, j);
                if (!context.getLevel().getBlockState(searchPos).canBeReplaced(context)) {
                    return null;
                }
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide) {
            return;
        }
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                BlockPos searchPos = pos.offset(i, 0, j);
                worldIn.setBlock(searchPos, state, Block.UPDATE_ALL);
                BlockEntity blockEntity = worldIn.getBlockEntity(searchPos);
                if (blockEntity instanceof TileEntityPicnicMat picnicMat) {
                    picnicMat.setCenterPos(pos);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        BlockPos below = blockPos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityPicnicMat(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AABB;
    }

    private static void handlePicnicMatRemove(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide) {
            return;
        }
        if (!(world.getBlockEntity(pos) instanceof TileEntityPicnicMat picnicMat)) {
            return;
        }
        BlockPos centerPos = picnicMat.getCenterPos();
        popResource(world, centerPos, InitItems.PICNIC_BASKET.get().getDefaultInstance());
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                BlockPos offset = centerPos.offset(i, 0, j);
                if (world.getBlockState(offset).is(InitBlocks.PICNIC_MAT.get())) {
                    world.setBlockAndUpdate(offset, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }
}
