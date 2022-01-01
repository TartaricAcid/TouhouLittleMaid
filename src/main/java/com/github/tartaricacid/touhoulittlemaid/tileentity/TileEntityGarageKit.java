package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class TileEntityGarageKit extends TileEntity {
    public static final TileEntityType<TileEntityGarageKit> TYPE = TileEntityType.Builder.of(TileEntityGarageKit::new, InitBlocks.GARAGE_KIT.get()).build(null);
    private static final String FACING_TAG = "GarageKitFacing";
    private static final String EXTRA_DATA = "ExtraData";
    private Direction facing = Direction.NORTH;
    private CompoundNBT extraData = new CompoundNBT();

    public TileEntityGarageKit() {
        super(TYPE);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        getTileData().putString(FACING_TAG, facing.getSerializedName());
        getTileData().put(EXTRA_DATA, extraData);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        facing = Direction.byName(getTileData().getString(FACING_TAG));
        extraData = getTileData().getCompound(EXTRA_DATA);
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

    public Direction getFacing() {
        return facing;
    }

    public CompoundNBT getExtraData() {
        return extraData;
    }

    public void setData(Direction facing, CompoundNBT extraData) {
        this.facing = facing;
        this.extraData = extraData;
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.DEFAULT);
        }
    }
}
