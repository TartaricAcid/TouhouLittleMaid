package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public final class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<Item> MAID_BACKPACK_SMALL = ITEMS.register("maid_backpack_small", () -> new ItemMaidBackpack(BackpackLevel.SMALL));
    public static RegistryObject<Item> MAID_BACKPACK_MIDDLE = ITEMS.register("maid_backpack_middle", () -> new ItemMaidBackpack(BackpackLevel.MIDDLE));
    public static RegistryObject<Item> MAID_BACKPACK_BIG = ITEMS.register("maid_backpack_big", () -> new ItemMaidBackpack(BackpackLevel.BIG));
    public static RegistryObject<Item> CHAIR = ITEMS.register("chair", ItemChair::new);
    public static RegistryObject<Item> HAKUREI_GOHEI = ITEMS.register("hakurei_gohei", ItemHakureiGohei::new);
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
    public static RegistryObject<Item> CHAIR_SHOW = ITEMS.register("chair_show", ItemChairShow::new);

    public static RegistryObject<Item> MAID_SPAWN_EGG = ITEMS.register("maid_spawn_egg", () -> new SpawnEggItem(EntityMaid.TYPE, 0x4a6195, 0xffffff, (new Item.Properties()).tab(MAIN_TAB)));
    public static RegistryObject<Item> FAIRY_SPAWN_EGG = ITEMS.register("fairy_spawn_egg", () -> new SpawnEggItem(EntityFairy.TYPE, 0x171c20, 0xffffff, (new Item.Properties()).tab(MAIN_TAB)));
}