package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

public enum MaidSchedule {
    // 日程表的模式
    DAY, NIGHT, ALL;

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
