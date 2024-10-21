package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.altar.AltarCraftTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.datagen.LanguageGenerator;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PickedUpItemTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;


public class MaidBaseAdvancement {
    public static void generate(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = make(InitItems.HAKUREI_GOHEI.get(), "switch_task")
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SWITCH_TASK))
                .save(saver, id("maid_base/switch_task"), existingFileHelper);

        generateTask(root, saver, existingFileHelper);

        generateOther(saver, existingFileHelper, root);

        generateBauble(root, saver, existingFileHelper);

        generatePhoto(root, saver, existingFileHelper);

        generateFind(saver, existingFileHelper, root);

        generateReborn(root, saver, existingFileHelper);
    }

    private static void generateFind(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper, Advancement root) {
        Advancement base = make(InitItems.SERVANT_BELL.get(), "use_servant_bell").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_SERVANT_BELL))
                .save(saver, id("maid_base/use_servant_bell"), existingFileHelper);

        make(InitItems.TRUMPET.get(), "use_trumpet").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_TRUMPET))
                .save(saver, id("maid_base/use_trumpet"), existingFileHelper);

        Advancement redFoxScroll = make(InitItems.RED_FOX_SCROLL.get(), "use_red_fox_scroll").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_RED_FOX_SCROLL))
                .save(saver, id("maid_base/use_red_fox_scroll"), existingFileHelper);

        make(InitItems.WHITE_FOX_SCROLL.get(), "use_white_fox_scroll").parent(redFoxScroll)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_WHITE_FOX_SCROLL))
                .save(saver, id("maid_base/use_white_fox_scroll"), existingFileHelper);
    }

    private static void generateOther(Consumer<Advancement> saver, ExistingFileHelper existingFileHelper, Advancement root) {
        Advancement base = make(Items.SADDLE, "pickup_maid").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.PICKUP_MAID))
                .save(saver, id("maid_base/pickup_maid"), existingFileHelper);

        make(Items.EXPERIENCE_BOTTLE, "take_maid_xp").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.TAKE_MAID_XP))
                .save(saver, id("maid_base/take_maid_xp"), existingFileHelper);

        make(Items.MILK_BUCKET, "clear_maid_effects").parent(base)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CLEAR_MAID_EFFECTS))
                .save(saver, id("maid_base/clear_maid_effects"), existingFileHelper);
    }

    private static void generateTask(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement taskRoot = make(Items.CLOCK, "switch_schedule").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SWITCH_SCHEDULE))
                .save(saver, id("maid_base/switch_schedule"), existingFileHelper);

        Advancement backpack = make(InitItems.MAID_BACKPACK_BIG.get(), "maid_backpack").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_BACKPACK))
                .save(saver, id("maid_base/maid_backpack"), existingFileHelper);

        makeGoal(Items.DIAMOND_SWORD, "maid_kill_mob").parent(backpack)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_KILL_MOB))
                .save(saver, id("maid_base/maid_kill_mob"), existingFileHelper);

        make(Items.FISHING_ROD, "maid_fishing").parent(backpack)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FISHING))
                .save(saver, id("maid_base/maid_fishing"), existingFileHelper);

        Advancement farm = makeGoal(Items.IRON_HOE, "maid_farm").parent(taskRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FARM))
                .save(saver, id("maid_base/maid_farm"), existingFileHelper);

        Advancement feedAnimal = make(Items.WHEAT, "maid_feed_animal").parent(farm)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FEED_ANIMAL))
                .save(saver, id("maid_base/maid_feed_animal"), existingFileHelper);

        make(Items.COOKED_BEEF, "maid_feed_player").parent(feedAnimal)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_FEED_PLAYER))
                .save(saver, id("maid_base/maid_feed_player"), existingFileHelper);
    }

    private static void generateBauble(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement baubleRoot = make(InitItems.FIRE_PROTECT_BAUBLE.get(), "use_protect_bauble").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_PROTECT_BAUBLE))
                .save(saver, id("maid_base/use_protect_bauble"), existingFileHelper);

        Advancement fabric = make(InitItems.NIMBLE_FABRIC.get(), "use_nimble_fabric").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_NIMBLE_FABRIC))
                .save(saver, id("maid_base/use_nimble_fabric"), existingFileHelper);

        makeGoal(InitItems.ULTRAMARINE_ORB_ELIXIR.get(), "use_undead_bauble").parent(fabric)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_UNDEAD_BAUBLE))
                .save(saver, id("maid_base/use_undead_bauble"), existingFileHelper);

        Advancement magnet = make(InitItems.ITEM_MAGNET_BAUBLE.get(), "use_item_magnet_bauble").parent(baubleRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_ITEM_MAGNET_BAUBLE))
                .save(saver, id("maid_base/use_item_magnet_bauble"), existingFileHelper);

        make(InitItems.WIRELESS_IO.get(), "use_wireless_io").parent(magnet)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.USE_WIRELESS_IO))
                .save(saver, id("maid_base/use_wireless_io"), existingFileHelper);
    }

    private static void generatePhoto(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement photoRoot = make(InitItems.CAMERA.get(), "photo_maid").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.PHOTO_MAID))
                .save(saver, id("maid_base/photo_maid"), existingFileHelper);

        Advancement statue = make(InitItems.CHISEL.get(), "chisel_statue").parent(photoRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.CHISEL_STATUE))
                .save(saver, id("maid_base/chisel_statue"), existingFileHelper);

        make(InitItems.GARAGE_KIT.get(), "pickup_garage_kit").parent(statue)
                .addCriterion("pickup_item", PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByPlayer(
                        ContextAwarePredicate.ANY,
                        ItemPredicate.Builder.item().of(InitItems.GARAGE_KIT.get()).build(),
                        ContextAwarePredicate.ANY))
                .save(saver, id("maid_base/pickup_garage_kit"), existingFileHelper);
    }

    private static void generateReborn(Advancement root, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        ItemStack stack = ItemEntityPlaceholder.setRecipeId(new ItemStack(InitItems.ENTITY_PLACEHOLDER.get()), id("altar/reborn_maid"));
        Advancement rebornRoot = make(stack, "reborn_maid").parent(root)
                .addCriterion("altar_craft", AltarCraftTrigger.Instance.recipe(id("altar/reborn_maid")))
                .save(saver, id("maid_base/reborn_maid"), existingFileHelper);

        makeGoal(InitItems.SHRINE.get(), "shrine_reborn_maid").parent(rebornRoot)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.SHRINE_REBORN_MAID))
                .save(saver, id("maid_base/shrine_reborn_maid"), existingFileHelper);
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.description", key));

        LanguageGenerator.addLanguage(title);
        LanguageGenerator.addLanguage(desc);

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static Advancement.Builder make(ItemStack item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.description", key));

        LanguageGenerator.addLanguage(title);
        LanguageGenerator.addLanguage(desc);

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static Advancement.Builder makeGoal(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.maid_base.%s.description", key));

        LanguageGenerator.addLanguage(title);
        LanguageGenerator.addLanguage(desc);

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.GOAL, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
