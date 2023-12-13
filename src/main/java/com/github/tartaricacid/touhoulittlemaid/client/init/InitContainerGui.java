package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.MaidConfigContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.*;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.WirelessIOContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitContainerGui {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> ScreenManager.register(EmptyBackpackContainer.TYPE, EmptyBackpackContainerScreen::new));
        evt.enqueueWork(() -> ScreenManager.register(SmallBackpackContainer.TYPE, SmallBackpackContainerScreen::new));
        evt.enqueueWork(() -> ScreenManager.register(MiddleBackpackContainer.TYPE, MiddleBackpackContainerScreen::new));
        evt.enqueueWork(() -> ScreenManager.register(BigBackpackContainer.TYPE, BigBackpackContainerScreen::new));
        evt.enqueueWork(() -> ScreenManager.register(CraftingTableBackpackContainer.TYPE, CraftingTableBackpackContainerScreen::new));
        evt.enqueueWork(() -> ScreenManager.register(EnderChestBackpackContainer.TYPE, EnderChestBackpackContainerScreen::new));
        evt.enqueueWork(() -> ScreenManager.register(FurnaceBackpackContainer.TYPE, FurnaceBackpackContainerScreen::new));

        evt.enqueueWork(() -> ScreenManager.register(MaidConfigContainer.TYPE, MaidConfigContainerGui::new));
        evt.enqueueWork(() -> ScreenManager.register(WirelessIOContainer.TYPE, WirelessIOContainerGui::new));
    }
}
