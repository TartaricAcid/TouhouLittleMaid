package com.github.tartaricacid.touhoulittlemaid.world.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

public final class MaidInfo {
    private final String dimension;
    private final BlockPos chunkPos;
    private final UUID ownerId;
    private final UUID entityId;
    private final long timestamp;
    private final ITextComponent name;

    public MaidInfo(String dimension, BlockPos chunkPos, UUID ownerId, UUID entityId, long timestamp, ITextComponent name) {
        this.dimension = dimension;
        this.chunkPos = chunkPos;
        this.ownerId = ownerId;
        this.entityId = entityId;
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

    public UUID getEntityId() {
        return entityId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ITextComponent getName() {
        return name;
    }
}