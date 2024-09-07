package com.github.tartaricacid.touhoulittlemaid.entity.misc;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum MonsterType implements StringRepresentable {
    FRIENDLY,
    NEUTRAL,
    HOSTILE;

    public static final Codec<MonsterType> CODEC = StringRepresentable.fromEnum(MonsterType::values);
    public static final StreamCodec<ByteBuf, MonsterType> STREAM_CODEC = StreamCodec.of(
            (byteBuf, type) -> byteBuf.writeInt(type.ordinal()),
            byteBuf -> getTypeByIndex(byteBuf.readInt())
    );

    private final MutableComponent component;

    MonsterType() {
        this.component = Component.translatable("gui.touhou_little_maid.monster_type." + this.name().toLowerCase(Locale.ENGLISH));
    }

    public static MonsterType getTypeByIndex(int index) {
        int length = MonsterType.values().length;
        return MonsterType.values()[Math.min(index, length - 1)];
    }

    public MonsterType getNext() {
        int ordinal = this.ordinal();
        int length = MonsterType.values().length;
        return MonsterType.values()[(ordinal + 1) % length];
    }

    public MutableComponent getComponent() {
        return component;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ENGLISH);
    }
}