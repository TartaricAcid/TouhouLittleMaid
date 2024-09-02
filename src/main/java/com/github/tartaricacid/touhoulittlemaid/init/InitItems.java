package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class InitItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TouhouLittleMaid.MOD_ID);

    public static DeferredItem<Item> MAID_BACKPACK_SMALL = ITEMS.register("maid_backpack_small", ItemMaidBackpack::new);
    public static DeferredItem<Item> MAID_BACKPACK_MIDDLE = ITEMS.register("maid_backpack_middle", ItemMaidBackpack::new);
    public static DeferredItem<Item> MAID_BACKPACK_BIG = ITEMS.register("maid_backpack_big", ItemMaidBackpack::new);
    public static DeferredItem<Item> CRAFTING_TABLE_BACKPACK = ITEMS.register("crafting_table_backpack", ItemMaidBackpack::new);
    public static DeferredItem<Item> ENDER_CHEST_BACKPACK = ITEMS.register("ender_chest_backpack", ItemMaidBackpack::new);
    public static DeferredItem<Item> FURNACE_BACKPACK = ITEMS.register("furnace_backpack", ItemMaidBackpack::new);
    public static DeferredItem<Item> TANK_BACKPACK = ITEMS.register("tank_backpack", ItemTankBackpack::new);
    public static DeferredItem<Item> CHAIR = ITEMS.register("chair", ItemChair::new);
    public static DeferredItem<Item> HAKUREI_GOHEI = ITEMS.register("hakurei_gohei", ItemHakureiGohei::new);
    public static DeferredItem<Item> SANAE_GOHEI = ITEMS.register("sanae_gohei", ItemHakureiGohei::new);
    public static DeferredItem<Item> MAID_BED = ITEMS.register("maid_bed", ItemMaidBed::new);
    public static DeferredItem<Item> EXTINGUISHER = ITEMS.register("extinguisher", ItemExtinguisher::new);
    public static DeferredItem<Item> ULTRAMARINE_ORB_ELIXIR = ITEMS.register("ultramarine_orb_elixir", () -> new ItemDamageableBauble(6));
    public static DeferredItem<Item> EXPLOSION_PROTECT_BAUBLE = ITEMS.register("explosion_protect_bauble", () -> new ItemDamageableBauble(32));
    public static DeferredItem<Item> FIRE_PROTECT_BAUBLE = ITEMS.register("fire_protect_bauble", () -> new ItemDamageableBauble(128));
    public static DeferredItem<Item> PROJECTILE_PROTECT_BAUBLE = ITEMS.register("projectile_protect_bauble", () -> new ItemDamageableBauble(64));
    public static DeferredItem<Item> MAGIC_PROTECT_BAUBLE = ITEMS.register("magic_protect_bauble", () -> new ItemDamageableBauble(128));
    public static DeferredItem<Item> FALL_PROTECT_BAUBLE = ITEMS.register("fall_protect_bauble", () -> new ItemDamageableBauble(32));
    public static DeferredItem<Item> DROWN_PROTECT_BAUBLE = ITEMS.register("drown_protect_bauble", () -> new ItemDamageableBauble(64));
    public static DeferredItem<Item> NIMBLE_FABRIC = ITEMS.register("nimble_fabric", () -> new ItemDamageableBauble(64));
    public static DeferredItem<Item> ITEM_MAGNET_BAUBLE = ITEMS.register("item_magnet_bauble", ItemNormalBauble::new);
    public static DeferredItem<Item> MUTE_BAUBLE = ITEMS.register("mute_bauble", ItemNormalBauble::new);
    public static DeferredItem<Item> ENTITY_PLACEHOLDER = ITEMS.register("entity_placeholder", ItemEntityPlaceholder::new);
    public static DeferredItem<Item> SUBSTITUTE_JIZO = ITEMS.register("substitute_jizo", ItemSubstituteJizo::new);
    public static DeferredItem<Item> POWER_POINT = ITEMS.register("power_point", ItemPowerPoint::new);
    public static DeferredItem<Item> CAMERA = ITEMS.register("camera", ItemCamera::new);
    public static DeferredItem<Item> PHOTO = ITEMS.register("photo", ItemPhoto::new);
    public static DeferredItem<Item> FILM = ITEMS.register("film", ItemFilm::new);
    public static DeferredItem<Item> CHISEL = ITEMS.register("chisel", ItemChisel::new);
    public static DeferredItem<Item> GARAGE_KIT = ITEMS.register("garage_kit", ItemGarageKit::new);
    public static DeferredItem<Item> SMART_SLAB_INIT = ITEMS.register("smart_slab_init", () -> new ItemSmartSlab(ItemSmartSlab.Type.INIT));
    public static DeferredItem<Item> SMART_SLAB_EMPTY = ITEMS.register("smart_slab_empty", () -> new ItemSmartSlab(ItemSmartSlab.Type.EMPTY));
    public static DeferredItem<Item> SMART_SLAB_HAS_MAID = ITEMS.register("smart_slab_has_maid", () -> new ItemSmartSlab(ItemSmartSlab.Type.HAS_MAID));
    public static DeferredItem<Item> TRUMPET = ITEMS.register("trumpet", ItemTrumpet::new);
    public static DeferredItem<Item> WIRELESS_IO = ITEMS.register("wireless_io", ItemWirelessIO::new);
    public static DeferredItem<Item> MAID_BEACON = ITEMS.register("maid_beacon", ItemMaidBeacon::new);
    public static DeferredItem<Item> MODEL_SWITCHER = ITEMS.register("model_switcher", ItemModelSwitcher::new);
    public static DeferredItem<Item> CHAIR_SHOW = ITEMS.register("chair_show", ItemChairShow::new);
    public static DeferredItem<Item> GOMOKU = ITEMS.register("gomoku", () -> new BlockItem(InitBlocks.GOMOKU.get(), new Item.Properties()));
    public static DeferredItem<Item> RED_FOX_SCROLL = ITEMS.register("red_fox_scroll", ItemFoxScroll::new);
    public static DeferredItem<Item> WHITE_FOX_SCROLL = ITEMS.register("white_fox_scroll", ItemFoxScroll::new);
    public static DeferredItem<Item> KEYBOARD = ITEMS.register("keyboard", () -> new BlockItem(InitBlocks.KEYBOARD.get(), new Item.Properties()));
    public static DeferredItem<Item> BOOKSHELF = ITEMS.register("bookshelf", () -> new BlockItem(InitBlocks.BOOKSHELF.get(), new Item.Properties()));
    public static DeferredItem<Item> COMPUTER = ITEMS.register("computer", () -> new BlockItem(InitBlocks.COMPUTER.get(), new Item.Properties()));
    public static DeferredItem<Item> FAVORABILITY_TOOL_ADD = ITEMS.register("favorability_tool_add", () -> new ItemFavorabilityTool("add"));
    public static DeferredItem<Item> FAVORABILITY_TOOL_REDUCE = ITEMS.register("favorability_tool_reduce", () -> new ItemFavorabilityTool("reduce"));
    public static DeferredItem<Item> FAVORABILITY_TOOL_FULL = ITEMS.register("favorability_tool_full", () -> new ItemFavorabilityTool("full"));
    public static DeferredItem<Item> SHRINE = ITEMS.register("shrine", () -> new BlockItem(InitBlocks.SHRINE.get(), new Item.Properties()));
    public static DeferredItem<Item> KAPPA_COMPASS = ITEMS.register("kappa_compass", ItemKappaCompass::new);
    public static DeferredItem<Item> BROOM = ITEMS.register("broom", ItemBroom::new);
    public static DeferredItem<Item> PICNIC_BASKET = ITEMS.register("picnic_basket", () -> new ItemPicnicBasket(InitBlocks.PICNIC_MAT.get()));
    public static DeferredItem<Item> SCARECROW = ITEMS.register("scarecrow", () -> new BlockItem(InitBlocks.SCARECROW.get(), new Item.Properties()));
    public static DeferredItem<Item> SERVANT_BELL = ITEMS.register("servant_bell", ItemServantBell::new);

    public static DeferredItem<Item> MAID_SPAWN_EGG = ITEMS.register("maid_spawn_egg", () -> new DeferredSpawnEggItem(() -> EntityMaid.TYPE, 0x4a6195, 0xffffff, new Item.Properties()));
    public static DeferredItem<Item> FAIRY_SPAWN_EGG = ITEMS.register("fairy_spawn_egg", () -> new DeferredSpawnEggItem(() -> EntityFairy.TYPE, 0x171c20, 0xffffff, new Item.Properties()));

    public static final ResourceLocation MEMORIZABLE_GENSOKYO_LOCATION = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo");
}