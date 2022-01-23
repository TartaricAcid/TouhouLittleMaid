package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public final class MiscConfig {
    public static ForgeConfigSpec.DoubleValue MAID_FAIRY_POWER_POINT;
    public static ForgeConfigSpec.IntValue MAID_FAIRY_SPAWN_PROBABILITY;
    public static ForgeConfigSpec.DoubleValue PLAYER_DEATH_LOSS_POWER_POINT;
    public static ForgeConfigSpec.BooleanValue GIVE_SMART_SLAB;
    public static ForgeConfigSpec.DoubleValue SHRINE_LAMP_EFFECT_COST;
    public static ForgeConfigSpec.DoubleValue SHRINE_LAMP_MAX_STORAGE;
    public static ForgeConfigSpec.IntValue SHRINE_LAMP_MAX_RANGE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("misc");

        builder.comment("Maid fairy's power point");
        MAID_FAIRY_POWER_POINT = builder.defineInRange("MaidFairyPowerPoint", 0.16, 0, 5);

        builder.comment("Maid fairy's spawn probability (zombie is 100, enderman is 10)");
        MAID_FAIRY_SPAWN_PROBABILITY = builder.defineInRange("MaidFairySpawnProbability", 70, 0, Integer.MAX_VALUE);

        builder.comment("Loss power point after player death");
        PLAYER_DEATH_LOSS_POWER_POINT = builder.defineInRange("PlayerDeathLossPowerPoint", 1.0, 0, 5);

        builder.comment("Give a soul spell item for player first join");
        GIVE_SMART_SLAB = builder.define("GiveSoulSpell", true);

        builder.comment("Shrine Lamp Effect Cost (Power Point/Per Hour)");
        SHRINE_LAMP_EFFECT_COST = builder.defineInRange("ShrineLampEffectCost", 0.9, 0, Double.MAX_VALUE);

        builder.comment("Shrine Lamp Max Storage Power Point");
        SHRINE_LAMP_MAX_STORAGE = builder.defineInRange("ShrineLampMaxStorage", 100, 0, Double.MAX_VALUE);

        builder.comment("Shrine Lamp Max Range Of Absorb Power Point");
        SHRINE_LAMP_MAX_RANGE = builder.defineInRange("MaidFairySpawnProbability", 6, 0, Integer.MAX_VALUE);

        builder.pop();
    }
}
