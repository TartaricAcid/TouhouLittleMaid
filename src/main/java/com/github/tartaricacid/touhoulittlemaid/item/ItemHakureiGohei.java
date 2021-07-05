package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.item.Item;

public class ItemHakureiGohei extends Item {
    public ItemHakureiGohei() {
        super((new Properties()).tab(InitItems.MAIN_TAB).durability(300).setNoRepair());
    }
}
