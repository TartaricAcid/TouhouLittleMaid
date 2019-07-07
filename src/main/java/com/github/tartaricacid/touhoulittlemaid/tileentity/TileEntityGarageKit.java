package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/7/7 10:56
 **/
public class TileEntityGarageKit extends TileEntity {
    private String character = "touhou_little_maid:passive.maid";
    private EnumFacing facing = EnumFacing.SOUTH;

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
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (getTileData().hasKey(BlockGarageKit.NBT.CHARACTER.getName())) {
            character = getTileData().getString(BlockGarageKit.NBT.CHARACTER.getName());
        }
        if (getTileData().hasKey(BlockGarageKit.NBT.FACING.getName())) {
            facing = EnumFacing.byName(getTileData().getString(BlockGarageKit.NBT.FACING.getName()));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        getTileData().setString(BlockGarageKit.NBT.CHARACTER.getName(), character);
        getTileData().setString(BlockGarageKit.NBT.FACING.getName(), facing.getName());
        super.writeToNBT(compound);
        return compound;
    }

    // ------------------------------- 所有的 Get 和 Set 方法 ------------------------------- //

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
        markDirty(); // 确保数据已经存入
        // 通知 world 更新方块数据
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
        markDirty(); // 确保数据已经存入
        // 通知 world 更新方块数据
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }
}
