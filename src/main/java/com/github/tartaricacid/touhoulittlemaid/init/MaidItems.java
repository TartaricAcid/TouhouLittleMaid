package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.UltramarineOrbElixir;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class MaidItems {
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "kappa_compass")
    public static ItemKappaCompass KAPPA_COMPASS;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "hakurei_gohei")
    public static ItemHakureiGohei HAKUREI_GOHEI;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "ultramarine_orb_elixir")
    public static UltramarineOrbElixir ULTRAMARINE_ORB_ELIXIR;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "marisa_broom")
    public static ItemMarisaBroom MARISA_BROOM;

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemKappaCompass());
        event.getRegistry().register(new ItemHakureiGohei());
        event.getRegistry().register(new UltramarineOrbElixir());
        event.getRegistry().register(new ItemMarisaBroom());

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
    };
}
