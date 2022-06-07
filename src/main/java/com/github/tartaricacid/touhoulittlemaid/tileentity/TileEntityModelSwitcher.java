package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TileEntityModelSwitcher extends BlockEntity {
    public static final BlockEntityType<TileEntityModelSwitcher> TYPE = BlockEntityType.Builder.of(TileEntityModelSwitcher::new, InitBlocks.MODEL_SWITCHER.get()).build(null);

    public static final String INFO_LIST = "info_list";
    public static final String ENTITY_UUID = "entity_uuid";
    public static final String LIST_INDEX = "list_index";

    private List<ModeInfo> infoList = Lists.newArrayList();
    private boolean isPowered;
    private UUID uuid;
    private int index;

    public TileEntityModelSwitcher(BlockPos pWorldPosition, BlockState pBlockState) {
        super(TYPE, pWorldPosition, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        ListTag listTag = new ListTag();
        for (ModeInfo info : infoList) {
            listTag.add(info.serialize());
        }
        getTileData().put(INFO_LIST, listTag);
        if (this.uuid != null) {
            getTileData().put(ENTITY_UUID, NbtUtils.createUUID(this.uuid));
        }
        getTileData().putInt(LIST_INDEX, this.index);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        infoList.clear();
        ListTag listTag = getTileData().getList(INFO_LIST, Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            ModeInfo info = new ModeInfo();
            info.deserialize(listTag.getCompound(i));
            infoList.add(info);
        }
        Tag uuidTag = getTileData().get(ENTITY_UUID);
        if (uuidTag != null) {
            this.uuid = NbtUtils.loadUUID(uuidTag);
        }
        this.index = getTileData().getInt(LIST_INDEX);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
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

        public static ModeInfo fromBuf(FriendlyByteBuf buf) {
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

        public void toBuf(FriendlyByteBuf buf) {
            buf.writeResourceLocation(this.modelId);
            buf.writeUtf(this.text);
            buf.writeVarInt(this.direction.get2DDataValue());
        }

        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.putString("model_id", this.modelId.toString());
            tag.putString("text", this.text);
            tag.putInt("direction", this.direction.get2DDataValue());
            return tag;
        }

        public void deserialize(CompoundTag nbt) {
            this.modelId = new ResourceLocation(nbt.getString("model_id"));
            this.text = nbt.getString("text");
            this.direction = Direction.from2DDataValue(nbt.getInt("direction"));
        }
    }
}
