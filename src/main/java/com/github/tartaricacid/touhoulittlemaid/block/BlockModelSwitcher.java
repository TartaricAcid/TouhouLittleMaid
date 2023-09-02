package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemModelSwitcher;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenSwitcherGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.UUID;

public class BlockModelSwitcher extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockModelSwitcher() {
        super(AbstractBlock.Properties.of(Material.STONE).strength(50.0F, 1200.0F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction direction) {
        Direction value = state.getValue(FACING);
        if (direction != null) {
            return direction == value.getClockWise() || direction == value.getCounterClockWise();
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityModelSwitcher();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pLevel.isClientSide) {
            return;
        }
        Direction direction = pState.getValue(FACING);
        boolean leftSignal = pLevel.getSignal(pPos.offset(direction.getCounterClockWise().getNormal()), direction.getCounterClockWise()) > 0;
        boolean rightSignal = pLevel.getSignal(pPos.offset(direction.getClockWise().getNormal()), direction.getClockWise()) > 0;
        boolean hasSignal = leftSignal || rightSignal;
        TileEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof TileEntityModelSwitcher && pLevel instanceof ServerWorld) {
            TileEntityModelSwitcher switcher = (TileEntityModelSwitcher) blockEntity;
            if (switcher.isPowered() != hasSignal) {
                switcher.setPowered(!switcher.isPowered());
                if (!switcher.isPowered()) {
                    return;
                }
                UUID uuid = switcher.getUuid();
                if (uuid == null) {
                    return;
                }
                int index = calculateIndex(leftSignal, switcher.getInfoList().size(), switcher.getIndex());
                switcher.setIndex(index);
                ServerWorld serverLevel = (ServerWorld) pLevel;
                Entity entity = serverLevel.getEntity(uuid);
                if (entity instanceof EntityMaid && entity.isAlive()) {
                    this.setMaidData(switcher, (EntityMaid) entity);
                }
            }
        }
    }

    private void setMaidData(TileEntityModelSwitcher switcher, EntityMaid maid) {
        TileEntityModelSwitcher.ModeInfo modelInfo = switcher.getModelInfo();
        if (modelInfo != null) {
            maid.setModelId(modelInfo.getModelId().toString());
            if (StringUtils.isNotBlank(modelInfo.getText())) {
                maid.setCustomName(new StringTextComponent(modelInfo.getText()));
                maid.setCustomNameVisible(true);
            } else {
                maid.setCustomName(null);
                maid.setCustomNameVisible(false);
            }
            BlockPos blockPos = maid.blockPosition();
            maid.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
            maid.yRot = modelInfo.getDirection().toYRot();
            maid.setYHeadRot(modelInfo.getDirection().toYRot());
            maid.setYBodyRot(modelInfo.getDirection().toYRot());
        }
    }

    private int calculateIndex(boolean leftSignal, int size, int index) {
        if (leftSignal) {
            if (index < size - 1) {
                index++;
            } else {
                index = 0;
            }
        } else {
            if (index > 0) {
                index--;
            } else {
                index = size - 1;
            }
        }
        return index;
    }


    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityModelSwitcher) {
            if (!worldIn.isClientSide) {
                NetworkHandler.sendToClientPlayer(new OpenSwitcherGuiMessage(pos), player);
            }
            return ActionResultType.sidedSuccess(worldIn.isClientSide);
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        TileEntity te = pLevel.getBlockEntity(pPos);
        if (te instanceof TileEntityModelSwitcher) {
            TileEntityModelSwitcher tileEntityModelSwitcher = (TileEntityModelSwitcher) te;
            ItemModelSwitcher.itemStackToTileEntity(pStack, tileEntityModelSwitcher);
            tileEntityModelSwitcher.refresh();
        }
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityModelSwitcher) {
            popResource(worldIn, pos, ItemModelSwitcher.tileEntityToItemStack((TileEntityModelSwitcher) te));
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}