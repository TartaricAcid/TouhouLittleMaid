package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.altar.AltarCraftTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PickedUpItemTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;


public class MaidBaseAdvancement {
    public static void generate(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = make(InitItems.HAKUREI_GOHEI.get(), "switch_task")
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SWITCH_TASK))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("maid_base/switch_task"), existingFileHelper);

        generateTask(root, saver, existingFileHelper);

        make(Items.SADDLE, "pickup_maid").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.PICKUP_MAID))
                .save(saver, id("maid_base/pickup_maid"), existingFileHelper);

        generateBauble(root, saver, existingFileHelper);

        generatePhoto(root, saver, existingFileHelper);

        generateReborn(root, saver, existingFileHelper);
    }

    private static void generateTask(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement taskRoot = make(InitItems.HAKUREI_GOHEI.get(), "switch_schedule").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SWITCH_SCHEDULE))
                .save(saver, id("maid_base/switch_schedule"), existingFileHelper);

        make(InitItems.HAKUREI_GOHEI.get(), "maid_backpack").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_BACKPACK))
                .save(saver, id("maid_base/maid_backpack"), existingFileHelper);

        make(InitItems.HAKUREI_GOHEI.get(), "maid_farm").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FARM))
                .save(saver, id("maid_base/maid_farm"), existingFileHelper);

        make(InitItems.HAKUREI_GOHEI.get(), "maid_feed_animal").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FEED_ANIMAL))
                .save(saver, id("maid_base/maid_feed_animal"), existingFileHelper);

        make(InitItems.HAKUREI_GOHEI.get(), "maid_feed_player").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FEED_PLAYER))
                .save(saver, id("maid_base/maid_feed_player"), existingFileHelper);

        make(InitItems.HAKUREI_GOHEI.get(), "maid_kill_mob").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_KILL_MOB))
                .save(saver, id("maid_base/maid_kill_mob"), existingFileHelper);

        make(InitItems.HAKUREI_GOHEI.get(), "maid_fishing").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FISHING))
                .save(saver, id("maid_base/maid_fishing"), existingFileHelper);
    }

    private static void generateBauble(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement baubleRoot = make(InitItems.FIRE_PROTECT_BAUBLE.get(), "use_protect_bauble").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_PROTECT_BAUBLE))
                .save(saver, id("maid_base/use_protect_bauble"), existingFileHelper);

        make(InitItems.NIMBLE_FABRIC.get(), "use_nimble_fabric").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_NIMBLE_FABRIC))
                .save(saver, id("maid_base/use_nimble_fabric"), existingFileHelper);

        make(InitItems.ITEM_MAGNET_BAUBLE.get(), "use_item_magnet_bauble").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_ITEM_MAGNET_BAUBLE))
                .save(saver, id("maid_base/use_item_magnet_bauble"), existingFileHelper);

        make(InitItems.ULTRAMARINE_ORB_ELIXIR.get(), "use_undead_bauble").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_UNDEAD_BAUBLE))
                .save(saver, id("maid_base/use_undead_bauble"), existingFileHelper);

        make(InitItems.WIRELESS_IO.get(), "use_wireless_io").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_WIRELESS_IO))
                .save(saver, id("maid_base/use_wireless_io"), existingFileHelper);
    }

    private static void generatePhoto(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement photoRoot = make(InitItems.CAMERA.get(), "photo_maid").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.PHOTO_MAID))
                .save(saver, id("maid_base/photo_maid"), existingFileHelper);

        make(InitItems.CHISEL.get(), "chisel_statue").parent(photoRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHISEL_STATUE))
                .save(saver, id("maid_base/chisel_statue"), existingFileHelper);

        make(InitItems.CHISEL.get(), "pickup_garage_kit").parent(photoRoot)
                .addCriterion("pickup_item", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(
                        ContextAwarePredicate.ANY,
                        ItemPredicate.Builder.item().of(InitItems.GARAGE_KIT.get()).build(),
                        ContextAwarePredicate.ANY))
                .save(saver, id("maid_base/pickup_garage_kit"), existingFileHelper);
    }

    private static void generateReborn(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement rebornRoot = make(InitItems.SHRINE.get(), "reborn_maid").parent(root)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/reborn_maid")))
                .save(saver, id("maid_base/reborn_maid"), existingFileHelper);

        make(InitItems.SHRINE.get(), "shrine_reborn_maid").parent(rebornRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SHRINE_REBORN_MAID))
                .save(saver, id("maid_base/shrine_reborn_maid"), existingFileHelper);
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
