package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.altar.AltarCraftTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.datagen.LootTableGenerator;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;


public class BaseAdvancement {
    public static void generate(Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        AdvancementHolder root = make(InitItems.HAKUREI_GOHEI.get(), "craft_gohei")
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("craft_hakurei_gohei", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("hakurei_gohei")))
                .addCriterion("craft_sanae_gohei", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("sanae_gohei")))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("base/craft_gohei"), existingFileHelper);

        generateAltar(saver, existingFileHelper, root);

        generateMaid(saver, existingFileHelper, root);

        generateChair(saver, existingFileHelper, root);
    }

    private static void generateChair(Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper, AdvancementHolder root) {
        AdvancementHolder chair = make(InitItems.CHAIR.get(), "craft_chair").parent(root)
                .addCriterion("craft_chair", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("chair")))
                .save(saver, id("base/craft_chair"), existingFileHelper);

        make(InitItems.CHANGE_CHAIR_MODEL.get(), "change_chair_model").parent(chair)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHANGE_CHAIR_MODEL))
                .save(saver, id("base/change_chair_model"), existingFileHelper);
    }

    private static void generateMaid(Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper, AdvancementHolder root) {
        ItemStack stack = ItemEntityPlaceholder.setRecipeId(new ItemStack(InitItems.ENTITY_PLACEHOLDER.get()), "spawn_box");
        AdvancementHolder spawnMaid = make(stack, "spawn_maid").parent(root)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar_recipe/spawn_box")))
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.CAKE))
                .save(saver, id("base/spawn_maid"), existingFileHelper);

        makeGoal(Items.CAKE, "tamed_maid").parent(spawnMaid)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.TAMED_MAID))
                .save(saver, id("base/tamed_maid"), existingFileHelper);

        make(InitItems.CHANGE_MAID_MODEL.get(), "change_maid_model").parent(spawnMaid)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHANGE_MAID_MODEL))
                .save(saver, id("base/change_maid_model"), existingFileHelper);

        make(Items.JUKEBOX, "change_maid_sound").parent(spawnMaid)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHANGE_MAID_SOUND))
                .save(saver, id("base/change_maid_sound"), existingFileHelper);
    }

    private static void generateAltar(Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper, AdvancementHolder root) {
        AdvancementHolder altar = make(Items.RED_WOOL, "build_altar").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.BUILD_ALTAR))
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.POWER_POINT))
                .save(saver, id("base/build_altar"), existingFileHelper);

        EntityPredicate.Builder predicate = EntityPredicate.Builder.entity().of(InitEntities.FAIRY.get());
        make(InitItems.FAIRY_SPAWN_EGG.get(), "kill_maid_fairy").parent(altar)
                .addCriterion("killed_entity", KilledTrigger.TriggerInstance.playerKilledEntity(predicate))
                .save(saver, id("base/kill_maid_fairy"), existingFileHelper);

        make(InitItems.POWER_POINT.get(), "pickup_power_point").parent(altar)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.PICKUP_POWER_POINT))
                .save(saver, id("base/pickup_power_point"), existingFileHelper);
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                AdvancementType.TASK, true, true, false);
    }

    private static Advancement.Builder make(ItemStack item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                AdvancementType.TASK, true, true, false);
    }

    private static Advancement.Builder makeGoal(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                AdvancementType.GOAL, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, id);
    }
}
