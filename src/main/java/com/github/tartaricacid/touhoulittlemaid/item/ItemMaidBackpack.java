package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.item.Item;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemMaidBackpack extends Item {
    public ItemMaidBackpack() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
    }
}
