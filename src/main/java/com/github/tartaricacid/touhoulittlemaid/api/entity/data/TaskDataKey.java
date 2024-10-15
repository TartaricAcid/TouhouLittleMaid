package com.github.tartaricacid.touhoulittlemaid.api.entity.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface TaskDataKey<T> {
    /**
     * 该 Data 的注册名，用于在存储 NBT 作为 KEY
     */
    ResourceLocation getKey();

    /**
     * 存储成 NBT
     */
    CompoundTag writeSaveData(T data);

    /**
     * 从 NBT 里读取为数据
     */
    T readSaveData(CompoundTag compound);

    /**
     * 网络同步，因为网络占用带宽，所以不一定代表服务端所有的数据都需要同步到客户端
     */
    default CompoundTag writeSyncData(T data) {
        return this.writeSaveData(data);
    }

    /**
     * 网络同步，客户端读取方法
     */
    default T readSyncData(CompoundTag compound) {
        return this.readSaveData(compound);
    }
}