package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemMaidBeacon extends TallBlockItem {
    public ItemMaidBeacon() {
        super(InitBlocks.MAID_BEACON.get(), (new Item.Properties()).stacksTo(1).tab(MAIN_TAB));
    }
}
