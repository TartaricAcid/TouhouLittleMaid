package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemModelSwitcher;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenSwitcherGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.UUID;

public class BlockModelSwitcher extends BaseEntityBlock {
    public BlockModelSwitcher() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(50.0F, 1200.0F));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction != null) {
            return direction == Direction.NORTH || direction == Direction.SOUTH;
        }
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TileEntityModelSwitcher(pPos, pState);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pLevel.isClientSide) {
            return;
        }
        boolean northSignal = pLevel.getSignal(pPos.north(), Direction.NORTH) > 0;
        boolean southSignal = pLevel.getSignal(pPos.south(), Direction.SOUTH) > 0;
        boolean hasSignal = northSignal || southSignal;
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof TileEntityModelSwitcher && pLevel instanceof ServerLevel) {
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
                int index = calculateIndex(northSignal, switcher.getInfoList().size(), switcher.getIndex());
                switcher.setIndex(index);
                ServerLevel serverLevel = (ServerLevel) pLevel;
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
                maid.setCustomName(new TextComponent(modelInfo.getText()));
                maid.setCustomNameVisible(true);
            } else {
                maid.setCustomName(null);
                maid.setCustomNameVisible(false);
            }
            BlockPos blockPos = maid.blockPosition();
            maid.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
            maid.setYRot(modelInfo.getDirection().toYRot());
            maid.setYHeadRot(modelInfo.getDirection().toYRot());
            maid.setYBodyRot(modelInfo.getDirection().toYRot());
        }
    }

    private int calculateIndex(boolean northSignal, int size, int index) {
        if (northSignal) {
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
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityModelSwitcher) {
            if (!worldIn.isClientSide) {
                NetworkHandler.sendToClientPlayer(new OpenSwitcherGuiMessage(pos), player);
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        BlockEntity te = pLevel.getBlockEntity(pPos);
        if (te instanceof TileEntityModelSwitcher) {
            TileEntityModelSwitcher tileEntityModelSwitcher = (TileEntityModelSwitcher) te;
            ItemModelSwitcher.itemStackToTileEntity(pStack, tileEntityModelSwitcher);
            tileEntityModelSwitcher.refresh();
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof TileEntityModelSwitcher) {
            popResource(worldIn, pos, ItemModelSwitcher.tileEntityToItemStack((TileEntityModelSwitcher) te));
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
