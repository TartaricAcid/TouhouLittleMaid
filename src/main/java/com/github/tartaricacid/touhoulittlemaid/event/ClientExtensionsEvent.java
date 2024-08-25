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
        event.registerBlock(BlockAltar.CLIENT_BLOCK_EXTENSIONS, InitBlocks.ALTAR.get());
        event.registerBlock(BlockGarageKit.CLIENT_BLOCK_EXTENSIONS, InitBlocks.GARAGE_KIT.get());
        event.registerBlock(BlockStatue.CLIENT_BLOCK_EXTENSIONS, InitBlocks.STATUE.get());
        event.registerItem(ItemChair.ITEM_EXTENSIONS, InitItems.CHAIR.get());
        event.registerItem(ItemEntityPlaceholder.ITEM_EXTENSIONS, InitItems.ENTITY_PLACEHOLDER.get());
        event.registerItem(ItemGarageKit.ITEM_EXTENSIONS, InitItems.GARAGE_KIT.get());
        event.registerItem(ItemPicnicBasket.ITEM_EXTENSIONS, InitItems.PICNIC_BASKET.get());
    }
}
