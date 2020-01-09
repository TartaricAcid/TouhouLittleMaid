package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2020/1/5 15:04
 **/
public class ItemGashaponCoin extends Item {
    public ItemGashaponCoin() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".gashapon_coin");
        setCreativeTab(MaidItems.MODEL_COUPON_TABS);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab) && GeneralConfig.MAID_CONFIG.maidCannotChangeModel) {
            items.add(new ItemStack(this));
        }
    }
}
