package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.Lists;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil.getItemId;

public final class MaidConfig {
    public static final String TAG_PREFIX = "#";
    public static ModConfigSpec.ConfigValue<String> MAID_TAMED_ITEM;
    public static ModConfigSpec.ConfigValue<String> MAID_TEMPTATION_ITEM;
    public static ModConfigSpec.ConfigValue<String> MAID_NTR_ITEM;
    public static ModConfigSpec.IntValue MAID_WORK_RANGE;
    public static ModConfigSpec.IntValue MAID_IDLE_RANGE;
    public static ModConfigSpec.IntValue MAID_SLEEP_RANGE;
    public static ModConfigSpec.IntValue MAID_NON_HOME_RANGE;
    public static ModConfigSpec.IntValue FEED_ANIMAL_MAX_NUMBER;
    public static ModConfigSpec.BooleanValue MAID_CHANGE_MODEL;
    public static ModConfigSpec.BooleanValue MAID_GOMOKU_OWNER_LIMIT;
    public static ModConfigSpec.IntValue OWNER_MAX_MAID_NUM;
    public static ModConfigSpec.DoubleValue REPLACE_ALLAY_PERCENT;

    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_BACKPACK_BLACKLIST;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_ATTACK_IGNORE;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_RANGED_ATTACK_IGNORE;

    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_WORK_MEALS_BLOCK_LIST;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_HOME_MEALS_BLOCK_LIST;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_HEAL_MEALS_BLOCK_LIST;

    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_WORK_MEALS_BLOCK_LIST_REGEX;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_HOME_MEALS_BLOCK_LIST_REGEX;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_HEAL_MEALS_BLOCK_LIST_REGEX;
    public static ModConfigSpec.ConfigValue<List<? extends String>> MAID_EATEN_RETURN_CONTAINER_LIST;

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("maid");

        builder.comment("The item that can tamed maid", "Use the registered name of the item directly or write tag name with # as prefix");
        MAID_TAMED_ITEM = builder.define("MaidTamedItem", "minecraft:cake", (s) -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("The item that can temptation maid", "Use the registered name of the item directly or write tag name with # as prefix");
        MAID_TEMPTATION_ITEM = builder.define("MaidTemptationItem", "minecraft:cake", (s) -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("The item that can NTR maid", "Use the registered name of the item directly or write tag name with # as prefix");
        MAID_NTR_ITEM = builder.define("MaidNtrItem", "minecraft:structure_void", (s) -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

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
        MAID_BACKPACK_BLACKLIST = builder.defineListAllowEmpty("MaidBackpackBlackList", Lists.newArrayList(), () -> "", (obj) -> true);

        builder.comment("The entity that the maid will not recognize as targets for attack");
        MAID_ATTACK_IGNORE = builder.defineListAllowEmpty("MaidAttackIgnore", Lists.newArrayList(), () -> "", (obj) -> true);

        builder.comment("The entity that the maid will not hurt when in ranged attack");
        MAID_RANGED_ATTACK_IGNORE = builder.defineListAllowEmpty("MaidRangedAttackIgnore", Lists.newArrayList(), () -> "", (obj) -> true);

        builder.comment("Percentage chance of replace Allays spawn in pillager outposts with Maids");
        REPLACE_ALLAY_PERCENT = builder.defineInRange("ReplaceAllayPercent", 0.2, 0, 1);

        builder.comment("These items cannot be used as a maid's work meals");
        MAID_WORK_MEALS_BLOCK_LIST = builder.defineListAllowEmpty("MaidWorkMealsBlockList", Lists.newArrayList(getItemId(Items.PUFFERFISH), getItemId(Items.POISONOUS_POTATO), getItemId(Items.ROTTEN_FLESH), getItemId(Items.SPIDER_EYE), getItemId(Items.CHORUS_FRUIT)), () -> "", s -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("These items cannot be used as a maid's home meals");
        MAID_HOME_MEALS_BLOCK_LIST = builder.defineListAllowEmpty("MaidHomeMealsBlockList", Lists.newArrayList(getItemId(Items.PUFFERFISH), getItemId(Items.POISONOUS_POTATO), getItemId(Items.ROTTEN_FLESH), getItemId(Items.SPIDER_EYE), getItemId(Items.CHORUS_FRUIT)), () -> "", s -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("These items cannot be used as a maid's heal meals");
        MAID_HEAL_MEALS_BLOCK_LIST = builder.defineListAllowEmpty("MaidHealMealsBlockList", Lists.newArrayList(getItemId(Items.PUFFERFISH), getItemId(Items.POISONOUS_POTATO), getItemId(Items.ROTTEN_FLESH), getItemId(Items.SPIDER_EYE)), () -> "", s -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("These items cannot be used as a maid's work meals which match the regex");
        MAID_WORK_MEALS_BLOCK_LIST_REGEX = builder.defineListAllowEmpty("MaidWorkMealsBlockListRegEx", Lists.newArrayList(), () -> "", s -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("These items cannot be used as a maid's home meals which match the regex");
        MAID_HOME_MEALS_BLOCK_LIST_REGEX = builder.defineListAllowEmpty("MaidHomeMealsBlockListRegEx", Lists.newArrayList(), () -> "", s -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("These items cannot be used as a maid's heal meals which match the regex");
        MAID_HEAL_MEALS_BLOCK_LIST_REGEX = builder.defineListAllowEmpty("MaidHealMealsBlockListRegEx", Lists.newArrayList(), () -> "", s -> s instanceof String string && !ItemsUtil.getItemStack(string).isEmpty());

        builder.comment("These entries configure the container returned after a maid has eaten", "Eg: [\"minecraft:beetroot_soup\", \"minecraft:bowl\"]");
        MAID_EATEN_RETURN_CONTAINER_LIST = builder.defineListAllowEmpty("MaidEatenReturnContainerList", Lists.newArrayList(), () -> "", s -> {
            if (s instanceof String string) {
                String[] split = string.split(",");
                if (split.length < 2) {
                    return false;
                }
                for (String item : split) {
                    if (ItemsUtil.getItemStack(item).isEmpty()) {
                        return false;
                    }
                }
                return true;
            }

            return false;
        });

        builder.pop();
    }
}
