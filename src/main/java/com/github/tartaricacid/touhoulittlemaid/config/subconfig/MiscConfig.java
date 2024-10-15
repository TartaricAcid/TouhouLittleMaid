package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.google.common.collect.Lists;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.isValidResourceLocation;

public final class MiscConfig {
    private static final String TRANSLATE_KEY = "config.touhou_little_maid.misc";
    public static ModConfigSpec.DoubleValue MAID_FAIRY_POWER_POINT;
    public static ModConfigSpec.IntValue MAID_FAIRY_SPAWN_PROBABILITY;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_FAIRY_BLACKLIST_DIMENSION;
    public static ModConfigSpec.DoubleValue PLAYER_DEATH_LOSS_POWER_POINT;
    public static ModConfigSpec.BooleanValue GIVE_SMART_SLAB;
    public static ModConfigSpec.BooleanValue GIVE_PATCHOULI_BOOK;
    public static ModConfigSpec.DoubleValue SHRINE_LAMP_EFFECT_COST;
    public static ModConfigSpec.DoubleValue SHRINE_LAMP_MAX_STORAGE;
    public static ModConfigSpec.IntValue SHRINE_LAMP_MAX_RANGE;
    public static ModConfigSpec.BooleanValue CLOSE_OPTIFINE_WARNING;
    public static ModConfigSpec.IntValue SCARECROW_RANGE;
    public static ModConfigSpec.BooleanValue USE_NEW_MAID_FAIRY_MODEL;
    public static ModConfigSpec.BooleanValue MODEL_ICON_CACHE;

    public static void init(ModConfigSpec.Builder builder) {
        builder.translation(TRANSLATE_KEY).push("misc");

        builder.comment("Maid fairy's power point")
                .translation(translateKey("maid_fairy_power_point"));
        MAID_FAIRY_POWER_POINT = builder.defineInRange("MaidFairyPowerPoint", 0.16, 0, 5);

        builder.comment("Maid fairy's spawn probability (zombie is 100, enderman is 10)")
                .translation(translateKey("maid_fairy_spawn_probability"));
        MAID_FAIRY_SPAWN_PROBABILITY = builder.defineInRange("MaidFairySpawnProbability", 70, 0, Integer.MAX_VALUE);

        builder.comment("The following dimension do not spawn maid fairy")
                .translation(translateKey("maid_fairy_blacklist_dimension"));
        MAID_FAIRY_BLACKLIST_DIMENSION = builder.defineList("MaidFairyBlacklistDimension",
                Lists.newArrayList(Level.NETHER.location().toString(), Level.END.location().toString(),
                        "twilightforest:twilight_forest"), MiscConfig::checkId);

        builder.comment("Loss power point after player death")
                .translation(translateKey("player_death_loss_power_point"));
        PLAYER_DEATH_LOSS_POWER_POINT = builder.defineInRange("PlayerDeathLossPowerPoint", 1.0, 0, 5);

        builder.comment("Give a soul spell item for player first join")
                .translation(translateKey("give_smart_slab"));
        GIVE_SMART_SLAB = builder.define("GiveSoulSpell", true);

        builder.comment("Give the Memorizable Gensokyo book item for player first join")
                .translation(translateKey("give_patchouli_book"));
        GIVE_PATCHOULI_BOOK = builder.define("GivePatchouliBook", true);

        builder.comment("Shrine Lamp Effect Cost (Power Point/Per Hour)")
                .translation(translateKey("shrine_lamp_effect_cost"));
        SHRINE_LAMP_EFFECT_COST = builder.defineInRange("ShrineLampEffectCost", 0.9, 0, Double.MAX_VALUE);

        builder.comment("Shrine Lamp Max Storage Power Point")
                .translation(translateKey("shrine_lamp_max_storage"));
        SHRINE_LAMP_MAX_STORAGE = builder.defineInRange("ShrineLampMaxStorage", 100, 0, Double.MAX_VALUE);

        builder.comment("Shrine Lamp Max Range Of Absorb Power Point")
                .translation(translateKey("shrine_lamp_max_range"));
        SHRINE_LAMP_MAX_RANGE = builder.defineInRange("ShrineLampMaxRange", 6, 0, Integer.MAX_VALUE);

        builder.comment("Whether to turn off the Optifine warning")
                .translation(translateKey("close_optifine_warning"));
        CLOSE_OPTIFINE_WARNING = builder.define("CloseOptifineWarning", false);

        builder.comment("The range of the scarecrow to prevent the fairy maid from spawning")
                .translation(translateKey("scarecrow_range"));
        SCARECROW_RANGE = builder.defineInRange("ScarecrowRange", 16 * 3, 0, Integer.MAX_VALUE);

        builder.comment("Whether to use the new version of the Fairy Maid model")
                .translation(translateKey("use_new_maid_fairy_model"));
        USE_NEW_MAID_FAIRY_MODEL = builder.define("UseNewMaidFairyModel", true);

        builder.comment("Whether to enable model icon caching");
        MODEL_ICON_CACHE = builder.define("ModelIconCache", true);

        builder.pop();
    }

    private static String translateKey(String key) {
        return TRANSLATE_KEY + "." + key;
    }

    private static boolean checkId(Object o) {
        if (o instanceof String name) {
            return isValidResourceLocation(name);
        }
        return false;
    }
}
