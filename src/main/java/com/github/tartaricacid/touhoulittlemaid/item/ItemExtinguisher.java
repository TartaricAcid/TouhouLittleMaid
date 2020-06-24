package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.item.Item;

public class ItemExtinguisher extends Item {
    public ItemExtinguisher() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".extinguisher");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        setMaxDamage(128);
    }
}
