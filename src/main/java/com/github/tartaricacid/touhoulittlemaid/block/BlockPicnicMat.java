package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.block.properties.PicnicMatPart;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPicnicBasket;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.UUID;

public class BlockPicnicMat extends Block implements EntityBlock {
    public static final EnumProperty<PicnicMatPart> PART = EnumProperty.create("part", PicnicMatPart.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape AABB = Block.box(0, 0, 0, 16, 1, 16);

    public BlockPicnicMat() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, PicnicMatPart.CENTER));
    }

    public void startMaidSit(EntityMaid maid, BlockState state, Level worldIn, BlockPos pos) {
        if (worldIn instanceof ServerLevel serverLevel && worldIn.getBlockEntity(pos) instanceof TileEntityPicnicMat picnicMat) {
            // 只能选中中心方块
            if (!state.getValue(PART).isCenter()) {
                return;
            }
            // 遍历，寻找是否有空位
            boolean hasEmptySit = false;
            int sitIndex = -1;
            for (UUID uuid : picnicMat.getSitIds()) {
                sitIndex++;
                if (uuid.equals(Util.NIL_UUID)) {
                    hasEmptySit = true;
                    break;
                }
                Entity oldSitEntity = serverLevel.getEntity(uuid);
                if (oldSitEntity == null || !oldSitEntity.isAlive()) {
                    hasEmptySit = true;
                    break;
                }
            }
            if (hasEmptySit) {
                Vec3 sitPosition = this.sitPosition(sitIndex);
                EntitySit newSitEntity = new EntitySit(worldIn, Vec3.atLowerCornerWithOffset(pos, sitPosition.x, sitPosition.y + 0.0625, sitPosition.z), Type.ON_HOME_MEAL.getTypeName(), pos);
                double y = sitPosition.z < 0 ? -1 : 1;
                double x = sitPosition.x < 0 ? -1 : 1;
                double rotOffset = Math.toDegrees(Math.atan2(y, x));
                newSitEntity.setYRot((float) rotOffset + 90);
                worldIn.addFreshEntity(newSitEntity);
                picnicMat.setSitId(sitIndex, newSitEntity.getUUID());
                maid.startRiding(newSitEntity);
            }
        }
    }

    private Vec3 sitPosition(int sitIndex) {
        switch (sitIndex) {
            case 0:
                return new Vec3(2, 0, 2);
            case 1:
                return new Vec3(-1, 0, 2);
            case 2:
                return new Vec3(-1, 0, -1);
            case 3:
            default:
                return new Vec3(2, 0, -1);
        }
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        if (!(worldIn.getBlockEntity(pos) instanceof TileEntityPicnicMat picnicMat)) {
            return ItemInteractionResult.FAIL;
        }
        BlockPos centerPos = picnicMat.getCenterPos();
        if (!(worldIn.getBlockEntity(centerPos) instanceof TileEntityPicnicMat picnicMatCenter)) {
            return ItemInteractionResult.FAIL;
        }
        ItemStack itemInHand = playerIn.getItemInHand(hand);
        if (itemInHand.isEdible()) {
            return placeFood(itemInHand, playerIn, picnicMatCenter);
        }
        if (itemInHand.isEmpty() && playerIn.isDiscrete()) {
            return takeFood(playerIn, picnicMatCenter);
        }
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }


    private static ItemInteractionResult placeFood(ItemStack food, Player playerIn, TileEntityPicnicMat picnicMatCenter) {
        int count = food.getCount();
        ItemStack resultStack = ItemHandlerHelper.insertItemStacked(picnicMatCenter.getHandler(), food.copy(), false);
        picnicMatCenter.refresh();
        int shrinkCount = count - resultStack.getCount();
        if (shrinkCount <= 0) {
            return ItemInteractionResult.FAIL;
        }
        food.shrink(shrinkCount);
        return ItemInteractionResult.SUCCESS;
    }

    private static ItemInteractionResult takeFood(Player playerIn, TileEntityPicnicMat picnicMatCenter) {
        ItemStackHandler handler = picnicMatCenter.getHandler();
        int size = handler.getSlots() - 1;
        for (int i = size; i >= 0; i--) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ItemStack outputStack = handler.extractItem(i, handler.getSlotLimit(i), false);
                picnicMatCenter.refresh();
                ItemHandlerHelper.giveItemToPlayer(playerIn, outputStack);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        handlePicnicMatRemove(world, pos, state);
        return super.playerWillDestroy(world, pos, state, player);
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
                // 正中心的不用放置
                if (!searchPos.equals(pos)) {
                    worldIn.setBlock(searchPos, state.setValue(PART, PicnicMatPart.SIDE), Block.UPDATE_ALL);
                }
                BlockEntity blockEntity = worldIn.getBlockEntity(searchPos);
                if (blockEntity instanceof TileEntityPicnicMat picnicMat) {
                    picnicMat.setCenterPos(pos);
                }
            }
        }
        // 给中心方块存入物品
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityPicnicMat picnicMat && stack.is(InitItems.PICNIC_BASKET.get())) {
            picnicMat.setHandler(ItemPicnicBasket.getContainer(stack));
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        BlockPos below = blockPos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
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
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return true;
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
        if (world.getBlockEntity(centerPos) instanceof TileEntityPicnicMat picnicMatCenter) {
            ItemStack stack = InitItems.PICNIC_BASKET.get().getDefaultInstance();
            ItemPicnicBasket.setContainer(stack, picnicMatCenter.getHandler());
            popResource(world, centerPos, stack);
            if (world instanceof ServerLevel serverLevel) {
                for (UUID uuid : picnicMatCenter.getSitIds()) {
                    if (uuid.equals(Util.NIL_UUID)) {
                        continue;
                    }
                    Entity entity = serverLevel.getEntity(uuid);
                    if (entity instanceof EntitySit) {
                        entity.discard();
                    }
                }
            }
        }
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
