package com.github.tartaricacid.touhoulittlemaid.entity.data;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class MaidTaskDataMaps {
    private static final String TAG_NAME = "MaidTaskDataMaps";
    private final Reference2ObjectMap<TaskDataKey<?>, Optional<?>> dataMaps = new Reference2ObjectOpenHashMap<>();

    @Nullable
    @SuppressWarnings("all")
    public <T> T getData(TaskDataKey<T> dataKey) {
        Optional<T> optional = (Optional<T>) dataMaps.get(dataKey);
        if (optional != null && optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public <T> T getOrCreateData(TaskDataKey<T> dataKey, T defaultValue) {
        if (dataMaps.containsKey(dataKey)) {
            T data = this.getData(dataKey);
            if (data != null) {
                return data;
            }
        }
        dataMaps.put(dataKey, Optional.of(defaultValue));
        return defaultValue;
    }

    public <T> void setData(TaskDataKey<?> dataKey, T value) {
        dataMaps.put(dataKey, Optional.of(value));
    }

    @SuppressWarnings("all")
    public void writeSaveData(CompoundTag entityTag) {
        CompoundTag dataTags = readOrCreateTag(entityTag);
        dataMaps.forEach((key, value) -> {
            TaskDataKey dataKey = key;
            value.ifPresent(data -> {
                CompoundTag saveData = dataKey.writeSaveData(data);
                dataTags.put(key.getKey().toString(), saveData);
            });
        });
    }

    public void readSaveData(CompoundTag entityTag) {
        dataMaps.clear();
        CompoundTag dataTags = readOrCreateTag(entityTag);
        for (String key : dataTags.getAllKeys()) {
            TaskDataKey<?> dataKey = TaskDataRegister.getValue(new ResourceLocation(key));
            if (dataKey != null) {
                CompoundTag tag = dataTags.getCompound(key);
                dataMaps.put(dataKey, Optional.of(dataKey.readSaveData(tag)));
            }
        }
    }

    @SuppressWarnings("all")
    public CompoundTag getUpdateTag() {
        CompoundTag taskTags = new CompoundTag();
        dataMaps.forEach((key, value) -> {
            TaskDataKey dataKey = key;
            value.ifPresent(data -> {
                CompoundTag syncData = dataKey.writeSyncData(data);
                taskTags.put(key.getKey().toString(), syncData);
            });
        });
        return taskTags;
    }

    @OnlyIn(Dist.CLIENT)
    public void readFromServer(CompoundTag taskTags) {
        dataMaps.clear();
        for (String key : taskTags.getAllKeys()) {
            TaskDataKey<?> dataKey = TaskDataRegister.getValue(new ResourceLocation(key));
            if (dataKey != null) {
                CompoundTag tag = taskTags.getCompound(key);
                dataMaps.put(dataKey, Optional.of(dataKey.readSyncData(tag)));
            }
        }
    }

    @NotNull
    private CompoundTag readOrCreateTag(CompoundTag entityTag) {
        if (entityTag.contains(TAG_NAME)) {
            return entityTag.getCompound(TAG_NAME);
        } else {
            CompoundTag dataTags = new CompoundTag();
            entityTag.put(TAG_NAME, dataTags);
            return dataTags;
        }
    }
}
