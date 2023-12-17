package com.github.tartaricacid.touhoulittlemaid.world.data;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MaidWorldData extends WorldSavedData {
    private static final String IDENTIFIER = "touhou_little_maid_world_data";
    private static final String MAID_INFOS_TAG = "MaidInfos";
    private static final String MAID_TOMBSTONES_TAG = "MaidTombstones";
    private final Map<UUID, List<MaidInfo>> infos = Maps.newHashMap();
    private final Map<UUID, List<MaidInfo>> tombstones = Maps.newHashMap();

    public MaidWorldData() {
        super(IDENTIFIER);
    }

    @Nullable
    public static MaidWorldData get(World level) {
        if (level instanceof ServerWorld) {
            ServerWorld overWorld = level.getServer().getLevel(World.OVERWORLD);
            if (overWorld == null) {
                return null;
            }
            DimensionSavedDataManager storage = overWorld.getDataStorage();
            MaidWorldData data = storage.computeIfAbsent(MaidWorldData::load, MaidWorldData::new, IDENTIFIER);
            data.setDirty();
            return data;
        }
        return null;
    }

    public static MaidWorldData load(CompoundNBT tag) {
        MaidWorldData data = new MaidWorldData();
        if (tag.contains(MAID_INFOS_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag infosTag = tag.getCompound(MAID_INFOS_TAG);
            for (String key : infosTag.getAllKeys()) {
                ListTag listTag = infosTag.getList(key, Tag.TAG_COMPOUND);
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
        if (tag.contains(MAID_TOMBSTONES_TAG, Tag.TAG_COMPOUND)) {
            CompoundTag tombstonesTag = tag.getCompound(MAID_TOMBSTONES_TAG);
            for (String key : tombstonesTag.getAllKeys()) {
                ListTag listTag = tombstonesTag.getList(key, Tag.TAG_COMPOUND);
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundTag infoTag = listTag.getCompound(i);
                    String dimension = infoTag.getString("Dimension");
                    BlockPos chunkPos = NbtUtils.readBlockPos(infoTag.getCompound("ChunkPos"));
                    UUID ownerId = infoTag.getUUID("OwnerId");
                    UUID tombstoneId = infoTag.getUUID("TombstoneId");
                    long timestamp = infoTag.getLong("Timestamp");
                    MutableComponent name = Component.Serializer.fromJson(infoTag.getString("Name"));
                    List<MaidInfo> tombstoneInfos = data.tombstones.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
                    tombstoneInfos.add(new MaidInfo(dimension, chunkPos, ownerId, tombstoneId, timestamp, name));
                }
            }
        }
        return data;
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        CompoundTag infosTag = new CompoundTag();
        infos.forEach((id, data) -> {
            ListTag listTag = new ListTag();
            data.forEach(info -> {
                CompoundTag infoTag = new CompoundTag();
                infoTag.putString("Dimension", info.getDimension());
                infoTag.put("ChunkPos", NbtUtils.writeBlockPos(info.getChunkPos()));
                infoTag.putUUID("OwnerId", info.getOwnerId());
                infoTag.putUUID("MaidId", info.getEntityId());
                infoTag.putLong("Timestamp", info.getTimestamp());
                infoTag.putString("Name", Component.Serializer.toJson(info.getName()));
                listTag.add(infoTag);
            });
            infosTag.put(id.toString(), listTag);
        });

        CompoundTag tombstonesTag = new CompoundTag();
        tombstones.forEach((id, data) -> {
            ListTag listTag = new ListTag();
            data.forEach(info -> {
                CompoundTag infoTag = new CompoundTag();
                infoTag.putString("Dimension", info.getDimension());
                infoTag.put("ChunkPos", NbtUtils.writeBlockPos(info.getChunkPos()));
                infoTag.putUUID("OwnerId", info.getOwnerId());
                infoTag.putUUID("TombstoneId", info.getEntityId());
                infoTag.putLong("Timestamp", info.getTimestamp());
                infoTag.putString("Name", Component.Serializer.toJson(info.getName()));
                listTag.add(infoTag);
            });
            tombstonesTag.put(id.toString(), listTag);
        });
        tag.put(MAID_INFOS_TAG, infosTag);
        tag.put(MAID_TOMBSTONES_TAG, tombstonesTag);
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
            this.infos.get(ownerId).removeIf(info -> info.getEntityId().equals(maidId));
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

    public void addTombstones(MaidInfo info) {
        UUID ownerId = info.getOwnerId();
        List<MaidInfo> tombstoneInfos = this.tombstones.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
        tombstoneInfos.add(info);
        this.setDirty();
    }

    public void addTombstones(EntityMaid maid, EntityTombstone tombstone) {
        String dimension = maid.level.dimension().location().toString();
        BlockPos chunkPos = maid.blockPosition();
        UUID ownerId = maid.getOwnerUUID();
        UUID tombstoneId = tombstone.getUUID();
        long timestamp = System.currentTimeMillis();
        Component name = maid.getDisplayName();
        this.addTombstones(new MaidInfo(dimension, chunkPos, ownerId, tombstoneId, timestamp, name));
    }

    public void removeTombstones(EntityTombstone tombstone) {
        UUID ownerId = tombstone.getOwnerId();
        if (this.tombstones.containsKey(ownerId)) {
            UUID tombstoneId = tombstone.getUUID();
            this.tombstones.get(ownerId).removeIf(info -> info.getEntityId().equals(tombstoneId));
            this.setDirty();
        }
    }

    @Nullable
    public List<MaidInfo> getTombstones(UUID owner) {
        return tombstones.get(owner);
    }

    @Nullable
    public List<MaidInfo> getPlayerMaidTombstones(Player player) {
        return this.tombstones.get(player.getUUID());
    }
}
