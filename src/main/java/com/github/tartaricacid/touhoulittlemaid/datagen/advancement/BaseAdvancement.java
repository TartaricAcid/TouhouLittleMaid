package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.altar.AltarCraftTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.datagen.LootTableGenerator;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;


public class BaseAdvancement {
    public static void generate(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = make(InitItems.HAKUREI_GOHEI.get(), "craft_gohei")
                .requirements(RequirementsStrategy.OR)
                .addCriterion("craft_hakurei_gohei", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("hakurei_gohei")))
                .addCriterion("craft_sanae_gohei", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("sanae_gohei")))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("base/craft_gohei"), existingFileHelper);

        generateAltar(saver, existingFileHelper, root);

        generateMaid(saver, existingFileHelper, root);

        generateChair(saver, existingFileHelper, root);
    }

    private static void generateChair(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper, Advancement root) {
        Advancement chair = make(InitItems.CHAIR.get(), "craft_chair").parent(root)
                .addCriterion("craft_chair", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("chair")))
                .save(saver, id("base/craft_chair"), existingFileHelper);

        make(InitItems.CHAIR.get(), "change_chair_model").parent(chair)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHANGE_CHAIR_MODEL))
                .save(saver, id("base/change_chair_model"), existingFileHelper);
    }

    private static void generateMaid(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper, Advancement root) {
        Advancement spawnMaid = make(Items.CAKE, "spawn_maid").parent(root)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/spawn_box")))
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.CAKE))
                .save(saver, id("base/spawn_maid"), existingFileHelper);

        makeGoal(Items.CAKE, "tamed_maid").parent(spawnMaid)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.TAMED_MAID))
                .save(saver, id("base/tamed_maid"), existingFileHelper);

        make(Items.CAKE, "change_maid_model").parent(spawnMaid)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHANGE_MAID_MODEL))
                .save(saver, id("base/change_maid_model"), existingFileHelper);

        make(Items.CAKE, "change_maid_sound").parent(spawnMaid)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHANGE_MAID_SOUND))
                .save(saver, id("base/change_maid_sound"), existingFileHelper);
    }

    private static void generateAltar(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper, Advancement root) {
        Advancement altar = make(Items.RED_WOOL, "build_altar").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.BUILD_ALTAR))
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.POWER_POINT))
                .save(saver, id("base/build_altar"), existingFileHelper);

        EntityPredicate.Builder predicate = EntityPredicate.Builder.entity().of(InitEntities.FAIRY.get());
        make(InitItems.POWER_POINT.get(), "kill_maid_fairy").parent(altar)
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
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static Advancement.Builder makeGoal(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.GOAL, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
