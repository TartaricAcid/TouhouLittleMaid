package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.google.common.base.Objects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/7/7 10:56
 **/
public class TileEntityGarageKit extends TileEntity {
    private String entityId = "touhou_little_maid:passive.maid";
    private EnumFacing facing = EnumFacing.SOUTH;
    private String modelId = "touhou_little_maid:hakurei_reimu";
    private NBTTagCompound maidData = new NBTTagCompound();

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
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (getTileData().hasKey(BlockGarageKit.NBT.ENTITY_ID.getName())) {
            entityId = getTileData().getString(BlockGarageKit.NBT.ENTITY_ID.getName());
        }
        if (getTileData().hasKey(BlockGarageKit.NBT.FACING.getName())) {
            facing = EnumFacing.byName(getTileData().getString(BlockGarageKit.NBT.FACING.getName()));
        }
        if (getTileData().hasKey(BlockGarageKit.NBT.MODEL_ID.getName())) {
            modelId = getTileData().getString(BlockGarageKit.NBT.MODEL_ID.getName());
        } else {
            modelId = null;
        }
        if (getTileData().hasKey(BlockGarageKit.NBT.MAID_DATA.getName())) {
            maidData = getTileData().getCompoundTag(BlockGarageKit.NBT.MAID_DATA.getName());
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        getTileData().setString(BlockGarageKit.NBT.ENTITY_ID.getName(), entityId);
        getTileData().setString(BlockGarageKit.NBT.FACING.getName(), facing.getName());
        if (modelId != null) {
            getTileData().setString(BlockGarageKit.NBT.MODEL_ID.getName(), modelId);
        }
        getTileData().setTag(BlockGarageKit.NBT.MAID_DATA.getName(), maidData);
        return super.writeToNBT(compound);
    }

    // ------------------------------- 所有的 Get 和 Set 方法 ------------------------------- //

    public String getEntityId() {
        return entityId;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    @Nullable
    public String getModelId() {
        return modelId;
    }

    public NBTTagCompound getMaidData() {
        return maidData;
    }

    public void setData(String entityId, EnumFacing facing, String modelId, NBTTagCompound maidData) {
        if (Objects.equal(this.entityId, entityId) && Objects.equal(this.facing, facing) && Objects.equal(this.modelId, modelId) && Objects.equal(this.maidData, maidData)) {
            return;
        }
        this.entityId = entityId;
        this.facing = facing;
        this.modelId = modelId;
        this.maidData = maidData;
        // 确保数据已经存入
        markDirty();
        // 通知 world 更新方块数据
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }
}
