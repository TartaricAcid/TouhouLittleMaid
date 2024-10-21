package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.datagen.LanguageGenerator;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;


public class ChallengeAdvancement {
    public static void generate(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = make(Items.IRON_HELMET, "any_equipment")
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.ANY_EQUIPMENT))
                .save(saver, id("challenge/any_equipment"), existingFileHelper);

        generateProtect(root, saver, existingFileHelper);

        generateKill(root, saver, existingFileHelper);
    }

    private static void generateProtect(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement protect = make(Items.ENCHANTED_GOLDEN_APPLE, "eat_enchanted_golden_apple").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.EAT_ENCHANTED_GOLDEN_APPLE))
                .save(saver, id("challenge/eat_enchanted_golden_apple"), existingFileHelper);

        makeChallenge(InitItems.ALL_NETHERITE_EQUIPMENT.get(), "all_netherite_equipment").parent(protect)
                .rewards(AdvancementRewards.Builder.experience(50))
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.ALL_NETHERITE_EQUIPMENT))
                .save(saver, id("challenge/all_netherite_equipment"), existingFileHelper);

        ItemStack stack = ItemEntityPlaceholder.setRecipeId(new ItemStack(InitItems.ENTITY_PLACEHOLDER.get()), id("altar/spawn_lightning_bolt"));
        Advancement lightningBolt = make(stack, "lightning_bolt").parent(protect)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.LIGHTNING_BOLT))
                .save(saver, id("challenge/lightning_bolt"), existingFileHelper);

        makeGoal(InitItems.MAID_100_HEALTHY.get(), "maid_100_healthy").parent(lightningBolt)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_100_HEALTHY))
                .save(saver, id("challenge/maid_100_healthy"), existingFileHelper);
    }

    private static void generateKill(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement kill = makeGoal(Items.DIAMOND_SWORD, "kill_100").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.KILL_100))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("challenge/kill_100"), existingFileHelper);

        makeChallenge(InitItems.KILL_SLIME_300.get(), "kill_slime_300").parent(kill)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.KILL_SLIME_300))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("challenge/kill_slime_300"), existingFileHelper);

        Advancement wither = makeChallenge(InitItems.KILL_WITHER.get(), "kill_wither").parent(kill)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.KILL_WITHER))
                .save(saver, id("challenge/kill_wither"), existingFileHelper);

        makeChallenge(InitItems.KILL_DRAGON.get(), "kill_dragon").parent(wither)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.KILL_DRAGON))
                .save(saver, id("challenge/kill_dragon"), existingFileHelper);
    }

    private static void generateOther(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        makeGoal(Items.ENCHANTED_BOOK, "maid_fishing_enchanted_book").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FISHING_ENCHANTED_BOOK))
                .save(saver, id("challenge/maid_fishing_enchanted_book"), existingFileHelper);

        makeGoal(Items.CAKE, "tamed_maid_in_pillager_outpost").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.TAMED_MAID_FROM_STRUCTURE))
                .save(saver, id("challenge/tamed_maid_in_pillager_outpost"), existingFileHelper);
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.description", key));

        LanguageGenerator.addLanguage(title);
        LanguageGenerator.addLanguage(desc);

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static Advancement.Builder make(ItemStack item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.description", key));

        LanguageGenerator.addLanguage(title);
        LanguageGenerator.addLanguage(desc);

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static Advancement.Builder makeGoal(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.description", key));

        LanguageGenerator.addLanguage(title);
        LanguageGenerator.addLanguage(desc);

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.GOAL, true, true, false);
    }

    private static Advancement.Builder makeChallenge(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.challenge.%s.description", key));

        LanguageGenerator.addLanguage(title);
        LanguageGenerator.addLanguage(desc);

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.CHALLENGE, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
