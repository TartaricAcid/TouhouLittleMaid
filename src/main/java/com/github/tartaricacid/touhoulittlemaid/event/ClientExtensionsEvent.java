package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockAltar;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.block.BlockStatue;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import com.github.tartaricacid.touhoulittlemaid.item.ItemGarageKit;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPicnicBasket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientExtensionsEvent {
    @SubscribeEvent
    public static void RegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerBlock(BlockAltar.iClientBlockExtensions, InitBlocks.ALTAR.get());
        event.registerBlock(BlockGarageKit.iClientBlockExtensions, InitBlocks.GARAGE_KIT.get());
        event.registerBlock(BlockStatue.iClientBlockExtensions, InitBlocks.STATUE.get());
        event.registerItem(ItemChair.itemExtensions, InitItems.CHAIR.get());
        event.registerItem(ItemEntityPlaceholder.itemExtensions, InitItems.ENTITY_PLACEHOLDER.get());
        event.registerItem(ItemGarageKit.itemExtensions, InitItems.GARAGE_KIT.get());
        event.registerItem(ItemPicnicBasket.itemExtensions, InitItems.PICNIC_BASKET.get());
    }
}
