package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.item.ItemKappaCompass;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class MaidItems {
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "kappa_compass")
    public static ItemKappaCompass KAPPA_COMPASS;
    @GameRegistry.ObjectHolder(TouhouLittleMaid.MOD_ID + ":" + "hakurei_gohei")
    public static ItemHakureiGohei HAKUREI_GOHEI;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemKappaCompass());
        event.getRegistry().register(new ItemHakureiGohei());
    }
}
