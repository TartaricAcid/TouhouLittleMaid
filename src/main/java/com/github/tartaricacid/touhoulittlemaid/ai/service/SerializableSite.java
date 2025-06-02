package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * 站点序列化接口，用于站点配置的读取与保存，还有站点配置的网络通信
 *
 * @param <T>
 */
public interface SerializableSite<T extends Site> {
    /**
     * 站点序列化反序列化的编码器
     */
    Codec<T> codec();

    /**
     * 生成一个默认站点数据，用于配置文件初始化时生成一个默认配置
     */
    T defaultSite();

    /**
     * 将站点配置写入网络数据包
     */
    default void writeToNetwork(T site, FriendlyByteBuf buffer) {
        codec().encodeStart(NbtOps.INSTANCE, site)
                .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                .ifPresent(tag -> {
                    if (tag instanceof CompoundTag compoundTag) {
                        buffer.writeNbt(compoundTag);
                    }
                });
    }

    /**
     * 从网络数据包读取站点配置
     */
    default T fromNetwork(FriendlyByteBuf buffer) {
        return codec().parse(NbtOps.INSTANCE, buffer.readNbt())
                .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                .orElse(null);
    }

    /**
     * 工具方法，通过站点 ID 获取一个默认图标地址
     */
    static ResourceLocation defaultIcon(String id) {
        return ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/ai_chat/%s.png".formatted(id));
    }
}
