package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.google.common.base.Objects;

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
    private String entityId = "touhou_little_maid:passive.maid";
    private EnumFacing facing = EnumFacing.SOUTH;
    private String model = "touhou_little_maid:models/entity/hakurei_reimu.json";
    private String texture = "touhou_little_maid:textures/entity/hakurei_reimu.png";
    private String name = "{model.vanilla_touhou_model.hakurei_reimu.name}";
    private NBTTagCompound entityData = new NBTTagCompound();

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
        if (getTileData().hasKey(BlockGarageKit.NBT.MODEL_LOCATION.getName())) {
            model = getTileData().getString(BlockGarageKit.NBT.MODEL_LOCATION.getName());
        }
        if (getTileData().hasKey(BlockGarageKit.NBT.MODEL_TEXTURE.getName())) {
            texture = getTileData().getString(BlockGarageKit.NBT.MODEL_TEXTURE.getName());
        }
        if (getTileData().hasKey(BlockGarageKit.NBT.MODEL_NAME.getName())) {
            name = getTileData().getString(BlockGarageKit.NBT.MODEL_NAME.getName());
        }
        if (getTileData().hasKey(BlockGarageKit.NBT.MAID_DATA.getName())) {
            entityData = getTileData().getCompoundTag(BlockGarageKit.NBT.MAID_DATA.getName());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        getTileData().setString(BlockGarageKit.NBT.ENTITY_ID.getName(), entityId);
        getTileData().setString(BlockGarageKit.NBT.FACING.getName(), facing.getName());
        getTileData().setString(BlockGarageKit.NBT.MODEL_LOCATION.getName(), model);
        getTileData().setString(BlockGarageKit.NBT.MODEL_TEXTURE.getName(), texture);
        getTileData().setString(BlockGarageKit.NBT.MODEL_NAME.getName(), name);
        getTileData().setTag(BlockGarageKit.NBT.MAID_DATA.getName(), entityData);
        super.writeToNBT(compound);
        return compound;
    }

    // ------------------------------- 所有的 Get 和 Set 方法 ------------------------------- //

    public String getEntityId() {
        return entityId;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public String getModel() {
        return model;
    }

    public String getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    public void setData(String character, EnumFacing facing, String model, String texture, String name, NBTTagCompound entityData) {
        if (Objects.equal(this.entityId, character) && Objects.equal(this.facing, facing) && Objects.equal(this.model, model) && Objects.equal(this.texture, texture) && Objects.equal(this.name, name)) {
            return;
        }
        this.entityId = character;
        this.facing = facing;
        this.model = model;
        this.texture = texture;
        this.name = name;
        this.entityData = entityData;
        // 确保数据已经存入
        markDirty();
        // 通知 world 更新方块数据
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }
}
