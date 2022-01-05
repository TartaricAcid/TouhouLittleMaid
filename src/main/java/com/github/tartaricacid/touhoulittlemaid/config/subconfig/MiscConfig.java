package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public final class MiscConfig {
    public static ForgeConfigSpec.DoubleValue MAID_FAIRY_POWER_POINT;
    public static ForgeConfigSpec.IntValue MAID_FAIRY_SPAWN_PROBABILITY;
    public static ForgeConfigSpec.DoubleValue PLAYER_DEATH_LOSS_POWER_POINT;
    public static ForgeConfigSpec.BooleanValue GIVE_SMART_SLAB;

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

        builder.pop();
    }
}
