package com.github.tartaricacid.touhoulittlemaid.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/10/2 21:50
 **/
public class TileEntityTombstone extends TileEntity {
    private static final String STORAGE_TAG_NAME = "StorageItem";
    private static final String OWNER_TAG_NAME = "OwnerName";
    public final ItemStackHandler handler = new ItemStackHandler(60);
    private String ownerName = "";

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -1, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (getTileData().hasKey(STORAGE_TAG_NAME)) {
            handler.deserializeNBT(getTileData().getCompoundTag(STORAGE_TAG_NAME));
        }
        if (getTileData().hasKey(OWNER_TAG_NAME)) {
            ownerName = getTileData().getString(OWNER_TAG_NAME);
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        getTileData().setTag(STORAGE_TAG_NAME, handler.serializeNBT());
        getTileData().setString(OWNER_TAG_NAME, ownerName);
        return super.writeToNBT(compound);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String name) {
        ownerName = name;
        refresh();
    }

    public void refresh() {
        markDirty();
        if (world != null) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }
}
