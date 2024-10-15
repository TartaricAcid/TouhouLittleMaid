package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.MaidConfigContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.*;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.DefaultMaidTaskConfigContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.PicnicBasketContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.WirelessIOContainerGui;
import com.github.tartaricacid.touhoulittlemaid.init.InitContainer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class InitContainerGui {
    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
        event.register(InitContainer.MAID_EMPTY_BACKPACK_CONTAINER.get(), EmptyBackpackContainerScreen::new);
        event.register(InitContainer.MAID_SMALL_BACKPACK_CONTAINER.get(), SmallBackpackContainerScreen::new);
        event.register(InitContainer.MAID_MIDDLE_BACKPACK_CONTAINER.get(), MiddleBackpackContainerScreen::new);
        event.register(InitContainer.MAID_BIG_BACKPACK_CONTAINER.get(), BigBackpackContainerScreen::new);
        event.register(InitContainer.MAID_CRAFTING_TABLE_BACKPACK_CONTAINER.get(), CraftingTableBackpackContainerScreen::new);
        event.register(InitContainer.MAID_ENDER_CHEST_CONTAINER.get(), EnderChestBackpackContainerScreen::new);
        event.register(InitContainer.MAID_FURNACE_CONTAINER.get(), FurnaceBackpackContainerScreen::new);
        event.register(InitContainer.MAID_TANK_CONTAINER.get(), TankBackpackContainerScreen::new);

        event.register(InitContainer.MAID_CONFIG_CONTAINER.get(), MaidConfigContainerGui::new);
        event.register(InitContainer.WIRELESS_IO_CONTAINER.get(), WirelessIOContainerGui::new);
        event.register(InitContainer.PICNIC_BASKET_CONTAINER.get(), PicnicBasketContainerScreen::new);

        event.register(InitContainer.DEFAULT_MAIK_TASK_CONFIG.get(), DefaultMaidTaskConfigContainerGui::new);
    }
}
