package com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public enum Level {
    // 好感度等级
    VERY_HIGH(2048, 20, 80),
    HIGH(512, 10, 40),
    NORMAL(0, 2, 20),
    LOW(-512, 2, 20),
    VERY_LOW(-2048, 2, 20);

    private final int count;
    private final ResourceLocation icon;
    private final float attackValue;
    private final float healthyValue;

    Level(int count, float attackValue, float healthyValue) {
        this.count = count;
        this.icon = new ResourceLocation(TouhouLittleMaid.MOD_ID, String.format("textures/gui/%s.png", this.name().toLowerCase(Locale.US)));
        this.attackValue = attackValue;
        this.healthyValue = healthyValue;
    }

    public static Level getLevelByCount(int count) {
        for (Level level : Level.values()) {
            if (count >= level.getCount()) {
                return level;
            }
        }
        return VERY_LOW;
    }

    public int getCount() {
        return count;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public float getAttackValue() {
        return attackValue;
    }

    public float getHealthyValue() {
        return healthyValue;
    }
}
