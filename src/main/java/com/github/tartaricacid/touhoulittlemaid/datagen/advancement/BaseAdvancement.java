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

        make(Items.RED_WOOL, "build_altar").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.BUILD_ALTAR))
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.POWER_POINT))
                .save(saver, id("base/build_altar"), existingFileHelper);

        EntityPredicate.Builder predicate = EntityPredicate.Builder.entity().of(InitEntities.FAIRY.get());
        make(InitItems.POWER_POINT.get(), "kill_maid_fairy").parent(root)
                .addCriterion("killed_entity", KilledTrigger.TriggerInstance.playerKilledEntity(predicate))
                .save(saver, id("base/kill_maid_fairy"), existingFileHelper);

        make(Items.CAKE, "spawn_maid").parent(root)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/spawn_box")))
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.CAKE))
                .save(saver, id("base/spawn_maid"), existingFileHelper);

        make(Items.CAKE, "tamed_maid").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.TAMED_MAID))
                .save(saver, id("base/tamed_maid"), existingFileHelper);
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
