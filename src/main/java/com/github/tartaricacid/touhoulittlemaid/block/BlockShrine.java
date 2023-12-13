package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.item.ItemFilm;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityShrine;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockShrine extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = VoxelShapes.or(Block.box(0, 0, 0, 16, 5, 16),
            Block.box(2, 5, 2, 14, 10, 14),
            Block.box(4, 10, 4, 12, 16, 12));

    public BlockShrine() {
        super(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F, 3.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (hand == Hand.MAIN_HAND && worldIn.getBlockEntity(pos) instanceof TileEntityShrine) {
            TileEntityShrine shrine = (TileEntityShrine) worldIn.getBlockEntity(pos);
            if (playerIn.isShiftKeyDown()) {
                if (!shrine.isEmpty()) {
                    ItemStack storageItem = shrine.extractStorageItem();
                    ItemHandlerHelper.giveItemToPlayer(playerIn, storageItem);
                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
            if (shrine.isEmpty()) {
                if (shrine.canInsert(playerIn.getMainHandItem())) {
                    shrine.insertStorageItem(ItemHandlerHelper.copyStackWithSize(playerIn.getMainHandItem(), 1));
                    playerIn.getMainHandItem().shrink(1);
                    return ActionResultType.SUCCESS;
                }
                if (!worldIn.isClientSide) {
                    playerIn.sendMessage(new TranslationTextComponent("message.touhou_little_maid.shrine.not_film"), Util.NIL_UUID);
                }
                return ActionResultType.PASS;
            }
            if (playerIn.getMainHandItem().isEmpty()) {
                if (playerIn.getHealth() < (playerIn.getMaxHealth() / 2) + 1) {
                    if (!worldIn.isClientSide) {
                        playerIn.sendMessage(new TranslationTextComponent("message.touhou_little_maid.shrine.health_low"), Util.NIL_UUID);
                    }
                    return ActionResultType.FAIL;
                }
                playerIn.setHealth(0.25f);
                ItemStack film = shrine.getStorageItem();
                ItemFilm.filmToMaid(film, worldIn, pos.above(), playerIn);
            }
        }
        return super.use(state, worldIn, pos, playerIn, hand, hit);
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityShrine) {
            TileEntityShrine shrine = (TileEntityShrine) blockEntity;
            ItemStack storageItem = shrine.extractStorageItem();
            if (!storageItem.isEmpty()) {
                Block.popResource(worldIn, pos.offset(0, 1, 0), storageItem);
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityShrine();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
