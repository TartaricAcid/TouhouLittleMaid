package com.github.tartaricacid.touhoulittlemaid.world.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public final class MaidInfo {
    private final String dimension;
    private final BlockPos chunkPos;
    private final UUID ownerId;
    private final UUID maidId;
    private final long timestamp;
    private final Component name;

    public MaidInfo(String dimension, BlockPos chunkPos, UUID ownerId, UUID maidId, long timestamp, Component name) {
        this.dimension = dimension;
        this.chunkPos = chunkPos;
        this.ownerId = ownerId;
        this.maidId = maidId;
        this.timestamp = timestamp;
        this.name = name;
    }

    public String getDimension() {
        return dimension;
    }

    public BlockPos getChunkPos() {
        return chunkPos;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public UUID getMaidId() {
        return maidId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Component getName() {
        return name;
    }
}