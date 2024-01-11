package com.github.tartaricacid.touhoulittlemaid.compat.cloth;


import com.github.tartaricacid.touhoulittlemaid.config.subconfig.ChairConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.world.biome.Biome.Category.*;


public class MenuIntegration {
    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder root = ConfigBuilder.create().setTitle(new StringTextComponent("Touhou Little Maid"));
        root.setGlobalized(true);
        root.setGlobalizedExpanded(false);
        ConfigEntryBuilder entryBuilder = root.entryBuilder();
        maidConfig(root, entryBuilder);
        chairConfig(root, entryBuilder);
        miscConfig(root, entryBuilder);
        vanillaConfig(root, entryBuilder);
        return root;
    }


    @SuppressWarnings("all")
    private static void maidConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory maid = root.getOrCreateCategory(new TranslationTextComponent("entity.touhou_little_maid.maid"));
        maid.addEntry(entryBuilder.startDropdownMenu(new TranslationTextComponent("config.touhou_little_maid.maid.maid_tamed_item.name"),
                        DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(ForgeRegistries.ITEMS.getValue(new ResourceLocation(MaidConfig.MAID_TAMED_ITEM.get()))),
                        DropdownMenuBuilder.CellCreatorBuilder.ofItemObject())
                .setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new)))
                .setDefaultValue(Items.CAKE).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_tamed_item.desc"))
                .setSaveConsumer(s -> MaidConfig.MAID_TAMED_ITEM.set(ForgeRegistries.ITEMS.getKey(s).toString())).build());


        maid.addEntry(entryBuilder.startDropdownMenu(new TranslationTextComponent("config.touhou_little_maid.maid.maid_temptation_item.name"),
                        DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(ForgeRegistries.ITEMS.getValue(new ResourceLocation(MaidConfig.MAID_TEMPTATION_ITEM.get()))),
                        DropdownMenuBuilder.CellCreatorBuilder.ofItemObject())
                .setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new)))
                .setDefaultValue(Items.CAKE).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_temptation_item.desc"))
                .setSaveConsumer(s -> MaidConfig.MAID_TEMPTATION_ITEM.set(ForgeRegistries.ITEMS.getKey(s).toString())).build());


        maid.addEntry(entryBuilder.startDropdownMenu(new TranslationTextComponent("config.touhou_little_maid.maid.maid_ntr_item.name"),
                        DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(ForgeRegistries.ITEMS.getValue(new ResourceLocation(MaidConfig.MAID_NTR_ITEM.get()))),
                        DropdownMenuBuilder.CellCreatorBuilder.ofItemObject())
                .setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString)).collect(Collectors.toCollection(LinkedHashSet::new)))
                .setDefaultValue(Items.STRUCTURE_VOID).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_ntr_item.desc"))
                .setSaveConsumer(s -> MaidConfig.MAID_NTR_ITEM.set(ForgeRegistries.ITEMS.getKey(s).toString())).build());

        maid.addEntry(entryBuilder.startIntSlider(new TranslationTextComponent("config.touhou_little_maid.maid.maid_work_range.name"), MaidConfig.MAID_WORK_RANGE.get(), 3, 64)
                .setDefaultValue(12).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_work_range.desc"))
                .setSaveConsumer(i -> MaidConfig.MAID_WORK_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(new TranslationTextComponent("config.touhou_little_maid.maid.maid_idle_range.name"), MaidConfig.MAID_IDLE_RANGE.get(), 3, 32)
                .setDefaultValue(6).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_idle_range.desc"))
                .setSaveConsumer(i -> MaidConfig.MAID_IDLE_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(new TranslationTextComponent("config.touhou_little_maid.maid.maid_sleep_range.name"), MaidConfig.MAID_SLEEP_RANGE.get(), 3, 32)
                .setDefaultValue(6).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_sleep_range.desc"))
                .setSaveConsumer(i -> MaidConfig.MAID_SLEEP_RANGE.set(i)).build());

        maid.addEntry(entryBuilder.startIntSlider(new TranslationTextComponent("config.touhou_little_maid.maid.maid_non_home_range.name"), MaidConfig.MAID_NON_HOME_RANGE.get(), 3, 32)
                .setDefaultValue(8).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_non_home_range.desc"))
                .setSaveConsumer(i -> MaidConfig.MAID_NON_HOME_RANGE.set(i)).build());


        maid.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.maid.maid_change_model.name"), MaidConfig.MAID_CHANGE_MODEL.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_change_model.desc"))
                .setSaveConsumer(MaidConfig.MAID_CHANGE_MODEL::set).build());


        maid.addEntry(entryBuilder.startIntField(new TranslationTextComponent("config.touhou_little_maid.maid.owner_max_maid_num.name"), MaidConfig.OWNER_MAX_MAID_NUM.get())
                .setDefaultValue(Integer.MAX_VALUE).setMin(0).setMax(Integer.MAX_VALUE)
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.owner_max_maid_num.desc"))
                .setSaveConsumer(i -> MaidConfig.OWNER_MAX_MAID_NUM.set(i)).build());


        maid.addEntry(entryBuilder.startStrList(new TranslationTextComponent("config.touhou_little_maid.maid.maid_ranged_attack_ignore.name"), MaidConfig.MAID_RANGED_ATTACK_IGNORE.get())
                .setDefaultValue(Lists.newArrayList())
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.maid.maid_ranged_attack_ignore.desc"))
                .setSaveConsumer(l -> MaidConfig.MAID_RANGED_ATTACK_IGNORE.set(l)).build());
    }


    private static void chairConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory chair = root.getOrCreateCategory(new TranslationTextComponent("entity.touhou_little_maid.chair"));
        chair.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.chair.chair_change_model.name"), ChairConfig.CHAIR_CHANGE_MODEL.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.chair.chair_change_model.desc"))
                .setSaveConsumer(ChairConfig.CHAIR_CHANGE_MODEL::set).build());


        chair.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.chair.chair_can_destroyed_by_anyone.name"), ChairConfig.CHAIR_CAN_DESTROYED_BY_ANYONE.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.chair.chair_can_destroyed_by_anyone.desc"))
                .setSaveConsumer(ChairConfig.CHAIR_CAN_DESTROYED_BY_ANYONE::set).build());
    }


    @SuppressWarnings("all")
    private static void miscConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory misc = root.getOrCreateCategory(new TranslationTextComponent("config.touhou_little_maid.misc"));
        misc.addEntry(entryBuilder.startDoubleField(new TranslationTextComponent("config.touhou_little_maid.misc.maid_fairy_power_point.name"), MiscConfig.MAID_FAIRY_POWER_POINT.get())
                .setDefaultValue(0.16).setMin(0).setMax(5)
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.maid_fairy_power_point.desc"))
                .setSaveConsumer(d -> MiscConfig.MAID_FAIRY_POWER_POINT.set(d)).build());


        misc.addEntry(entryBuilder.startIntField(new TranslationTextComponent("config.touhou_little_maid.misc.maid_fairy_spawn_probability.name"), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get())
                .setDefaultValue(70).setMin(0).setMax(Integer.MAX_VALUE)
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.maid_fairy_spawn_probability.desc"))
                .setSaveConsumer(d -> MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.set(d)).build());

        misc.addEntry(entryBuilder.startDoubleField(new TranslationTextComponent("config.touhou_little_maid.misc.player_death_loss_power_point.name"), MiscConfig.PLAYER_DEATH_LOSS_POWER_POINT.get())
                .setDefaultValue(1.0).setMin(0).setMax(5)
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.player_death_loss_power_point.desc"))
                .setSaveConsumer(d -> MiscConfig.PLAYER_DEATH_LOSS_POWER_POINT.set(d)).build());

        misc.addEntry(entryBuilder.startStrList(new TranslationTextComponent("config.touhou_little_maid.misc.maid_fairy_blacklist_biome.name"), (List<String>) MiscConfig.MAID_FAIRY_BLACKLIST_BIOME.get())
                .setDefaultValue(Lists.newArrayList(NETHER.getName(), THEEND.getName(), NONE.getName(), MUSHROOM.getName()))
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.maid_fairy_blacklist_biome.desc"))
                .setSaveConsumer(l -> MiscConfig.MAID_FAIRY_BLACKLIST_BIOME.set(l)).build());


        misc.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.misc.give_smart_slab.name"), MiscConfig.GIVE_SMART_SLAB.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.give_smart_slab.desc"))
                .setSaveConsumer(MiscConfig.GIVE_SMART_SLAB::set).build());


        misc.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.misc.give_patchouli_book.name"), MiscConfig.GIVE_PATCHOULI_BOOK.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.give_patchouli_book.desc"))
                .setSaveConsumer(MiscConfig.GIVE_PATCHOULI_BOOK::set).build());


        misc.addEntry(entryBuilder.startDoubleField(new TranslationTextComponent("config.touhou_little_maid.misc.shrine_lamp_effect_cost.name"), MiscConfig.SHRINE_LAMP_EFFECT_COST.get())
                .setDefaultValue(0.9).setMin(0).setMax(Double.MAX_VALUE)
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.shrine_lamp_effect_cost.desc"))
                .setSaveConsumer(d -> MiscConfig.SHRINE_LAMP_EFFECT_COST.set(d)).build());


        misc.addEntry(entryBuilder.startDoubleField(new TranslationTextComponent("config.touhou_little_maid.misc.shrine_lamp_max_storage.name"), MiscConfig.SHRINE_LAMP_MAX_STORAGE.get())
                .setDefaultValue(100).setMin(0).setMax(Double.MAX_VALUE)
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.shrine_lamp_max_storage.desc"))
                .setSaveConsumer(d -> MiscConfig.SHRINE_LAMP_MAX_STORAGE.set(d)).build());


        misc.addEntry(entryBuilder.startIntField(new TranslationTextComponent("config.touhou_little_maid.misc.shrine_lamp_max_range.name"), MiscConfig.SHRINE_LAMP_MAX_RANGE.get())
                .setDefaultValue(6).setMin(0).setMax(Integer.MAX_VALUE)
                .setTooltip(new TranslationTextComponent("config.touhou_little_maid.misc.shrine_lamp_max_range.desc"))
                .setSaveConsumer(d -> MiscConfig.SHRINE_LAMP_MAX_RANGE.set(d)).build());
    }

    private static void vanillaConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory vanilla = root.getOrCreateCategory(new TranslationTextComponent("config.touhou_little_maid.vanilla"));

        vanilla.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_slime_model.name"), VanillaConfig.REPLACE_SLIME_MODEL.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_slime_model.desc"))
                .setSaveConsumer(VanillaConfig.REPLACE_SLIME_MODEL::set).build());

        vanilla.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_xp_texture.name"), VanillaConfig.REPLACE_XP_TEXTURE.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_xp_texture.desc"))
                .setSaveConsumer(VanillaConfig.REPLACE_XP_TEXTURE::set).build());

        vanilla.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_totem_texture.name"), VanillaConfig.REPLACE_TOTEM_TEXTURE.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_totem_texture.desc"))
                .setSaveConsumer(VanillaConfig.REPLACE_TOTEM_TEXTURE::set).build());

        vanilla.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_xp_bottle_texture.name"), VanillaConfig.REPLACE_XP_BOTTLE_TEXTURE.get())
                .setDefaultValue(true).setTooltip(new TranslationTextComponent("config.touhou_little_maid.vanilla.replace_xp_bottle_texture.desc"))
                .setSaveConsumer(VanillaConfig.REPLACE_XP_BOTTLE_TEXTURE::set).build());
    }


    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () ->
                (client, parent) -> getConfigBuilder().setParentScreen(parent).build());
    }
}