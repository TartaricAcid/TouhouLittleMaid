package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public final class MaidConfig {
    public static final String TAG_PREFIX = "#";
    public static ForgeConfigSpec.ConfigValue<String> MAID_TAMED_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> MAID_TEMPTATION_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> MAID_NTR_ITEM;
    public static ForgeConfigSpec.IntValue MAID_WORK_RANGE;
    public static ForgeConfigSpec.IntValue MAID_IDLE_RANGE;
    public static ForgeConfigSpec.IntValue MAID_SLEEP_RANGE;
    public static ForgeConfigSpec.IntValue MAID_NON_HOME_RANGE;
    public static ForgeConfigSpec.IntValue FEED_ANIMAL_MAX_NUMBER;
    public static ForgeConfigSpec.BooleanValue MAID_CHANGE_MODEL;
    public static ForgeConfigSpec.BooleanValue MAID_GOMOKU_OWNER_LIMIT;
    public static ForgeConfigSpec.IntValue OWNER_MAX_MAID_NUM;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_BACKPACK_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_ATTACK_IGNORE;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_RANGED_ATTACK_IGNORE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("maid");

        builder.comment("The item that can tamed maid", "Use the registered name of the item directly or write tag name with # as prefix");
        MAID_TAMED_ITEM = builder.define("MaidTamedItem", "minecraft:cake");

        builder.comment("The item that can temptation maid", "Use the registered name of the item directly or write tag name with # as prefix");
        MAID_TEMPTATION_ITEM = builder.define("MaidTemptationItem", "minecraft:cake");

        builder.comment("The item that can NTR maid", "Use the registered name of the item directly or write tag name with # as prefix");
        MAID_NTR_ITEM = builder.define("MaidNtrItem", "minecraft:structure_void");

        builder.comment("The max range of maid work mode");
        MAID_WORK_RANGE = builder.defineInRange("MaidWorkRange", 12, 3, 64);

        builder.comment("The max range of maid idle mode");
        MAID_IDLE_RANGE = builder.defineInRange("MaidIdleRange", 6, 3, 32);

        builder.comment("The max range of maid sleep mode");
        MAID_SLEEP_RANGE = builder.defineInRange("MaidSleepRange", 6, 3, 32);

        builder.comment("The max range of maid's Non-Home mode");
        MAID_NON_HOME_RANGE = builder.defineInRange("MaidNonHomeRange", 8, 3, 32);

        builder.comment("The max number of animals around when the maid breeds animals");
        FEED_ANIMAL_MAX_NUMBER = builder.defineInRange("FeedAnimalMaxNumber", 50, 6, 65536);

        builder.comment("Maid can switch models freely");
        MAID_CHANGE_MODEL = builder.define("MaidChangeModel", true);

        builder.comment("Maid can only play gomoku with her owner");
        MAID_GOMOKU_OWNER_LIMIT = builder.define("MaidGomokuOwnerLimit", true);

        builder.comment("The maximum number of maids the player own");
        OWNER_MAX_MAID_NUM = builder.defineInRange("OwnerMaxMaidNum", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

        builder.comment("These items cannot be placed in the maid backpack");
        MAID_BACKPACK_BLACKLIST = builder.define("MaidBackpackBlackList", Lists.newArrayList());

        builder.comment("The entity that the maid will not recognize as targets for attack");
        MAID_ATTACK_IGNORE = builder.define("MaidAttackIgnore", Lists.newArrayList());

        builder.comment("The entity that the maid will not hurt when in ranged attack");
        MAID_RANGED_ATTACK_IGNORE = builder.define("MaidRangedAttackIgnore", Lists.newArrayList());

        builder.pop();
    }
}
