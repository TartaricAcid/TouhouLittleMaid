package com.github.tartaricacid.touhoulittlemaid.world.data;

import net.minecraft.core.BlockPos;

import java.util.UUID;

public final class MaidInfo {
    private final String dimension;
    private final BlockPos chunkPos;
    private final UUID ownerId;
    private final UUID maidId;

    public MaidInfo(String dimension, BlockPos chunkPos, UUID ownerId, UUID maidId) {
        this.dimension = dimension;
        this.chunkPos = chunkPos;
        this.ownerId = ownerId;
        this.maidId = maidId;
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
}