package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileEntityPicnicMat extends BlockEntity {
    public static final BlockEntityType<TileEntityPicnicMat> TYPE = BlockEntityType.Builder.of(TileEntityPicnicMat::new, InitBlocks.PICNIC_MAT.get()).build(null);
    private static final String CENTER_POS_NAME = "CenterPos";
    private static final String STORAGE_ITEM = "StorageItem";
    private static final String SIT_IDS = "SitIds";
    private final ItemStackHandler handler = new ItemStackHandler(9) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getFoodProperties(null) != null;
        }
    };
    private final UUID[] sitIds = new UUID[]{Util.NIL_UUID, Util.NIL_UUID, Util.NIL_UUID, Util.NIL_UUID};
    private BlockPos centerPos = BlockPos.ZERO;

    public TileEntityPicnicMat(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }

    public void setCenterPos(BlockPos centerPos) {
        this.centerPos = centerPos;
        this.refresh();
    }

    public void setSitId(int index, UUID uuid) {
        if (index < 0 || index >= 4) {
            return;
        }
        this.sitIds[index] = uuid;
        this.refresh();
    }

    public UUID[] getSitIds() {
        return sitIds;
    }

    public BlockPos getCenterPos() {
        return centerPos;
    }

    public ItemStack getStorageItem(int slotId) {
        return handler.getStackInSlot(slotId);
    }

    public boolean isEmpty(int slotId) {
        return handler.getStackInSlot(slotId).isEmpty();
    }

    public void setHandler(ItemStackHandler stackHandler) {
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            ItemStack stack = stackHandler.getStackInSlot(i);
            if (i >= this.handler.getSlots()) {
                return;
            }
            this.handler.setStackInSlot(i, stack);
        }
        this.refresh();
    }

    public ItemStackHandler getHandler() {
        return handler;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        getPersistentData().put(CENTER_POS_NAME, NbtUtils.writeBlockPos(centerPos));
        getPersistentData().put(STORAGE_ITEM, handler.serializeNBT(pRegistries));
        ListTag listTag = new ListTag();
        for (UUID uuid : sitIds) {
            listTag.add(NbtUtils.createUUID(uuid));
        }
        getPersistentData().put(SIT_IDS, listTag);
        super.saveAdditional(pTag,pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag,pRegistries);
        var blockPos = NbtUtils.readBlockPos(getPersistentData(),CENTER_POS_NAME);
        blockPos.ifPresent(blockPos1 -> centerPos = blockPos1);
        this.handler.deserializeNBT(pRegistries,getPersistentData().getCompound(STORAGE_ITEM));
        ListTag sitIdsTag = getPersistentData().getList(SIT_IDS, Tag.TAG_INT_ARRAY);
        int i = 0;
        for (Tag tag : sitIdsTag) {
            this.sitIds[i] = NbtUtils.loadUUID(tag);
            i = i + 1;
            if (i >= 4) {
                break;
            }
        }
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public BlockPos getWorldPosition() {
        return this.worldPosition;
    }
}
