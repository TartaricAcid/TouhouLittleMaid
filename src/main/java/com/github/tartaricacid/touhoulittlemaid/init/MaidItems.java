package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class MaidItems {
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "kappa_compass")
    public static Item KAPPA_COMPASS;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "hakurei_gohei")
    public static Item HAKUREI_GOHEI;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "ultramarine_orb_elixir")
    public static Item ULTRAMARINE_ORB_ELIXIR;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "marisa_broom")
    public static Item MARISA_BROOM;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "camera")
    public static Item CAMERA;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "photo")
    public static Item PHOTO;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "chair")
    public static Item CHAIR;

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemKappaCompass().setRegistryName("kappa_compass"));
        event.getRegistry().register(new ItemHakureiGohei().setRegistryName("hakurei_gohei"));
        event.getRegistry().register(new ItemDamageable(5).setRegistryName("ultramarine_orb_elixir")
                .setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "ultramarine_orb_elixir")
                .setCreativeTab(MaidItems.TABS));
        event.getRegistry().register(new ItemMarisaBroom().setRegistryName("marisa_broom"));
        event.getRegistry().register(new ItemCamera().setRegistryName("camera"));
        event.getRegistry().register(new ItemPhoto().setRegistryName("photo"));
        event.getRegistry().register(new ItemChair().setRegistryName("chair"));

        event.getRegistry().register(new ItemBlock(MaidBlocks.GRID).setRegistryName("grid"));
        event.getRegistry().register(new ItemBlock(MaidBlocks.GARAGE_KIT).setRegistryName(MaidBlocks.GARAGE_KIT.getRegistryName()));
    }


    public static final CreativeTabs TABS = new CreativeTabs("touhou_little_maid.name") {
        @Override
        public ItemStack createIcon() {
            return HAKUREI_GOHEI.getDefaultInstance();
        }

        @SideOnly(Side.CLIENT)
        @Override
        public String getTranslationKey() {
            return "item_group." + getTabLabel();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            ItemStack egg = new ItemStack(Items.SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(egg, new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"));
            items.add(egg);
            super.displayAllRelevantItems(items);
        }
    };
}
