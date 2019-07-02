package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber(Side.CLIENT)
public class MaidItemsModel {
    @SubscribeEvent
    public static void register(ModelRegistryEvent event) {
        registerRender(MaidItems.KAPPA_COMPASS);
        registerRender(MaidItems.HAKUREI_GOHEI);
        registerRender(MaidItems.ULTRAMARINE_ORB_ELIXIR);
    }

    private static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),
                        "inventory"));
    }
}
