package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityShrine extends TileEntity {
    public static final TileEntityType<TileEntityShrine> TYPE = TileEntityType.Builder.of(TileEntityShrine::new, InitBlocks.SHRINE.get()).build(null);
    private static final String STORAGE_ITEM = "StorageItem";
    private final ItemStackHandler handler = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() == InitItems.FILM.get();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public TileEntityShrine() {
        super(TYPE);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        getTileData().put(STORAGE_ITEM, handler.serializeNBT());
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        handler.deserializeNBT(getTileData().getCompound(STORAGE_ITEM));
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

    public ItemStack getStorageItem() {
        return handler.getStackInSlot(0);
    }

    public void insertStorageItem(ItemStack stack) {
        handler.insertItem(0, stack, false);
    }

    public ItemStack extractStorageItem() {
        return handler.extractItem(0, 1, false);
    }

    public boolean isEmpty() {
        return handler.getStackInSlot(0).isEmpty();
    }

    public boolean canInsert(ItemStack stack) {
        return handler.isItemValid(0, stack);
    }
}
