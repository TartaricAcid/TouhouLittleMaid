package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

public enum MaidSchedule {
    // 日程表的模式
    DAY, NIGHT, ALL;

    public static final IDataSerializer<MaidSchedule> DATA = new IDataSerializer<MaidSchedule>() {
        @Override
        public void write(PacketBuffer buf, MaidSchedule value) {
            buf.writeEnum(value);
        }

        @Override
        public MaidSchedule read(PacketBuffer buf) {
            return buf.readEnum(MaidSchedule.class);
        }

        @Override
        public MaidSchedule copy(MaidSchedule value) {
            return value;
        }
    };
}
