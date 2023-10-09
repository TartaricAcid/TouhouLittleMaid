package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public final class MiscConfig {
    public static ForgeConfigSpec.DoubleValue MAID_FAIRY_POWER_POINT;
    public static ForgeConfigSpec.IntValue MAID_FAIRY_SPAWN_PROBABILITY;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> MAID_FAIRY_BLACKLIST_BIOME;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> MAID_FAIRY_BLACKLIST_DIMENSION;
    public static ForgeConfigSpec.DoubleValue PLAYER_DEATH_LOSS_POWER_POINT;
    public static ForgeConfigSpec.BooleanValue GIVE_SMART_SLAB;
    public static ForgeConfigSpec.BooleanValue GIVE_PATCHOULI_BOOK;
    public static ForgeConfigSpec.DoubleValue SHRINE_LAMP_EFFECT_COST;
    public static ForgeConfigSpec.DoubleValue SHRINE_LAMP_MAX_STORAGE;
    public static ForgeConfigSpec.IntValue SHRINE_LAMP_MAX_RANGE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("misc");

        builder.comment("Maid fairy's power point");
        MAID_FAIRY_POWER_POINT = builder.defineInRange("MaidFairyPowerPoint", 0.16, 0, 5);

        builder.comment("Maid fairy's spawn probability (zombie is 100, enderman is 10)");
        MAID_FAIRY_SPAWN_PROBABILITY = builder.defineInRange("MaidFairySpawnProbability", 70, 0, Integer.MAX_VALUE);

        builder.comment("The following biome do not spawn maid fairy");
        MAID_FAIRY_BLACKLIST_BIOME = builder.defineList("MaidFairyBlacklistBiome",
                Lists.newArrayList(Biomes.THE_VOID.location().toString(),
                        Biomes.MUSHROOM_FIELDS.location().toString()), MiscConfig::checkId);

        builder.comment("The following dimension do not spawn maid fairy");
        MAID_FAIRY_BLACKLIST_DIMENSION = builder.defineList("MaidFairyBlacklistDimension",
                Lists.newArrayList(Level.NETHER.location().toString(), Level.END.location().toString(),
                        "twilightforest:twilight_forest"), MiscConfig::checkId);

        builder.comment("Loss power point after player death");
        PLAYER_DEATH_LOSS_POWER_POINT = builder.defineInRange("PlayerDeathLossPowerPoint", 1.0, 0, 5);

        builder.comment("Give a soul spell item for player first join");
        GIVE_SMART_SLAB = builder.define("GiveSoulSpell", true);

        builder.comment("Give the Memorizable Gensokyo book item for player first join");
        GIVE_PATCHOULI_BOOK = builder.define("GivePatchouliBook", true);

        builder.comment("Shrine Lamp Effect Cost (Power Point/Per Hour)");
        SHRINE_LAMP_EFFECT_COST = builder.defineInRange("ShrineLampEffectCost", 0.9, 0, Double.MAX_VALUE);

        builder.comment("Shrine Lamp Max Storage Power Point");
        SHRINE_LAMP_MAX_STORAGE = builder.defineInRange("ShrineLampMaxStorage", 100, 0, Double.MAX_VALUE);

        builder.comment("Shrine Lamp Max Range Of Absorb Power Point");
        SHRINE_LAMP_MAX_RANGE = builder.defineInRange("ShrineLampMaxRange", 6, 0, Integer.MAX_VALUE);

        builder.pop();
    }

    private static boolean checkId(Object o) {
        if (o instanceof String name) {
            return ResourceLocation.isValidResourceLocation(name);
        }
        return false;
    }
}
