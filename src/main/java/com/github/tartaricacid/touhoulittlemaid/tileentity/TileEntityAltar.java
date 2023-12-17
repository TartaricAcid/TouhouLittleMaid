package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.AltarItemHandler;
import com.github.tartaricacid.touhoulittlemaid.util.PosListData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityAltar extends TileEntity {
    private static final String STORAGE_ITEM = "StorageItem";    public static final TileEntityType<TileEntityAltar> TYPE = TileEntityType.Builder.of(TileEntityAltar::new, InitBlocks.ALTAR.get()).build(null);
    private static final String IS_RENDER = "IsRender";
    private static final String CAN_PLACE_ITEM = "CanPlaceItem";
    private static final String STORAGE_STATE_ID = "StorageBlockStateId";
    private static final String DIRECTION = "Direction";
    private static final String STORAGE_BLOCK_LIST = "StorageBlockList";
    private static final String CAN_PLACE_ITEM_POS_LIST = "CanPlaceItemPosList";
    public final ItemStackHandler handler = new AltarItemHandler();
    private boolean isRender = false;
    private boolean canPlaceItem = false;
    private BlockState storageState = Blocks.AIR.defaultBlockState();
    private PosListData blockPosList = new PosListData();
    private PosListData canPlaceItemPosList = new PosListData();
    private Direction direction = Direction.SOUTH;
    public TileEntityAltar() {
        super(TYPE);
    }

    public void setForgeData(BlockState storageState, boolean isRender, boolean canPlaceItem, Direction direction,
                             PosListData blockPosList, PosListData canPlaceItemPosList) {
        this.isRender = isRender;
        this.canPlaceItem = canPlaceItem;
        this.storageState = storageState;
        this.direction = direction;
        this.blockPosList = blockPosList;
        this.canPlaceItemPosList = canPlaceItemPosList;
        refresh();
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        getTileData().putBoolean(IS_RENDER, isRender);
        getTileData().putBoolean(CAN_PLACE_ITEM, canPlaceItem);
        getTileData().putInt(STORAGE_STATE_ID, Block.getId(storageState));
        getTileData().put(STORAGE_ITEM, handler.serializeNBT());
        getTileData().putString(DIRECTION, direction.getSerializedName());
        getTileData().put(STORAGE_BLOCK_LIST, blockPosList.serialize());
        getTileData().put(CAN_PLACE_ITEM_POS_LIST, canPlaceItemPosList.serialize());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        isRender = getTileData().getBoolean(IS_RENDER);
        canPlaceItem = getTileData().getBoolean(CAN_PLACE_ITEM);
        storageState = Block.stateById(getTileData().getInt(STORAGE_STATE_ID));
        handler.deserializeNBT(getTileData().getCompound(STORAGE_ITEM));
        direction = Direction.byName(getTileData().getString(DIRECTION));
        blockPosList.deserialize(getTileData().getList(STORAGE_BLOCK_LIST, Constants.NBT.TAG_COMPOUND));
        canPlaceItemPosList.deserialize(getTileData().getList(CAN_PLACE_ITEM_POS_LIST, Constants.NBT.TAG_COMPOUND));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition.offset(-9, -5, -9), worldPosition.offset(9, 5, 9));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), -1, this.save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (level != null) {
            this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
        }
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.DEFAULT);
        }
    }

    public boolean isRender() {
        return isRender;
    }

    public boolean isCanPlaceItem() {
        return canPlaceItem;
    }

    public BlockState getStorageState() {
        return storageState;
    }

    public PosListData getBlockPosList() {
        return blockPosList;
    }

    public PosListData getCanPlaceItemPosList() {
        return canPlaceItemPosList;
    }

    public ItemStack getStorageItem() {
        if (canPlaceItem) {
            return handler.getStackInSlot(0);
        }
        return ItemStack.EMPTY;
    }

    public Direction getDirection() {
        return direction;
    }


}
