package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<Item> MAID_BACKPACK_SMALL = ITEMS.register("maid_backpack_small", ItemMaidBackpack::new);
    public static RegistryObject<Item> MAID_BACKPACK_MIDDLE = ITEMS.register("maid_backpack_middle", ItemMaidBackpack::new);
    public static RegistryObject<Item> MAID_BACKPACK_BIG = ITEMS.register("maid_backpack_big", ItemMaidBackpack::new);
    public static RegistryObject<Item> CRAFTING_TABLE_BACKPACK = ITEMS.register("crafting_table_backpack", ItemMaidBackpack::new);
    public static RegistryObject<Item> ENDER_CHEST_BACKPACK = ITEMS.register("ender_chest_backpack", ItemMaidBackpack::new);
    public static RegistryObject<Item> FURNACE_BACKPACK = ITEMS.register("furnace_backpack", ItemMaidBackpack::new);
    public static RegistryObject<Item> TANK_BACKPACK = ITEMS.register("tank_backpack", ItemTankBackpack::new);
    public static RegistryObject<Item> CHAIR = ITEMS.register("chair", ItemChair::new);
    public static RegistryObject<Item> HAKUREI_GOHEI = ITEMS.register("hakurei_gohei", ItemHakureiGohei::new);
    public static RegistryObject<Item> SANAE_GOHEI = ITEMS.register("sanae_gohei", ItemHakureiGohei::new);
    public static RegistryObject<Item> MAID_BED = ITEMS.register("maid_bed", ItemMaidBed::new);
    public static RegistryObject<Item> EXTINGUISHER = ITEMS.register("extinguisher", ItemExtinguisher::new);
    public static RegistryObject<Item> ULTRAMARINE_ORB_ELIXIR = ITEMS.register("ultramarine_orb_elixir", () -> new ItemDamageableBauble(6));
    public static RegistryObject<Item> EXPLOSION_PROTECT_BAUBLE = ITEMS.register("explosion_protect_bauble", () -> new ItemDamageableBauble(32));
    public static RegistryObject<Item> FIRE_PROTECT_BAUBLE = ITEMS.register("fire_protect_bauble", () -> new ItemDamageableBauble(128));
    public static RegistryObject<Item> PROJECTILE_PROTECT_BAUBLE = ITEMS.register("projectile_protect_bauble", () -> new ItemDamageableBauble(64));
    public static RegistryObject<Item> MAGIC_PROTECT_BAUBLE = ITEMS.register("magic_protect_bauble", () -> new ItemDamageableBauble(128));
    public static RegistryObject<Item> FALL_PROTECT_BAUBLE = ITEMS.register("fall_protect_bauble", () -> new ItemDamageableBauble(32));
    public static RegistryObject<Item> DROWN_PROTECT_BAUBLE = ITEMS.register("drown_protect_bauble", () -> new ItemDamageableBauble(64));
    public static RegistryObject<Item> NIMBLE_FABRIC = ITEMS.register("nimble_fabric", () -> new ItemDamageableBauble(64));
    public static RegistryObject<Item> ITEM_MAGNET_BAUBLE = ITEMS.register("item_magnet_bauble", ItemNormalBauble::new);
    public static RegistryObject<Item> MUTE_BAUBLE = ITEMS.register("mute_bauble", ItemNormalBauble::new);
    public static RegistryObject<Item> ENTITY_PLACEHOLDER = ITEMS.register("entity_placeholder", ItemEntityPlaceholder::new);
    public static RegistryObject<Item> SUBSTITUTE_JIZO = ITEMS.register("substitute_jizo", ItemSubstituteJizo::new);
    public static RegistryObject<Item> POWER_POINT = ITEMS.register("power_point", ItemPowerPoint::new);
    public static RegistryObject<Item> CAMERA = ITEMS.register("camera", ItemCamera::new);
    public static RegistryObject<Item> PHOTO = ITEMS.register("photo", ItemPhoto::new);
    public static RegistryObject<Item> FILM = ITEMS.register("film", ItemFilm::new);
    public static RegistryObject<Item> CHISEL = ITEMS.register("chisel", ItemChisel::new);
    public static RegistryObject<Item> GARAGE_KIT = ITEMS.register("garage_kit", ItemGarageKit::new);
    public static RegistryObject<Item> SMART_SLAB_INIT = ITEMS.register("smart_slab_init", () -> new ItemSmartSlab(ItemSmartSlab.Type.INIT));
    public static RegistryObject<Item> SMART_SLAB_EMPTY = ITEMS.register("smart_slab_empty", () -> new ItemSmartSlab(ItemSmartSlab.Type.EMPTY));
    public static RegistryObject<Item> SMART_SLAB_HAS_MAID = ITEMS.register("smart_slab_has_maid", () -> new ItemSmartSlab(ItemSmartSlab.Type.HAS_MAID));
    public static RegistryObject<Item> TRUMPET = ITEMS.register("trumpet", ItemTrumpet::new);
    public static RegistryObject<Item> WIRELESS_IO = ITEMS.register("wireless_io", ItemWirelessIO::new);
    public static RegistryObject<Item> MAID_BEACON = ITEMS.register("maid_beacon", ItemMaidBeacon::new);
    public static RegistryObject<Item> MODEL_SWITCHER = ITEMS.register("model_switcher", ItemModelSwitcher::new);
    public static RegistryObject<Item> CHAIR_SHOW = ITEMS.register("chair_show", ItemChairShow::new);
    public static RegistryObject<Item> GOMOKU = ITEMS.register("gomoku", () -> new BlockItem(InitBlocks.GOMOKU.get(), new Item.Properties()));
    public static RegistryObject<Item> CCHESS = ITEMS.register("cchess", () -> new BlockItem(InitBlocks.CCHESS.get(), new Item.Properties()));
    public static RegistryObject<Item> WCHESS = ITEMS.register("wchess", () -> new BlockItem(InitBlocks.WCHESS.get(), new Item.Properties()));
    public static RegistryObject<Item> RED_FOX_SCROLL = ITEMS.register("red_fox_scroll", ItemFoxScroll::new);
    public static RegistryObject<Item> WHITE_FOX_SCROLL = ITEMS.register("white_fox_scroll", ItemFoxScroll::new);
    public static RegistryObject<Item> KEYBOARD = ITEMS.register("keyboard", () -> new BlockItem(InitBlocks.KEYBOARD.get(), new Item.Properties()));
    public static RegistryObject<Item> BOOKSHELF = ITEMS.register("bookshelf", () -> new BlockItem(InitBlocks.BOOKSHELF.get(), new Item.Properties()));
    public static RegistryObject<Item> COMPUTER = ITEMS.register("computer", () -> new BlockItem(InitBlocks.COMPUTER.get(), new Item.Properties()));
    public static RegistryObject<Item> FAVORABILITY_TOOL_ADD = ITEMS.register("favorability_tool_add", () -> new ItemFavorabilityTool("add"));
    public static RegistryObject<Item> FAVORABILITY_TOOL_REDUCE = ITEMS.register("favorability_tool_reduce", () -> new ItemFavorabilityTool("reduce"));
    public static RegistryObject<Item> FAVORABILITY_TOOL_FULL = ITEMS.register("favorability_tool_full", () -> new ItemFavorabilityTool("full"));
    public static RegistryObject<Item> SHRINE = ITEMS.register("shrine", () -> new BlockItem(InitBlocks.SHRINE.get(), new Item.Properties()));
    public static RegistryObject<Item> KAPPA_COMPASS = ITEMS.register("kappa_compass", ItemKappaCompass::new);
    public static RegistryObject<Item> BROOM = ITEMS.register("broom", ItemBroom::new);
    public static RegistryObject<Item> PICNIC_BASKET = ITEMS.register("picnic_basket", () -> new ItemPicnicBasket(InitBlocks.PICNIC_MAT.get()));
    public static RegistryObject<Item> SCARECROW = ITEMS.register("scarecrow", () -> new BlockItem(InitBlocks.SCARECROW.get(), new Item.Properties()));
    public static RegistryObject<Item> SERVANT_BELL = ITEMS.register("servant_bell", ItemServantBell::new);
    public static RegistryObject<Item> MONSTER_LIST = ITEMS.register("monster_list", ItemMonsterList::new);

    public static RegistryObject<Item> MAID_SPAWN_EGG = ITEMS.register("maid_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityMaid.TYPE, 0x4a6195, 0xffffff, new Item.Properties()));
    public static RegistryObject<Item> FAIRY_SPAWN_EGG = ITEMS.register("fairy_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityFairy.TYPE, 0x171c20, 0xffffff, new Item.Properties()));

    // 成就图标
    public static RegistryObject<Item> CHANGE_CHAIR_MODEL = ITEMS.register("change_chair_model", ItemAdvancementIcon::new);
    public static RegistryObject<Item> CHANGE_MAID_MODEL = ITEMS.register("change_maid_model", ItemAdvancementIcon::new);
    public static RegistryObject<Item> MAID_100_HEALTHY = ITEMS.register("maid_100_healthy", ItemAdvancementIcon::new);
    public static RegistryObject<Item> KILL_SLIME_300 = ITEMS.register("kill_slime_300", ItemAdvancementIcon::new);
    public static RegistryObject<Item> ALL_NETHERITE_EQUIPMENT = ITEMS.register("all_netherite_equipment", ItemAdvancementIcon::new);
    public static RegistryObject<Item> KILL_WITHER = ITEMS.register("kill_wither", ItemAdvancementIcon::new);
    public static RegistryObject<Item> KILL_DRAGON = ITEMS.register("kill_dragon", ItemAdvancementIcon::new);
}