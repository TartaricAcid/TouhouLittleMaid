package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.altar.AltarCraftTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.altar.BuildAltarTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEvent;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class AdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = altar(InitItems.HAKUREI_GOHEI.get(), "gohei")
                .requirements(RequirementsStrategy.OR)
                .addCriterion("craft_hakurei_gohei", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("hakurei_gohei")))
                .addCriterion("craft_sanae_gohei", RecipeCraftedTrigger.TriggerInstance.craftedItem(id("sanae_gohei")))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("altar/craft_gohei"), existingFileHelper);

        Advancement buildAltar = altar(Items.RED_WOOL, "build_altar").parent(root)
                .addCriterion("build_altar", new BuildAltarTrigger.Instance())
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.POWER_POINT))
                .save(saver, id("altar/build_altar"), existingFileHelper);

        Advancement killMaidFairy = altar(InitItems.POWER_POINT.get(), "kill_maid_fairy").parent(buildAltar)
                .addCriterion("killed_entity", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(InitEntities.FAIRY.get())))
                .save(saver, id("altar/kill_maid_fairy"), existingFileHelper);

        Advancement spawnMaid = altar(Items.CAKE, "spawn_maid").parent(killMaidFairy)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/spawn_box")))
                .rewards(AdvancementRewards.Builder.loot(LootTableGenerator.CAKE))
                .save(saver, id("altar/spawn_maid"), existingFileHelper);

        Advancement tamedMaid = maid(Items.CAKE, "tamed_maid").parent(spawnMaid)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.TAMED_MAID))
                .save(saver, id("maid/tamed_maid"), existingFileHelper);

        Advancement craftCamera = altar(InitItems.CAMERA.get(), "craft_camera").parent(tamedMaid)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/craft_camera")))
                .save(saver, id("altar/craft_camera"), existingFileHelper);

        Advancement photoMaid = maid(InitItems.PHOTO.get(), "photo_maid").parent(craftCamera)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.PHOTO_MAID))
                .save(saver, id("maid/photo_maid"), existingFileHelper);

        Advancement craftChisel = altar(InitItems.CHISEL.get(), "craft_chisel").parent(photoMaid)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/craft_chisel")))
                .save(saver, id("altar/craft_chisel"), existingFileHelper);

        Advancement chiselStatue = maid(InitItems.CHISEL.get(), "chisel_statue").parent(craftChisel)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.CHISEL_STATUE))
                .save(saver, id("maid/chisel_statue"), existingFileHelper);

        maid(InitItems.CHISEL.get(), "pickup_garage_kit").parent(chiselStatue)
                .addCriterion("pickup_item", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(
                        ContextAwarePredicate.ANY,
                        ItemPredicate.Builder.item().of(InitItems.GARAGE_KIT.get()).build(),
                        ContextAwarePredicate.ANY))
                .save(saver, id("maid/pickup_garage_kit"), existingFileHelper);

        Advancement craftGomoku = altar(InitItems.GOMOKU.get(), "craft_gomoku").parent(tamedMaid)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/craft_gomoku")))
                .save(saver, id("altar/craft_gomoku"), existingFileHelper);

        maid(InitItems.GOMOKU.get(), "win_gomoku").parent(craftGomoku)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.WIN_GOMOKU))
                .save(saver, id("maid/win_gomoku"), existingFileHelper);

        Advancement spawnLightningBolt = altar(InitItems.GOMOKU.get(), "spawn_lightning_bolt").parent(tamedMaid)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/spawn_lightning_bolt")))
                .save(saver, id("altar/spawn_lightning_bolt"), existingFileHelper);

        maid(InitItems.GOMOKU.get(), "lightning_bolt").parent(spawnLightningBolt)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.LIGHTNING_BOLT))
                .save(saver, id("maid/lightning_bolt"), existingFileHelper);

        Advancement craftMaidBed = altar(InitItems.MAID_BED.get(), "craft_maid_bed").parent(tamedMaid)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/craft_maid_bed")))
                .save(saver, id("altar/craft_maid_bed"), existingFileHelper);

        maid(InitItems.MAID_BED.get(), "maid_sleep").parent(craftMaidBed)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.MAID_SLEEP))
                .save(saver, id("maid/maid_sleep"), existingFileHelper);

        Advancement craftPicnicBasket = altar(InitItems.PICNIC_BASKET.get(), "craft_picnic_basket").parent(tamedMaid)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/craft_picnic_basket")))
                .save(saver, id("altar/craft_picnic_basket"), existingFileHelper);

        Advancement maidPicnicEat = maid(InitItems.PICNIC_BASKET.get(), "maid_picnic_eat").parent(craftPicnicBasket)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.MAID_PICNIC_EAT))
                .save(saver, id("maid/maid_picnic_eat"), existingFileHelper);

        maid(InitItems.FAVORABILITY_TOOL_ADD.get(), "maid_favorability_increased").parent(maidPicnicEat)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.MAID_FAVORABILITY_INCREASED))
                .save(saver, id("maid/maid_favorability_increased"), existingFileHelper);

        Advancement maidDeath = maid(InitItems.SHRINE.get(), "maid_death").parent(tamedMaid)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.MAID_DEATH))
                .save(saver, id("maid/maid_death"), existingFileHelper);

        Advancement rebornMaid = altar(InitItems.SHRINE.get(), "reborn_maid").parent(maidDeath)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/reborn_maid")))
                .save(saver, id("altar/reborn_maid"), existingFileHelper);

        maid(InitItems.SHRINE.get(), "shrine_reborn_maid").parent(rebornMaid)
                .addCriterion("maid_event", new MaidEventTrigger.Instance(MaidEvent.SHRINE_REBORN_MAID))
                .save(saver, id("maid/shrine_reborn_maid"), existingFileHelper);
    }

    public static Advancement.Builder altar(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.altar.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.altar.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    public static Advancement.Builder maid(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    public static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
