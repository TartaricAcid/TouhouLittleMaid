package com.github.tartaricacid.touhoulittlemaid.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.attachment.AttachmentType;

public class PowerAttachment {
    public static final float MAX_POWER = 5.0f;
    public static final AttachmentType<PowerAttachment> TYPE = AttachmentType.builder(() -> new PowerAttachment(0))
            .serialize(RecordCodecBuilder.create(ins -> ins.group(Codec.FLOAT.fieldOf("power").forGetter(o -> o.power))
                    .apply(ins, PowerAttachment::new))).build();
    private float power;

    public PowerAttachment(float power) {
        this.power = power;
    }

    public void add(float points) {
        if (points + this.power <= MAX_POWER) {
            this.power += points;
        } else {
            this.power = MAX_POWER;
        }
    }

    public void min(float points) {
        if (points <= this.power) {
            this.power -= points;
        } else {
            this.power = 0.0f;
        }
    }

    public void set(float points) {
        this.power = Mth.clamp(points, 0, MAX_POWER);
    }

    public float get() {
        return this.power;
    }
}
