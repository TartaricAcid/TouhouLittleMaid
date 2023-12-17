package com.github.tartaricacid.touhoulittlemaid.world.data;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

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
            MaidWorldData data = storage.computeIfAbsent(MaidWorldData::new, IDENTIFIER);
            data.setDirty();
            return data;
        }
        return null;
    }


    public void load(CompoundNBT tag) {
        if (tag.contains(MAID_INFOS_TAG, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT infosTag = tag.getCompound(MAID_INFOS_TAG);
            for (String key : infosTag.getAllKeys()) {
                ListNBT listTag = infosTag.getList(key, Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundNBT infoTag = listTag.getCompound(i);
                    String dimension = infoTag.getString("Dimension");
                    BlockPos chunkPos = NBTUtil.readBlockPos(infoTag.getCompound("ChunkPos"));
                    UUID ownerId = infoTag.getUUID("OwnerId");
                    UUID maidId = infoTag.getUUID("MaidId");
                    long timestamp = infoTag.getLong("Timestamp");
                    IFormattableTextComponent name = ITextComponent.Serializer.fromJson(infoTag.getString("Name"));
                    List<MaidInfo> maidInfos = this.infos.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
                    maidInfos.add(new MaidInfo(dimension, chunkPos, ownerId, maidId, timestamp, name));
                }
            }
        }
        if (tag.contains(MAID_TOMBSTONES_TAG, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT tombstonesTag = tag.getCompound(MAID_TOMBSTONES_TAG);
            for (String key : tombstonesTag.getAllKeys()) {
                ListNBT listTag = tombstonesTag.getList(key, Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < listTag.size(); i++) {
                    CompoundNBT infoTag = listTag.getCompound(i);
                    String dimension = infoTag.getString("Dimension");
                    BlockPos chunkPos = NBTUtil.readBlockPos(infoTag.getCompound("ChunkPos"));
                    UUID ownerId = infoTag.getUUID("OwnerId");
                    UUID tombstoneId = infoTag.getUUID("TombstoneId");
                    long timestamp = infoTag.getLong("Timestamp");
                    IFormattableTextComponent name = ITextComponent.Serializer.fromJson(infoTag.getString("Name"));
                    List<MaidInfo> tombstoneInfos = this.tombstones.computeIfAbsent(ownerId, uuid -> Lists.newArrayList());
                    tombstoneInfos.add(new MaidInfo(dimension, chunkPos, ownerId, tombstoneId, timestamp, name));
                }
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        CompoundNBT infosTag = new CompoundNBT();
        infos.forEach((id, data) -> {
            ListNBT listTag = new ListNBT();
            data.forEach(info -> {
                CompoundNBT infoTag = new CompoundNBT();
                infoTag.putString("Dimension", info.getDimension());
                infoTag.put("ChunkPos", NBTUtil.writeBlockPos(info.getChunkPos()));
                infoTag.putUUID("OwnerId", info.getOwnerId());
                infoTag.putUUID("MaidId", info.getEntityId());
                infoTag.putLong("Timestamp", info.getTimestamp());
                infoTag.putString("Name", ITextComponent.Serializer.toJson(info.getName()));
                listTag.add(infoTag);
            });
            infosTag.put(id.toString(), listTag);
        });

        CompoundNBT tombstonesTag = new CompoundNBT();
        tombstones.forEach((id, data) -> {
            ListNBT listTag = new ListNBT();
            data.forEach(info -> {
                CompoundNBT infoTag = new CompoundNBT();
                infoTag.putString("Dimension", info.getDimension());
                infoTag.put("ChunkPos", NBTUtil.writeBlockPos(info.getChunkPos()));
                infoTag.putUUID("OwnerId", info.getOwnerId());
                infoTag.putUUID("TombstoneId", info.getEntityId());
                infoTag.putLong("Timestamp", info.getTimestamp());
                infoTag.putString("Name", ITextComponent.Serializer.toJson(info.getName()));
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
        ITextComponent name = maid.getDisplayName();
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
    public List<MaidInfo> getPlayerMaidInfos(PlayerEntity player) {
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
        ITextComponent name = maid.getDisplayName();
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
    public List<MaidInfo> getPlayerMaidTombstones(PlayerEntity player) {
        return this.tombstones.get(player.getUUID());
    }
}
