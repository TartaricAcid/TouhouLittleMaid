package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;


import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum MaidSchedule {
    // 日程表的模式
    DAY, NIGHT, ALL;

    public static final IntFunction<MaidSchedule> BY_ID =
            ByIdMap.continuous(
                    MaidSchedule::ordinal,
                    MaidSchedule.values(),
                    ByIdMap.OutOfBoundsStrategy.ZERO
            );
    public static final StreamCodec<ByteBuf, MaidSchedule> STREAM_CODEC = ByteBufCodecs.idMapper(MaidSchedule.BY_ID, MaidSchedule::ordinal);

    public static final EntityDataSerializer<MaidSchedule> DATA = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, MaidSchedule value) {
            buf.writeEnum(value);
        }

        @Override
        public MaidSchedule read(FriendlyByteBuf buf) {
            return buf.readEnum(MaidSchedule.class);
        }

        @Override
        public MaidSchedule copy(MaidSchedule value) {
            return value;
        }
    };
}
