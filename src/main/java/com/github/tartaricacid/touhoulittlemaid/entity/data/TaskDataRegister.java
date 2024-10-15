package com.github.tartaricacid.touhoulittlemaid.entity.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.init.InitTaskData;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class TaskDataRegister {
    private static final Map<ResourceLocation, TaskDataKey<?>> MAPS = Maps.newHashMap();

    public static void init() {
        TaskDataRegister register = new TaskDataRegister();
        // 注册本模组自己的数据
        InitTaskData.registerAll(register);
        // 注册第三方模组添加的数据
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerTaskData(register);
        }
    }

    @SuppressWarnings("all")
    public static <T> TaskDataKey<T> getValue(ResourceLocation key) {
        return (TaskDataKey<T>) MAPS.get(key);
    }

    public <T> TaskDataKey<T> register(ResourceLocation key, Codec<T> codec) {
        return register(key, codec, codec);
    }

    public <T> TaskDataKey<T> register(ResourceLocation key, Codec<T> saveCodec, Codec<T> syncCodec) {
        TaskDataKey<T> value = new TaskDataKey<>() {
            @Override
            public ResourceLocation getKey() {
                return key;
            }

            @Override
            public CompoundTag writeSaveData(T data) {
                return saveCodec.encodeStart(NbtOps.INSTANCE, data)
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .map(tag -> (CompoundTag) tag)
                        .orElse(new CompoundTag());
            }

            @Override
            public T readSaveData(CompoundTag compound) {
                return saveCodec.parse(NbtOps.INSTANCE, compound)
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .orElse(null);
            }

            @Override
            public CompoundTag writeSyncData(T data) {
                return syncCodec.encodeStart(NbtOps.INSTANCE, data)
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .map(tag -> (CompoundTag) tag)
                        .orElse(new CompoundTag());
            }

            @Override
            public T readSyncData(CompoundTag compound) {
                return syncCodec.parse(NbtOps.INSTANCE, compound)
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .orElse(null);
            }
        };
        return register(value);
    }

    public <T> TaskDataKey<T> register(TaskDataKey<T> value) {
        MAPS.put(value.getKey(), value);
        return value;
    }
}
