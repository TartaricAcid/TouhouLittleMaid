package com.github.tartaricacid.touhoulittlemaid.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CompoundData(CompoundTag nbt) {
    public static final Codec<CompoundData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            CompoundTag.CODEC.fieldOf("nbt").forGetter(o -> o.nbt)
    ).apply(ins, CompoundData::new));

    public static final StreamCodec<ByteBuf, CompoundData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            CompoundData::nbt,
            CompoundData::new
    );

    public static CompoundData of(CompoundTag nbt) {
        return new CompoundData(nbt.copy());
    }
}
