package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_WORK_MEALS_BLOCK_LIST;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_HOME_MEALS_BLOCK_LIST;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_HEAL_MEALS_BLOCK_LIST;

    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_WORK_MEALS_BLOCK_LIST_REGEX;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_HOME_MEALS_BLOCK_LIST_REGEX;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_HEAL_MEALS_BLOCK_LIST_REGEX;

    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_EATEN_CLASS_LIST;
    public static ForgeConfigSpec.ConfigValue<List<String>> MAID_EATEN_RETURN_CONTAINER_LIST;

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

        builder.comment("These items cannot be used as a maid's work meals");
        MAID_WORK_MEALS_BLOCK_LIST = builder.define("MaidWorkMealsBlockList", Lists.newArrayList(
                getItemId(Items.PUFFERFISH),
                getItemId(Items.POISONOUS_POTATO),
                getItemId(Items.ROTTEN_FLESH),
                getItemId(Items.SPIDER_EYE),
                getItemId(Items.CHORUS_FRUIT)
        ));

        builder.comment("These items cannot be used as a maid's home meals");
        MAID_HOME_MEALS_BLOCK_LIST = builder.define("MaidHomeMealsBlockList", Lists.newArrayList(
                getItemId(Items.PUFFERFISH),
                getItemId(Items.POISONOUS_POTATO),
                getItemId(Items.ROTTEN_FLESH),
                getItemId(Items.SPIDER_EYE),
                getItemId(Items.CHORUS_FRUIT)
        ));

        builder.comment("These items cannot be used as a maid's heal meals");
        MAID_HEAL_MEALS_BLOCK_LIST = builder.define("MaidHealMealsBlockList", Lists.newArrayList(
                getItemId(Items.PUFFERFISH),
                getItemId(Items.POISONOUS_POTATO),
                getItemId(Items.ROTTEN_FLESH),
                getItemId(Items.SPIDER_EYE)
        ));

        builder.comment("These items cannot be used as a maid's work meals which match the regex");
        MAID_WORK_MEALS_BLOCK_LIST_REGEX = builder.define("MaidWorkMealsBlockListRegEx", Lists.newArrayList(
        ));

        builder.comment("These items cannot be used as a maid's home meals which match the regex");
        MAID_HOME_MEALS_BLOCK_LIST_REGEX = builder.define("MaidHomeMealsBlockListRegEx", Lists.newArrayList(
        ));

        builder.comment("These items cannot be used as a maid's heal meals which match the regex");
        MAID_HEAL_MEALS_BLOCK_LIST_REGEX = builder.define("MaidHealMealsBlockListRegEx", Lists.newArrayList(
        ));

        builder.comment("This define which type of food class");
        MAID_EATEN_CLASS_LIST = builder.define("MaidEatenClassList", getEatenReturnInfo().getFirst());

        builder.comment("This define which type of food returns what kind of container");
        MAID_EATEN_RETURN_CONTAINER_LIST = builder.define("MaidEatenReturnContainerList", getEatenReturnInfo().getSecond());

        builder.pop();
    }

    public static LinkedHashMap<Class<?>, ItemStack> getDefaultRetCon() {
        LinkedHashMap<Class<?>, ItemStack> classItemStackLinkedHashMap = new LinkedHashMap<>();
        classItemStackLinkedHashMap.put(BowlFoodItem.class, Items.BOWL.getDefaultInstance());
        classItemStackLinkedHashMap.put(HoneyBottleItem.class, Items.GLASS_BOTTLE.getDefaultInstance());
        return classItemStackLinkedHashMap;
    }

    public static LinkedHashMap<String, String> getCompatRetConMap() {
        LinkedHashMap<String, String> stringStringLinkedHashMap = new LinkedHashMap<>();
        // 注意：子类一定要在前面
        // 农夫乐事系列
        if (ModList.get().isLoaded("miners_delight")){
            stringStringLinkedHashMap.put("com.sammy.minersdelight.content.item.CopperCupFoodItem", "miners_delight:copper_cup");
        }
        if (ModList.get().isLoaded("farmersdelight")){
            stringStringLinkedHashMap.put("vectorwing.farmersdelight.common.item.DrinkableItem", getItemId(Items.GLASS_BOTTLE));
            stringStringLinkedHashMap.put("vectorwing.farmersdelight.common.item.ConsumableItem", getItemId(Items.BOWL));
        }

        // 其他

        return stringStringLinkedHashMap;
    }

    public static Pair<List<String>, List<String>> getEatenReturnInfo() {
        Pair<List<String>, List<String>> list;
        List<String> classList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        getDefaultRetCon().forEach((class1, itemStack) ->{
            classList.add(class1.getName());
            stringList.add(getItemId(itemStack.getItem()));
        });

        getCompatRetConMap().forEach((className, itemId) ->{
            classList.add(className);
            stringList.add(itemId);
        });

        list = new Pair<>(classList, stringList);
        return list;
    }

    private static String getItemId(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        Preconditions.checkNotNull(key);
        return key.toString();
    }
}
