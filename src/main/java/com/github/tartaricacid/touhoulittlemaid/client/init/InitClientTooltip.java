package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.tooltip.ClientItemContainerTooltip;
import com.github.tartaricacid.touhoulittlemaid.client.tooltip.ClientMaidTooltip;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemContainerTooltip;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemMaidTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InitClientTooltip {
    @SubscribeEvent
    public static void onRegisterClientTooltip(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MinecraftForgeClient.registerTooltipComponentFactory(ItemMaidTooltip.class, ClientMaidTooltip::new));
        event.enqueueWork(() -> MinecraftForgeClient.registerTooltipComponentFactory(ItemContainerTooltip.class, ClientItemContainerTooltip::new));
    }
}
