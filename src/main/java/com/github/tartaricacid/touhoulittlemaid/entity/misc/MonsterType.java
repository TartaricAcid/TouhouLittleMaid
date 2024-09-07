package com.github.tartaricacid.touhoulittlemaid.entity.misc;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Locale;

public enum MonsterType {
    FRIENDLY,
    NEUTRAL,
    HOSTILE;

    private final MutableComponent component;

    MonsterType() {
        this.component = new TranslatableComponent("gui.touhou_little_maid.monster_type." + this.name().toLowerCase(Locale.ENGLISH));
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
}