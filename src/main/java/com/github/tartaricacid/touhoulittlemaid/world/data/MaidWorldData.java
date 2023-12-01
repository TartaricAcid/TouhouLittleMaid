package com.github.tartaricacid.touhoulittlemaid.world.data;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MaidWorldData extends SavedData {
    private static final String IDENTIFIER = "touhou_little_maid_world_data";
    private static final String INFOS_TAG = "MaidInfos";
    private final Map<UUID, List<MaidInfo>> infos = Maps.newHashMap();

    @Nullable
    public static MaidWorldData get(Level level) {
        if (level instanceof ServerLevel) {
            ServerLevel overWorld = level.getServer().getLevel(Level.OVERWORLD);
            if (overWorld == null) {
                return null;
            }
            DimensionDataStorage storage = overWorld.getDataStorage();
            MaidWorldData data = storage.computeIfAbsent(MaidWorldData::load, MaidWorldData::new, IDENTIFIER);
            data.setDirty();
            return data;
        }
        return null;
    }

    public static MaidWorldData load(CompoundTag tag) {
        MaidWorldData data = new MaidWorldData();
        if (tag.contains(INFOS_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag mapTag = tag.getCompound(INFOS_TAG);
            for (String key : mapTag.getAllKeys()) {
                ListTag listTag = mapTag.getList(key, Tag.TAG_COMPOUND);
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundTag infoTag = listTag.getCompound(i);
                    String dimension = infoTag.getString("Dimension");
                    BlockPos chunkPos = NbtUtils.readBlockPos(infoTag.getCompound("ChunkPos"));
                    UUID ownerId = infoTag.getUUID("OwnerId");
                    UUID maidId = infoTag.getUUID("MaidId");
                    long timestamp = infoTag.getLong("Timestamp");
                    MutableComponent name = Component.Serializer.fromJson(infoTag.getString("Name"));
                    List<MaidInfo> maidInfos = data.infos.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
                    maidInfos.add(new MaidInfo(dimension, chunkPos, ownerId, maidId, timestamp, name));
                }
            }
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag mapTag = new CompoundTag();
        infos.forEach((id, data) -> {
            ListTag listTag = new ListTag();
            data.forEach(info -> {
                CompoundTag infoTag = new CompoundTag();
                infoTag.putString("Dimension", info.getDimension());
                infoTag.put("ChunkPos", NbtUtils.writeBlockPos(info.getChunkPos()));
                infoTag.putUUID("OwnerId", info.getOwnerId());
                infoTag.putUUID("MaidId", info.getMaidId());
                infoTag.putLong("Timestamp", info.getTimestamp());
                infoTag.putString("Name", Component.Serializer.toJson(info.getName()));
                listTag.add(infoTag);
            });
            mapTag.put(id.toString(), listTag);
        });
        tag.put(INFOS_TAG, mapTag);
        return tag;
    }

    public void addInfo(MaidInfo info) {
        UUID ownerId = info.getOwnerId();
        List<MaidInfo> maidInfos = this.infos.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
        maidInfos.add(info);
        this.setDirty();
    }

    public void addInfo(EntityMaid maid) {
        String dimension = maid.level.dimension().location().toString();
        BlockPos chunkPos = maid.blockPosition();
        UUID ownerId = maid.getOwnerUUID();
        UUID maidId = maid.getUUID();
        long timestamp = System.currentTimeMillis();
        Component name = maid.getDisplayName();
        this.addInfo(new MaidInfo(dimension, chunkPos, ownerId, maidId, timestamp, name));
    }

    public void removeInfo(EntityMaid maid) {
        UUID ownerId = maid.getOwnerUUID();
        if (this.infos.containsKey(ownerId)) {
            UUID maidId = maid.getUUID();
            this.infos.get(ownerId).removeIf(info -> info.getMaidId().equals(maidId));
            this.setDirty();
        }
    }

    @Nullable
    public List<MaidInfo> getInfos(UUID owner) {
        return infos.get(owner);
    }

    @Nullable
    public List<MaidInfo> getPlayerMaidInfos(Player player) {
        return this.infos.get(player.getUUID());
    }
}
