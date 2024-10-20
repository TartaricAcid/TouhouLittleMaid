package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;


public class FavorabilityAdvancement {
    public static void generate(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = make(InitItems.BOOKSHELF.get(), "maid_sit_joy")
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_SIT_JOY))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("favorability/maid_sit_joy"), existingFileHelper);

        generateFavorability(saver, existingFileHelper, root);

        generateJoy(saver, existingFileHelper, root);
    }

    private static void generateJoy(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper, Advancement root) {
        Advancement joy = make(InitItems.PICNIC_BASKET.get(), "maid_picnic_eat").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_PICNIC_EAT))
                .save(saver, id("favorability/maid_picnic_eat"), existingFileHelper);

        makeGoal(InitItems.GOMOKU.get(), "win_gomoku").parent(joy)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.WIN_GOMOKU))
                .save(saver, id("favorability/win_gomoku"), existingFileHelper);

        makeGoal(InitItems.CCHESS.get(), "win_cchess").parent(joy)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.WIN_CCHESS))
                .save(saver, id("favorability/win_cchess"), existingFileHelper);

        make(InitItems.MAID_BED.get(), "maid_sleep").parent(joy)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_SLEEP))
                .save(saver, id("favorability/maid_sleep"), existingFileHelper);
    }

    private static void generateFavorability(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper, Advancement root) {
        Advancement increased = make(InitItems.FAVORABILITY_TOOL_ADD.get(), "favorability_increased").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.FAVORABILITY_INCREASED))
                .save(saver, id("favorability/favorability_increased"), existingFileHelper);

        makeGoal(InitItems.FAVORABILITY_TOOL_FULL.get(), "favorability_increased_max").parent(increased)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.FAVORABILITY_INCREASED_MAX))
                .save(saver, id("favorability/favorability_increased_max"), existingFileHelper);
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static Advancement.Builder makeGoal(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.GOAL, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
