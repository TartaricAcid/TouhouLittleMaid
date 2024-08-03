package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.MaidConfigContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.*;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.PicnicBasketContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.WirelessIOContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.PicnicBasketContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitContainerGui {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> MenuScreens.register(EmptyBackpackContainer.TYPE, EmptyBackpackContainerScreen::new));
        evt.enqueueWork(() -> MenuScreens.register(SmallBackpackContainer.TYPE, SmallBackpackContainerScreen::new));
        evt.enqueueWork(() -> MenuScreens.register(MiddleBackpackContainer.TYPE, MiddleBackpackContainerScreen::new));
        evt.enqueueWork(() -> MenuScreens.register(BigBackpackContainer.TYPE, BigBackpackContainerScreen::new));
        evt.enqueueWork(() -> MenuScreens.register(CraftingTableBackpackContainer.TYPE, CraftingTableBackpackContainerScreen::new));
        evt.enqueueWork(() -> MenuScreens.register(EnderChestBackpackContainer.TYPE, EnderChestBackpackContainerScreen::new));
        evt.enqueueWork(() -> MenuScreens.register(FurnaceBackpackContainer.TYPE, FurnaceBackpackContainerScreen::new));
        evt.enqueueWork(() -> MenuScreens.register(TankBackpackContainer.TYPE, TankBackpackContainerScreen::new));

        evt.enqueueWork(() -> MenuScreens.register(MaidConfigContainer.TYPE, MaidConfigContainerGui::new));
        evt.enqueueWork(() -> MenuScreens.register(WirelessIOContainer.TYPE, WirelessIOContainerGui::new));
        evt.enqueueWork(() -> MenuScreens.register(PicnicBasketContainer.TYPE, PicnicBasketContainerScreen::new));
    }
}
