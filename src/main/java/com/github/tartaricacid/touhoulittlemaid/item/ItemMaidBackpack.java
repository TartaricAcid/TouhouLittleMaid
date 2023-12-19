package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.world.item.Item;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemMaidBackpack extends Item {
    public ItemMaidBackpack() {
        super((new Properties()).stacksTo(1).tab(MAIN_TAB));
    }
}
