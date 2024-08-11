package com.github.tartaricacid.touhoulittlemaid.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TrackInfo(String dimension, BlockPos position) {
    public static final Codec<TrackInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("dimension").forGetter(TrackInfo::dimension),
            BlockPos.CODEC.fieldOf("position").forGetter(TrackInfo::position)
    ).apply(instance, TrackInfo::new));

    public static final StreamCodec<ByteBuf, TrackInfo> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, TrackInfo::dimension,
            BlockPos.STREAM_CODEC, TrackInfo::position,
            TrackInfo::new
    );
}
