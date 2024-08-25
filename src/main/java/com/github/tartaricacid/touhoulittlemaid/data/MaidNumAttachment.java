package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.attachment.AttachmentType;

public class MaidNumAttachment {
    public static final AttachmentType<MaidNumAttachment> TYPE = AttachmentType.builder(() -> new MaidNumAttachment(0))
            .serialize(RecordCodecBuilder.create(ins -> ins.group(Codec.INT.fieldOf("num")
                    .forGetter(o -> o.num)).apply(ins, MaidNumAttachment::new))).build();
    private int num;

    public MaidNumAttachment(int num) {
        this.num = num;
    }

    public boolean canAdd() {
        return this.num + 1 <= getMaxNum();
    }

    public void add() {
        this.add(1);
    }

    public void add(int num) {
        if (num + this.num <= getMaxNum()) {
            this.num += num;
        } else {
            this.num = getMaxNum();
        }
    }

    public void min(int num) {
        if (num <= this.num) {
            this.num -= num;
        } else {
            this.num = 0;
        }
    }

    public void set(int num) {
        this.num = Mth.clamp(num, 0, getMaxNum());
    }

    public int getMaxNum() {
        return MaidConfig.OWNER_MAX_MAID_NUM.get();
    }

    public int get() {
        return this.num;
    }
}
