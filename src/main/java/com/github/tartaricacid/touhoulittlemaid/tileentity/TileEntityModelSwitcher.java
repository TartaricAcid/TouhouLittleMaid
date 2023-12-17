package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TileEntityModelSwitcher extends TileEntity {
    public static final String INFO_LIST = "info_list";
    public static final String ENTITY_UUID = "entity_uuid";    public static final TileEntityType<TileEntityModelSwitcher> TYPE = TileEntityType.Builder.of(TileEntityModelSwitcher::new, InitBlocks.MODEL_SWITCHER.get()).build(null);
    public static final String LIST_INDEX = "list_index";
    private List<ModeInfo> infoList = Lists.newArrayList();
    private boolean isPowered;
    private UUID uuid;
    private int index;
    public TileEntityModelSwitcher() {
        super(TYPE);
    }

    @Override
    public CompoundNBT save(CompoundNBT pTag) {
        ListNBT listTag = new ListNBT();
        for (ModeInfo info : infoList) {
            listTag.add(info.serialize());
        }
        getTileData().put(INFO_LIST, listTag);
        if (this.uuid != null) {
            getTileData().put(ENTITY_UUID, NBTUtil.createUUID(this.uuid));
        }
        getTileData().putInt(LIST_INDEX, this.index);
        return super.save(pTag);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        infoList.clear();
        ListNBT listTag = getTileData().getList(INFO_LIST, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            ModeInfo info = new ModeInfo();
            info.deserialize(listTag.getCompound(i));
            infoList.add(info);
        }
        INBT uuidTag = getTileData().get(ENTITY_UUID);
        if (uuidTag != null) {
            this.uuid = NBTUtil.loadUUID(uuidTag);
        }
        this.index = getTileData().getInt(LIST_INDEX);
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

    @Nullable
    public UUID getUuid() {
        return uuid;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.refresh();
    }

    public boolean isPowered() {
        return isPowered;
    }

    public void setPowered(boolean powered) {
        isPowered = powered;
    }

    @Nullable
    public ModeInfo getModelInfo() {
        if (0 <= index && index < infoList.size()) {
            return infoList.get(this.index);
        }
        return null;
    }

    public List<ModeInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<ModeInfo> infoList) {
        this.infoList = infoList;
        this.refresh();
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.DEFAULT);
        }
    }

    public static class ModeInfo {
        private ResourceLocation modelId;
        private String text;
        private Direction direction;

        public ModeInfo() {
        }

        public ModeInfo(ResourceLocation modelId, String text, Direction direction) {
            this.modelId = modelId;
            this.text = text;
            this.direction = direction;
        }

        public static ModeInfo fromBuf(PacketBuffer buf) {
            return new ModeInfo(buf.readResourceLocation(), buf.readUtf(), Direction.from2DDataValue(buf.readVarInt()));
        }

        public ResourceLocation getModelId() {
            return modelId;
        }

        public void setModelId(ResourceLocation modelId) {
            this.modelId = modelId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public void toBuf(PacketBuffer buf) {
            buf.writeResourceLocation(this.modelId);
            buf.writeUtf(this.text);
            buf.writeVarInt(this.direction.get2DDataValue());
        }

        public CompoundNBT serialize() {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("model_id", this.modelId.toString());
            tag.putString("text", this.text);
            tag.putInt("direction", this.direction.get2DDataValue());
            return tag;
        }

        public void deserialize(CompoundNBT nbt) {
            this.modelId = new ResourceLocation(nbt.getString("model_id"));
            this.text = nbt.getString("text");
            this.direction = Direction.from2DDataValue(nbt.getInt("direction"));
        }
    }


}