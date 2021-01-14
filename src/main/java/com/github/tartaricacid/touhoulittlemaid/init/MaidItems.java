package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class MaidItems {
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "kappa_compass")
    public static Item KAPPA_COMPASS;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "hakurei_gohei")
    public static Item HAKUREI_GOHEI;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "ultramarine_orb_elixir")
    public static Item ULTRAMARINE_ORB_ELIXIR;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "explosion_protect_bauble")
    public static Item EXPLOSION_PROTECT_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "fire_protect_bauble")
    public static Item FIRE_PROTECT_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "projectile_protect_bauble")
    public static Item PROJECTILE_PROTECT_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "magic_protect_bauble")
    public static Item MAGIC_PROTECT_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "fall_protect_bauble")
    public static Item FALL_PROTECT_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "drown_protect_bauble")
    public static Item DROWN_PROTECT_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "tombstone_bauble")
    public static Item TOMBSTONE_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "nimble_fabric")
    public static Item NIMBLE_FABRIC;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "marisa_broom")
    public static Item MARISA_BROOM;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "item_magnet_bauble")
    public static Item ITEM_MAGNET_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "mute_bauble")
    public static Item MUTE_BAUBLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "camera")
    public static Item CAMERA;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "photo")
    public static Item PHOTO;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "chair")
    public static Item CHAIR;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "hata_sasimono")
    public static Item HATA_SASIMONO;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "album")
    public static Item ALBUM;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "maid_beacon")
    public static Item MAID_BEACON;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "spell_card")
    public static Item SPELL_CARD;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "debug_danmaku")
    public static Item DEBUG_DANMAKU;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "npc_maid_tool")
    public static Item NPC_MAID_TOOL;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "substitute_jizo")
    public static Item SUBSTITUTE_JIZO;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "maid_model_coupon")
    public static Item MAID_MODEL_COUPON;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "gashapon")
    public static Item GASHAPON;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "gashapon_coin")
    public static Item GASHAPON_COIN;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "maid_backpack")
    public static Item MAID_BACKPACK;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "suitcase")
    public static Item SUITCASE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "trolley_audio")
    public static Item TROLLEY_AUDIO;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "maid_vehicle")
    public static Item MAID_VEHICLE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "scarecrow")
    public static Item SCARECROW;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "portable_audio")
    public static Item PORTABLE_AUDIO;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "chisel")
    public static Item CHISEL;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "extinguisher")
    public static Item EXTINGUISHER;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "power_point")
    public static Item POWER_POINT;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "maid_bed")
    public static Item MAID_BED;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "maid_joy")
    public static Item MAID_JOY;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "smart_slab")
    public static Item SMART_SLAB;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "wireless_io")
    public static Item WIRELESS_IO;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "hammer")
    public static Item HAMMER;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "furnace_guide")
    public static Item FURNACE_GUIDE;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "potion_guide")
    public static Item POTION_GUIDE;

    public static CreativeTabs MAIN_TABS = new MaidCreativeTabs("main") {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getIcon() {
            return HAKUREI_GOHEI.getDefaultInstance();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
            addSpawnEgg("entity.passive.maid", items);
            addSpawnEgg("entity.monster.rinnosuke", items);
            addSpawnEgg("entity.monster.fairy", items);
            super.displayAllRelevantItems(items);
        }
    };
    public static CreativeTabs GARAGE_KIT_TABS = new MaidCreativeTabs("garage_kit") {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getIcon() {
            return new ItemStack(MaidBlocks.GARAGE_KIT);
        }
    };
    public static CreativeTabs CHAIR_TABS = new MaidCreativeTabs("chair") {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getIcon() {
            return CHAIR.getDefaultInstance();
        }
    };
    public static CreativeTabs MODEL_COUPON_TABS = new MaidCreativeTabs("model_coupon") {
        @SideOnly(Side.CLIENT)
        @Override
        public ItemStack getIcon() {
            return MAID_MODEL_COUPON.getDefaultInstance();
        }
    };

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(getDamageableBaubles(6, "ultramarine_orb_elixir"));
        event.getRegistry().register(getDamageableBaubles(32, "explosion_protect_bauble"));
        event.getRegistry().register(getDamageableBaubles(128, "fire_protect_bauble"));
        event.getRegistry().register(getDamageableBaubles(64, "projectile_protect_bauble"));
        event.getRegistry().register(getDamageableBaubles(128, "magic_protect_bauble"));
        event.getRegistry().register(getDamageableBaubles(32, "fall_protect_bauble"));
        event.getRegistry().register(getDamageableBaubles(64, "drown_protect_bauble"));
        event.getRegistry().register(getDamageableBaubles(64, "nimble_fabric"));
        event.getRegistry().register(getNormalBaubles("tombstone_bauble"));
        event.getRegistry().register(new ItemWirelessIO().setRegistryName("wireless_io"));
        event.getRegistry().register(getNormalBaubles("item_magnet_bauble"));
        event.getRegistry().register(getNormalBaubles("mute_bauble"));

        event.getRegistry().register(new ItemKappaCompass().setRegistryName("kappa_compass"));
        event.getRegistry().register(new ItemHakureiGohei().setRegistryName("hakurei_gohei"));
        event.getRegistry().register(new ItemMarisaBroom().setRegistryName("marisa_broom"));
        event.getRegistry().register(new ItemCamera().setRegistryName("camera"));
        event.getRegistry().register(new ItemPhoto().setRegistryName("photo"));
        event.getRegistry().register(new ItemChair().setRegistryName("chair"));
        event.getRegistry().register(new ItemHataSasimono().setRegistryName("hata_sasimono"));
        event.getRegistry().register(new ItemAlbum().setRegistryName("album"));
        event.getRegistry().register(new ItemMaidBeacon(MaidBlocks.MAID_BEACON).setRegistryName("maid_beacon"));
        event.getRegistry().register(new ItemSpellCard().setRegistryName("spell_card"));
        event.getRegistry().register(new ItemDebugDanmaku().setRegistryName("debug_danmaku"));
        event.getRegistry().register(new ItemNpcMaidTool().setRegistryName("npc_maid_tool"));
        event.getRegistry().register(new ItemSubstituteJizo().setRegistryName("substitute_jizo"));
        event.getRegistry().register(new ItemMaidModelCoupon().setRegistryName("maid_model_coupon"));
        event.getRegistry().register(new ItemGashapon().setRegistryName("gashapon"));
        event.getRegistry().register(new ItemGashaponCoin().setRegistryName("gashapon_coin"));
        event.getRegistry().register(new ItemMaidBackpack().setRegistryName("maid_backpack"));
        event.getRegistry().register(new ItemSuitcase().setRegistryName("suitcase"));
        event.getRegistry().register(new ItemTrolleyAudio().setRegistryName("trolley_audio"));
        event.getRegistry().register(new ItemScarecrow().setRegistryName("scarecrow"));
        event.getRegistry().register(new ItemPortableAudio().setRegistryName("portable_audio"));
        event.getRegistry().register(new ItemChisel().setRegistryName("chisel"));
        event.getRegistry().register(new ItemExtinguisher().setRegistryName("extinguisher"));
        event.getRegistry().register(new ItemPowerPoint().setRegistryName("power_point"));
        event.getRegistry().register(new ItemMaidBed().setRegistryName("maid_bed"));
        event.getRegistry().register(new ItemMaidJoy().setRegistryName("maid_joy"));
        event.getRegistry().register(new ItemSmartSlab().setRegistryName("smart_slab"));
        event.getRegistry().register(new ItemHammer().setRegistryName("hammer"));
        event.getRegistry().register(new ItemFurnaceGuide().setRegistryName("furnace_guide"));
        event.getRegistry().register(new ItemPotionGuide().setRegistryName("potion_guide"));

        event.getRegistry().register(new ItemMaidVehicle().setRegistryName("maid_vehicle"));
        event.getRegistry().register(new ItemBlock(MaidBlocks.GRID).setRegistryName("grid"));
        event.getRegistry().register(new ItemBlock(MaidBlocks.GARAGE_KIT).setRegistryName(MaidBlocks.GARAGE_KIT.getRegistryName()));
        event.getRegistry().register(new ItemBlock(MaidBlocks.GASHAPON_MACHINES).setRegistryName(MaidBlocks.GASHAPON_MACHINES.getRegistryName()));
    }

    private static Item getNormalBaubles(String id) {
        return new Item()
                .setRegistryName(id)
                .setTranslationKey(TouhouLittleMaid.MOD_ID + "." + id)
                .setMaxStackSize(1)
                .setCreativeTab(MaidItems.MAIN_TABS);
    }

    private static Item getDamageableBaubles(int maxDamage, String id) {
        return new ItemDamageable(maxDamage - 1)
                .setRegistryName(id)
                .setTranslationKey(TouhouLittleMaid.MOD_ID + "." + id)
                .setMaxStackSize(1)
                .setNoRepair()
                .setCreativeTab(MaidItems.MAIN_TABS);
    }

    private static void addSpawnEgg(String id, NonNullList<ItemStack> items) {
        ItemStack egg = new ItemStack(Items.SPAWN_EGG);
        ItemMonsterPlacer.applyEntityIdToItemStack(egg, new ResourceLocation(TouhouLittleMaid.MOD_ID, id));
        items.add(egg);
    }
}
