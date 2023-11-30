package com.github.tartaricacid.touhoulittlemaid.world.data;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MaidWorldData extends SavedData {
    private static final String IDENTIFIER = "TouhouLittleMaidWorldData";
    private static final String INFOS_TAG = "MaidInfos";
    private final List<MaidInfo> infos = Lists.newArrayList();

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
        if (tag.contains(INFOS_TAG, Tag.TAG_LIST)) {
            ListTag listTag = tag.getList(INFOS_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag infoTag = listTag.getCompound(i);
                String dimension = infoTag.getString("Dimension");
                BlockPos chunkPos = NbtUtils.readBlockPos(infoTag.getCompound("ChunkPos"));
                UUID ownerId = infoTag.getUUID("OwnerId");
                UUID maidId = infoTag.getUUID("MaidId");
                data.infos.add(new MaidInfo(dimension, chunkPos, ownerId, maidId));
            }
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (MaidInfo info : infos) {
            CompoundTag infoTag = new CompoundTag();
            infoTag.putString("Dimension", info.getDimension());
            infoTag.put("ChunkPos", NbtUtils.writeBlockPos(info.getChunkPos()));
            infoTag.putUUID("OwnerId", info.getOwnerId());
            infoTag.putUUID("MaidId", info.getMaidId());
            listTag.add(infoTag);
        }
        tag.put(INFOS_TAG, listTag);
        return tag;
    }

    public void addInfo(MaidInfo info) {
        this.infos.add(info);
        this.setDirty();
    }

    public void addInfo(EntityMaid maid) {
        String dimension = maid.level.dimension().location().toString();
        BlockPos chunkPos = maid.blockPosition();
        UUID ownerId = maid.getOwnerUUID();
        UUID maidId = maid.getUUID();
        this.addInfo(new MaidInfo(dimension, chunkPos, ownerId, maidId));
    }

    public void removeInfo(UUID maidId) {
        this.infos.removeIf(info -> info.getMaidId().equals(maidId));
        this.setDirty();
    }

    public List<MaidInfo> getInfos() {
        return infos;
    }

    public List<MaidInfo> getPlayerMaidInfos(Player player) {
        return this.infos.stream().filter(info -> info.getOwnerId().equals(player.getUUID())).collect(Collectors.toList());
    }
}
