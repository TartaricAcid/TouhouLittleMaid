package com.github.tartaricacid.touhoulittlemaid.compat.cloth;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.AddClothConfigEvent;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabConfig;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabRemoteConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.*;
import com.github.tartaricacid.touhoulittlemaid.event.MaidMealRegConfigEvent;
import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuIntegration {
    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder root = ConfigBuilder.create().setTitle(Component.literal("Touhou Little Maid"));
        root.setGlobalized(true);
        root.setGlobalizedExpanded(false);
        ConfigEntryBuilder entryBuilder = root.entryBuilder();
        maidConfig(root, entryBuilder);
        chairConfig(root, entryBuilder);
        miscConfig(root, entryBuilder);
        vanillaConfig(root, entryBuilder);
        renderConfig(root, entryBuilder);
        wabConfig(root, entryBuilder);
        GlobalAIIntegration.aiChat(root, entryBuilder);
        MinecraftForge.EVENT_BUS.post(new AddClothConfigEvent(root, entryBuilder));
        return root;
    }

    @SuppressWarnings("all")
    private static void maidConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory maid = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.maid"));

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.global_maid_sound_frequency"), MaidConfig.GLOBAL_MAID_SOUND_FREQUENCY.get(), 0, 100)
                .setDefaultValue(100).setTooltip(Component.translatable("config.touhou_little_maid.maid.global_maid_sound_frequency.tooltip"))
                .setSaveConsumer(i -> MaidConfig.GLOBAL_MAID_SOUND_FREQUENCY.set(i)).build());

        maid.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.maid.global_maid_show_chat_bubble"), MaidConfig.GLOBAL_MAID_SHOW_CHAT_BUBBLE.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.maid.global_maid_show_chat_bubble.tooltip"))
                .setSaveConsumer(MaidConfig.GLOBAL_MAID_SHOW_CHAT_BUBBLE::set).build());

        maid.addEntry(entryBuilder.startTextField(Component.translatable("config.touhou_little_maid.maid.maid_tamed_item"),
                        MaidConfig.MAID_TAMED_ITEM.get())
                .setDefaultValue(MaidConfig.MAID_TAMED_ITEM.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_tamed_item.tooltip"))
                .setSaveConsumer(s -> MaidConfig.MAID_TAMED_ITEM.set(s)).build());

        maid.addEntry(entryBuilder.startTextField(Component.translatable("config.touhou_little_maid.maid.maid_tamed_item"),
                        MaidConfig.MAID_TEMPTATION_ITEM.get())
                .setDefaultValue(MaidConfig.MAID_TEMPTATION_ITEM.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_temptation_item.tooltip"))
                .setSaveConsumer(s -> MaidConfig.MAID_TEMPTATION_ITEM.set(s)).build());

        maid.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.maid.enable_maid_curios"),
                        MaidConfig.ENABLE_MAID_CURIOS.get())
                .setDefaultValue(MaidConfig.ENABLE_MAID_CURIOS.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.enable_maid_curios.tooltip"))
                .setSaveConsumer(s -> MaidConfig.ENABLE_MAID_CURIOS.set(s)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.maid_work_range"), MaidConfig.MAID_WORK_RANGE.get(), 3, 64)
                .setDefaultValue(12).setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_work_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.MAID_WORK_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.maid_idle_range"), MaidConfig.MAID_IDLE_RANGE.get(), 3, 32)
                .setDefaultValue(6).setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_idle_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.MAID_IDLE_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.maid_sleep_range"), MaidConfig.MAID_SLEEP_RANGE.get(), 3, 32)
                .setDefaultValue(6).setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_sleep_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.MAID_SLEEP_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.maid_non_home_range"), MaidConfig.MAID_NON_HOME_RANGE.get(), 3, 32)
                .setDefaultValue(8).setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_non_home_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.MAID_NON_HOME_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.bow_range"), MaidConfig.BOW_RANGE.get(), 8, 192)
                .setDefaultValue(48).setTooltip(Component.translatable("config.touhou_little_maid.maid.bow_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.BOW_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.cross_bow_range"), MaidConfig.CROSS_BOW_RANGE.get(), 8, 192)
                .setDefaultValue(64).setTooltip(Component.translatable("config.touhou_little_maid.maid.cross_bow_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.CROSS_BOW_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.danmaku_range"), MaidConfig.DANMAKU_RANGE.get(), 8, 192)
                .setDefaultValue(64).setTooltip(Component.translatable("config.touhou_little_maid.maid.danmaku_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.DANMAKU_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.maid.trident_range"), MaidConfig.TRIDENT_RANGE.get(), 8, 192)
                .setDefaultValue(48).setTooltip(Component.translatable("config.touhou_little_maid.maid.trident_range.tooltip"))
                .setSaveConsumer(i -> MaidConfig.TRIDENT_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.feed_animal_max_number"), MaidConfig.FEED_ANIMAL_MAX_NUMBER.get())
                .setMin(6).setMax(65536).setDefaultValue(50).setTooltip(Component.translatable("config.touhou_little_maid.maid.feed_animal_max_number.tooltip"))
                .setSaveConsumer(i -> MaidConfig.FEED_ANIMAL_MAX_NUMBER.set(i)).build());

        maid.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.maid.maid_change_model"), MaidConfig.MAID_CHANGE_MODEL.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_change_model.tooltip"))
                .setSaveConsumer(MaidConfig.MAID_CHANGE_MODEL::set).build());

        maid.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.maid.maid_gomoku_owner_limit"), MaidConfig.MAID_GOMOKU_OWNER_LIMIT.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_gomoku_owner_limit.tooltip"))
                .setSaveConsumer(MaidConfig.MAID_GOMOKU_OWNER_LIMIT::set).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.owner_max_maid_num"), MaidConfig.OWNER_MAX_MAID_NUM.get())
                .setDefaultValue(Integer.MAX_VALUE).setMin(0).setMax(Integer.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.owner_max_maid_num.tooltip"))
                .setSaveConsumer(i -> MaidConfig.OWNER_MAX_MAID_NUM.set(i)).build());

        maid.addEntry(entryBuilder.startDoubleField(Component.translatable("config.touhou_little_maid.maid.replace_allay_percent"), MaidConfig.REPLACE_ALLAY_PERCENT.get())
                .setDefaultValue(0.2).setMin(0).setMax(1)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.replace_allay_percent.tooltip"))
                .setSaveConsumer(i -> MaidConfig.REPLACE_ALLAY_PERCENT.set(i)).build());

        maid.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.maid.enable_emoji"), MaidConfig.ENABLE_EMOJI.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.maid.enable_emoji.tooltip"))
                .setSaveConsumer(MaidConfig.ENABLE_EMOJI::set).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.emoji_check_rate"), MaidConfig.EMOJI_CHECK_RATE.get())
                .setDefaultValue(MaidConfig.EMOJI_CHECK_RATE.getDefault()).setMin(20).setMax(24000)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.emoji_check_rate.tooltip"))
                .setSaveConsumer(i -> MaidConfig.EMOJI_CHECK_RATE.set(i)).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.image_emoji_weight"), MaidConfig.IMAGE_EMOJI_WEIGHT.get())
                .setDefaultValue(MaidConfig.IMAGE_EMOJI_WEIGHT.getDefault()).setMin(0).setMax(100)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.image_emoji_weight.tooltip"))
                .setSaveConsumer(i -> MaidConfig.IMAGE_EMOJI_WEIGHT.set(i)).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.kaomoji_emoji_weight"), MaidConfig.KAOMOJI_EMOJI_WEIGHT.get())
                .setDefaultValue(MaidConfig.KAOMOJI_EMOJI_WEIGHT.getDefault()).setMin(0).setMax(100)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.kaomoji_emoji_weight.tooltip"))
                .setSaveConsumer(i -> MaidConfig.KAOMOJI_EMOJI_WEIGHT.set(i)).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_backpack_blacklist"), MaidConfig.MAID_BACKPACK_BLACKLIST.get())
                .setDefaultValue(MaidConfig.MAID_BACKPACK_BLACKLIST.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_backpack_blacklist.tooltip"))
                .setSaveConsumer(l -> MaidConfig.MAID_BACKPACK_BLACKLIST.set(l)).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_attack_ignore"), MaidConfig.MAID_ATTACK_IGNORE.get())
                .setDefaultValue(Lists.newArrayList())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_attack_ignore.tooltip"))
                .setSaveConsumer(l -> MaidConfig.MAID_ATTACK_IGNORE.set(l)).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_ranged_attack_ignore"), MaidConfig.MAID_RANGED_ATTACK_IGNORE.get())
                .setDefaultValue(Lists.newArrayList())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_ranged_attack_ignore.tooltip"))
                .setSaveConsumer(l -> MaidConfig.MAID_RANGED_ATTACK_IGNORE.set(l)).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_work_meals_block_list"), MaidConfig.MAID_WORK_MEALS_BLOCK_LIST.get())
                .setDefaultValue(MaidConfig.MAID_WORK_MEALS_BLOCK_LIST.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_work_meals_block_list.tooltip"))
                .setSaveConsumer(l -> MaidConfig.MAID_WORK_MEALS_BLOCK_LIST.set(l)).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_home_meals_block_list"), MaidConfig.MAID_HOME_MEALS_BLOCK_LIST.get())
                .setDefaultValue(MaidConfig.MAID_HOME_MEALS_BLOCK_LIST.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_home_meals_block_list.tooltip"))
                .setSaveConsumer(l -> MaidConfig.MAID_HOME_MEALS_BLOCK_LIST.set(l)).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_heal_meals_block_list"), MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST.get())
                .setDefaultValue(MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_heal_meals_block_list.tooltip"))
                .setSaveConsumer(l -> MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST.set(l)).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_work_meals_block_list_regex"), MaidConfig.MAID_WORK_MEALS_BLOCK_LIST_REGEX.get())
                .setDefaultValue(MaidConfig.MAID_WORK_MEALS_BLOCK_LIST_REGEX.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_work_meals_block_list_regex.tooltip"))
                .setSaveConsumer(l -> {
                    MaidConfig.MAID_WORK_MEALS_BLOCK_LIST_REGEX.set(l);
                    MaidMealRegConfigEvent.handleConfig(MaidConfig.MAID_WORK_MEALS_BLOCK_LIST_REGEX.get(), MaidMealRegConfigEvent.WORK_MEAL_REGEX);
                }).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_home_meals_block_list_regex"), MaidConfig.MAID_HOME_MEALS_BLOCK_LIST_REGEX.get())
                .setDefaultValue(MaidConfig.MAID_HOME_MEALS_BLOCK_LIST_REGEX.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_home_meals_block_list_regex.tooltip"))
                .setSaveConsumer(l -> {
                    MaidConfig.MAID_HOME_MEALS_BLOCK_LIST_REGEX.set(l);
                    MaidMealRegConfigEvent.handleConfig(MaidConfig.MAID_HOME_MEALS_BLOCK_LIST_REGEX.get(), MaidMealRegConfigEvent.HOME_MEAL_REGEX);
                }).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_heal_meals_block_list_regex"), MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST_REGEX.get())
                .setDefaultValue(MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST_REGEX.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_heal_meals_block_list_regex.tooltip"))
                .setSaveConsumer(l -> {
                    MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST_REGEX.set(l);
                    MaidMealRegConfigEvent.handleConfig(MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST_REGEX.get(), MaidMealRegConfigEvent.HEAL_MEAL_REGEX);
                }).build());

        maid.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.maid.maid_eaten_return_container_list"), MaidConfig.MAID_EATEN_RETURN_CONTAINER_LIST.get().stream().map(s -> s.get(0) + "," + s.get(1)).toList())
                .setDefaultValue(MaidConfig.MAID_EATEN_RETURN_CONTAINER_LIST.getDefault().stream().map(s -> s.get(0) + "," + s.get(1)).toList())
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_eaten_return_container_list.tooltip"))
                .setSaveConsumer(l -> {
                    List<List<String>> maidMealContainerList = new ArrayList<>();
                    for (String s : l) {
                        String[] split = s.split(",");
                        if (split.length != 2) continue;
                        maidMealContainerList.add(Arrays.asList(split[0], split[1]));
                    }
                    MaidConfig.MAID_EATEN_RETURN_CONTAINER_LIST.set(maidMealContainerList);
                }).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.maid_gun_long_distance"), MaidConfig.MAID_GUN_LONG_DISTANCE.get())
                .setDefaultValue(64).setMin(0).setMax(512)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_gun_long_distance.tooltip"))
                .setSaveConsumer(i -> MaidConfig.MAID_GUN_LONG_DISTANCE.set(i)).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.maid_gun_medium_distance"), MaidConfig.MAID_GUN_MEDIUM_DISTANCE.get())
                .setDefaultValue(48).setMin(0).setMax(512)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_gun_medium_distance.tooltip"))
                .setSaveConsumer(i -> MaidConfig.MAID_GUN_MEDIUM_DISTANCE.set(i)).build());

        maid.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.maid.maid_gun_near_distance"), MaidConfig.MAID_GUN_NEAR_DISTANCE.get())
                .setDefaultValue(32).setMin(0).setMax(512)
                .setTooltip(Component.translatable("config.touhou_little_maid.maid.maid_gun_near_distance.tooltip"))
                .setSaveConsumer(i -> MaidConfig.MAID_GUN_NEAR_DISTANCE.set(i)).build());
    }

    private static void chairConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory chair = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.chair"));
        chair.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.chair.chair_change_model"), ChairConfig.CHAIR_CHANGE_MODEL.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.chair.chair_change_model.tooltip"))
                .setSaveConsumer(ChairConfig.CHAIR_CHANGE_MODEL::set).build());

        chair.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.chair.chair_can_destroyed_by_anyone"), ChairConfig.CHAIR_CAN_DESTROYED_BY_ANYONE.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.chair.chair_can_destroyed_by_anyone.tooltip"))
                .setSaveConsumer(ChairConfig.CHAIR_CAN_DESTROYED_BY_ANYONE::set).build());
    }

    @SuppressWarnings("all")
    private static void miscConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory misc = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.misc"));
        misc.addEntry(entryBuilder.startDoubleField(Component.translatable("config.touhou_little_maid.misc.maid_fairy_power_point"), MiscConfig.MAID_FAIRY_POWER_POINT.get())
                .setDefaultValue(0.16).setMin(0).setMax(5)
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.maid_fairy_power_point.tooltip"))
                .setSaveConsumer(d -> MiscConfig.MAID_FAIRY_POWER_POINT.set(d)).build());

        misc.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.misc.maid_fairy_spawn_probability"), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get())
                .setDefaultValue(70).setMin(0).setMax(Integer.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.maid_fairy_spawn_probability.tooltip"))
                .setSaveConsumer(d -> MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.set(d)).build());

        misc.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid.misc.maid_fairy_blacklist_dimension"), (List<String>) MiscConfig.MAID_FAIRY_BLACKLIST_DIMENSION.get())
                .setDefaultValue((List<String>) MiscConfig.MAID_FAIRY_BLACKLIST_DIMENSION.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.maid_fairy_blacklist_dimension.tooltip"))
                .setSaveConsumer(l -> MiscConfig.MAID_FAIRY_BLACKLIST_DIMENSION.set(l)).build());

        misc.addEntry(entryBuilder.startDoubleField(Component.translatable("config.touhou_little_maid.misc.player_death_loss_power_point"), MiscConfig.PLAYER_DEATH_LOSS_POWER_POINT.get())
                .setDefaultValue(1.0).setMin(0).setMax(5)
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.player_death_loss_power_point.tooltip"))
                .setSaveConsumer(d -> MiscConfig.PLAYER_DEATH_LOSS_POWER_POINT.set(d)).build());

        misc.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.misc.give_smart_slab"), MiscConfig.GIVE_SMART_SLAB.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.misc.give_smart_slab.tooltip"))
                .setSaveConsumer(MiscConfig.GIVE_SMART_SLAB::set).build());

        misc.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.misc.give_patchouli_book"), MiscConfig.GIVE_PATCHOULI_BOOK.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.misc.give_patchouli_book.tooltip"))
                .setSaveConsumer(MiscConfig.GIVE_PATCHOULI_BOOK::set).build());

        misc.addEntry(entryBuilder.startDoubleField(Component.translatable("config.touhou_little_maid.misc.shrine_lamp_effect_cost"), MiscConfig.SHRINE_LAMP_EFFECT_COST.get())
                .setDefaultValue(0.9).setMin(0).setMax(Double.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.shrine_lamp_effect_cost.tooltip"))
                .setSaveConsumer(d -> MiscConfig.SHRINE_LAMP_EFFECT_COST.set(d)).build());

        misc.addEntry(entryBuilder.startDoubleField(Component.translatable("config.touhou_little_maid.misc.shrine_lamp_max_storage"), MiscConfig.SHRINE_LAMP_MAX_STORAGE.get())
                .setDefaultValue(100).setMin(0).setMax(Double.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.shrine_lamp_max_storage.tooltip"))
                .setSaveConsumer(d -> MiscConfig.SHRINE_LAMP_MAX_STORAGE.set(d)).build());

        misc.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.misc.shrine_lamp_max_range"), MiscConfig.SHRINE_LAMP_MAX_RANGE.get())
                .setDefaultValue(6).setMin(0).setMax(Integer.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.shrine_lamp_max_range.tooltip"))
                .setSaveConsumer(d -> MiscConfig.SHRINE_LAMP_MAX_RANGE.set(d)).build());

        misc.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.misc.close_optifine_warning"), MiscConfig.CLOSE_OPTIFINE_WARNING.get())
                .setDefaultValue(false).setTooltip(Component.translatable("config.touhou_little_maid.misc.close_optifine_warning.tooltip"))
                .setSaveConsumer(MiscConfig.CLOSE_OPTIFINE_WARNING::set).build());

        misc.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.misc.scarecrow_range"), MiscConfig.SCARECROW_RANGE.get())
                .setDefaultValue(16 * 3).setMin(0).setMax(Integer.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.misc.scarecrow_range.tooltip"))
                .setSaveConsumer(d -> MiscConfig.SCARECROW_RANGE.set(d)).build());

        misc.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.misc.use_new_maid_fairy_model"), MiscConfig.USE_NEW_MAID_FAIRY_MODEL.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.misc.use_new_maid_fairy_model.tooltip"))
                .setSaveConsumer(MiscConfig.USE_NEW_MAID_FAIRY_MODEL::set).build());

        misc.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.misc.invulnerable_particle_effect"), MiscConfig.INVULNERABLE_PARTICLE_EFFECT.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.misc.invulnerable_particle_effect.tooltip"))
                .setSaveConsumer(MiscConfig.INVULNERABLE_PARTICLE_EFFECT::set).build());
    }

    private static void vanillaConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory vanilla = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.vanilla"));

        vanilla.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.vanilla.replace_slime_model"), VanillaConfig.REPLACE_SLIME_MODEL.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.vanilla.replace_slime_model.tooltip"))
                .setSaveConsumer(VanillaConfig.REPLACE_SLIME_MODEL::set).build());

        vanilla.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.vanilla.replace_xp_texture"), VanillaConfig.REPLACE_XP_TEXTURE.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.vanilla.replace_xp_texture.tooltip"))
                .setSaveConsumer(VanillaConfig.REPLACE_XP_TEXTURE::set).build());

        vanilla.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.vanilla.replace_totem_texture"), VanillaConfig.REPLACE_TOTEM_TEXTURE.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.vanilla.replace_totem_texture.tooltip"))
                .setSaveConsumer(VanillaConfig.REPLACE_TOTEM_TEXTURE::set).build());

        vanilla.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.vanilla.replace_xp_bottle_texture"), VanillaConfig.REPLACE_XP_BOTTLE_TEXTURE.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.vanilla.replace_xp_bottle_texture.tooltip"))
                .setSaveConsumer(VanillaConfig.REPLACE_XP_BOTTLE_TEXTURE::set).build());
    }

    private static void renderConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory render = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.render"));

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_compass_tip"), RenderConfig.ENABLE_COMPASS_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_COMPASS_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_golden_apple_tip"), RenderConfig.ENABLE_GOLDEN_APPLE_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_GOLDEN_APPLE_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_potion_tip"), RenderConfig.ENABLE_POTION_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_POTION_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_milk_bucket_tip"), RenderConfig.ENABLE_MILK_BUCKET_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_MILK_BUCKET_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_glass_bottle_tip"), RenderConfig.ENABLE_GLASS_BOTTLE_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_GLASS_BOTTLE_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_name_tag_tip"), RenderConfig.ENABLE_NAME_TAG_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_NAME_TAG_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_lead_tip"), RenderConfig.ENABLE_LEAD_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_LEAD_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_saddle_tip"), RenderConfig.ENABLE_SADDLE_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_SADDLE_TIP::set).build());

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.render.enable_shears_tip"), RenderConfig.ENABLE_SHEARS_TIP.get())
                .setDefaultValue(true).setSaveConsumer(RenderConfig.ENABLE_SHEARS_TIP::set).build());
    }

    private static void wabConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory wab = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.wab"));

        if (!WabConfig.loaded()) {
            wab.addEntry(entryBuilder.startTextDescription(Component.translatable("config.touhou_little_maid.wab.unloaded")).build());
            return;
        }

        wab.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.enabled"), WabConfig.ENABLED.get())
                .setDefaultValue(WabConfig.ENABLED.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.enabled.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.ENABLED.set(v))).build());

        wab.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.wab.discovery_port"), WabConfig.DISCOVERY_PORT.get())
                .setDefaultValue(WabConfig.DISCOVERY_PORT.getDefault()).setMin(1).setMax(65535)
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.discovery_port.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.DISCOVERY_PORT.set(v))).build());

        wab.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.lan_discovery"), WabConfig.LAN_DISCOVERY.get())
                .setDefaultValue(WabConfig.LAN_DISCOVERY.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.lan_discovery.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.LAN_DISCOVERY.set(v))).build());

        wab.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.wab.beacon_interval"), WabConfig.BEACON_INTERVAL_SECONDS.get())
                .setDefaultValue(WabConfig.BEACON_INTERVAL_SECONDS.getDefault()).setMin(1).setMax(60)
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.beacon_interval.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.BEACON_INTERVAL_SECONDS.set(v))).build());

        wab.addEntry(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.wab.chat_range"), WabConfig.CHAT_RANGE.get(), 1, 64)
                .setDefaultValue(WabConfig.CHAT_RANGE.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.chat_range.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.CHAT_RANGE.set(v))).build());

        wab.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.show_hud"), WabConfig.SHOW_HUD.get())
                .setDefaultValue(WabConfig.SHOW_HUD.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.show_hud.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.SHOW_HUD.set(v))).build());

        wab.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.autonomy"), WabConfig.AUTONOMY_ENABLED.get())
                .setDefaultValue(WabConfig.AUTONOMY_ENABLED.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.autonomy.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.AUTONOMY_ENABLED.set(v))).build());

        wab.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.player_in_maid_bed"), WabConfig.PLAYER_IN_MAID_BED.get())
                .setDefaultValue(WabConfig.PLAYER_IN_MAID_BED.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.player_in_maid_bed.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.PLAYER_IN_MAID_BED.set(v))).build());

        wab.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.wab.maid_chat_cooldown"), WabConfig.MAID_CHAT_COOLDOWN.get())
                .setDefaultValue(WabConfig.MAID_CHAT_COOLDOWN.getDefault()).setMin(10).setMax(3600)
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.maid_chat_cooldown.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.MAID_CHAT_COOLDOWN.set(v))).build());

        wab.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.debug"), WabConfig.DEBUG.get())
                .setDefaultValue(WabConfig.DEBUG.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.debug.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.DEBUG.set(v))).build());

        ConfigCategory feeding = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.wab.feeding"));
        feeding.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.feeding_enabled"), WabConfig.FEEDING_ENABLED.get())
                .setDefaultValue(WabConfig.FEEDING_ENABLED.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.feeding_enabled.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.FEEDING_ENABLED.set(v))).build());
        feeding.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.owner_only_feeding"), WabConfig.OWNER_ONLY_FEEDING.get())
                .setDefaultValue(WabConfig.OWNER_ONLY_FEEDING.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.owner_only_feeding.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.OWNER_ONLY_FEEDING.set(v))).build());
        feeding.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.multiplayer_feeding"), WabConfig.MULTIPLAYER_FEEDING.get())
                .setDefaultValue(WabConfig.MULTIPLAYER_FEEDING.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.multiplayer_feeding.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.MULTIPLAYER_FEEDING.set(v))).build());
        feeding.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.wab.show_above_maid"), WabConfig.HEAD_INFO.get())
                .setDefaultValue(WabConfig.HEAD_INFO.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid.wab.show_above_maid.tooltip"))
                .setSaveConsumer(v -> saveWabConfig(() -> WabConfig.HEAD_INFO.set(v))).build());

        pluginConfig(entryBuilder, wab);
    }

    /**
     * WarmAsBefore 插件那一侧的设置：字段表由宿主按 plugin.json 现描现发，这里一个键名都不写死。
     * 插件以后加项、改默认值或换了中文名，游戏里这一栏跟着长出来，不需要再动模组——
     * 两头各存一份清单迟早漂移，那样「游戏里改的」和「WAB 里生效的」就不是同一件事。
     */
    private static void pluginConfig(ConfigEntryBuilder entryBuilder, ConfigCategory wab) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.wab.plugin"));
        builder.setExpanded(false);
        if (!WabRemoteConfig.present()) {
            builder.add(entryBuilder.startTextDescription(Component.translatable("config.touhou_little_maid.wab.plugin.empty")).build());
            wab.addEntry(builder.build());
            return;
        }
        for (WabRemoteConfig.Field field : WabRemoteConfig.fields()) {
            builder.add(pluginEntry(entryBuilder, field));
        }
        wab.addEntry(builder.build());
    }

    private static AbstractConfigListEntry<?> pluginEntry(ConfigEntryBuilder entryBuilder, WabRemoteConfig.Field field) {
        Component title = Component.literal(field.label());
        Component tooltip = Component.literal(field.help().isEmpty()
                ? "这一项由 WarmAsBefore 保存，改完在 WAB 那边立刻生效；以 WAB 实际生效的值为准。"
                : field.help() + "（改完在 WAB 那边立刻生效，以实际生效值为准）");
        if (field.locked()) {
            // 只读一行：Cloth 的 setEditable(false) 只把字画灰、点击照样写值，那样看着像锁死其实还能改，比不灰更骗人。
            return entryBuilder.startTextDescription(Component.literal(field.label()).withStyle(ChatFormatting.GRAY))
                    .setTooltip(tooltip).build();
        }
        switch (field.type()) {
            case "switch": {
                boolean def = "true".equalsIgnoreCase(field.defaultValue());
                return entryBuilder.startBooleanToggle(title, field.on())
                        .setDefaultValue(def).setTooltip(tooltip)
                        .setSaveConsumer(v -> WabRemoteConfig.save(field.key(), String.valueOf(v))).build();
            }
            case "number": {
                int def = parseInt(field.defaultValue(), 0);
                return entryBuilder.startIntField(title, parseInt(field.value(), def))
                        .setDefaultValue(def).setTooltip(tooltip)
                        .setSaveConsumer(v -> WabRemoteConfig.save(field.key(), String.valueOf(v))).build();
            }
            default:
                return entryBuilder.startStrField(title, field.value())
                        .setDefaultValue(field.defaultValue()).setTooltip(tooltip)
                        .setSaveConsumer(v -> WabRemoteConfig.save(field.key(), v == null ? "" : v)).build();
        }
    }

    private static int parseInt(String raw, int fallback) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    /** Cloth 的开关只改内存里的那份配置，得自己落盘；女仆 AI 设置页也是这么写的。 */
    private static void saveWabConfig(Runnable apply) {
        apply.run();
        WabConfig.SPEC.save();
    }

    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> getConfigBuilder().setParentScreen(parent).build()));
    }
}
