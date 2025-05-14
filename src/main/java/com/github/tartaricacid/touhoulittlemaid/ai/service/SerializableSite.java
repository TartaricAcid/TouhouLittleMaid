package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface SerializableSite<T extends Site> {
    Codec<T> codec();

    T defaultSite();

    default void writeToNetwork(T site, FriendlyByteBuf buffer) {
        codec().encodeStart(NbtOps.INSTANCE, site)
                .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                .ifPresent(tag -> {
                    if (tag instanceof CompoundTag compoundTag) {
                        buffer.writeNbt(compoundTag);
                    }
                });
    }

    default T fromNetwork(FriendlyByteBuf buffer) {
        return codec().parse(NbtOps.INSTANCE, buffer.readNbt())
                .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                .orElse(null);
    }

    static ResourceLocation defaultIcon(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/ai_chat/%s.png".formatted(id));
    }
}
