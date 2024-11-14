package com.github.tartaricacid.touhoulittlemaid.entity.misc;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum MonsterType implements StringRepresentable {
    FRIENDLY,
    NEUTRAL,
    HOSTILE;

    public static final Codec<MonsterType> CODEC = StringRepresentable.fromEnum(MonsterType::values);

    private final MutableComponent component;

    MonsterType() {
        this.component = Component.translatable("gui.touhou_little_maid.monster_type." + this.name().toLowerCase(Locale.ENGLISH));
    }

    public MonsterType getPrevious() {
        int index = this.ordinal() - 1;
        if (index < 0) {
            index = values().length - 1;
        }
        return values()[index % values().length];
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