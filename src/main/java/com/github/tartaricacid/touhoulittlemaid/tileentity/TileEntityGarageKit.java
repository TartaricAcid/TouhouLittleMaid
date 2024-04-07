package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TileEntityGarageKit extends BlockEntity {
    private static final String FACING_TAG = "GarageKitFacing";
    private static final String EXTRA_DATA = "ExtraData";    public static final BlockEntityType<TileEntityGarageKit> TYPE = BlockEntityType.Builder.of(TileEntityGarageKit::new, InitBlocks.GARAGE_KIT.get()).build(null);
    private Direction facing = Direction.NORTH;
    private CompoundTag extraData = new CompoundTag();
    public TileEntityGarageKit(BlockPos blockPos, BlockState blockState) {
        super(TYPE, blockPos, blockState);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        getTileData().putString(FACING_TAG, facing.getSerializedName());
        getTileData().put(EXTRA_DATA, extraData);
        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        facing = Direction.byName(getTileData().getString(FACING_TAG));
        extraData = getTileData().getCompound(EXTRA_DATA);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public Direction getFacing() {
        return facing;
    }

    public CompoundTag getExtraData() {
        return extraData;
    }

    public void setData(Direction facing, CompoundTag extraData) {
        this.facing = facing;
        this.extraData = extraData;
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }




}
