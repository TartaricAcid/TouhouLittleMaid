package com.github.tartaricacid.touhoulittlemaid.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/12/1 15:47
 **/
public class MaidCreativeTabs extends CreativeTabs {
    public MaidCreativeTabs(String name) {
        super(String.format("touhou_little_maid.%s.name", name));
    }

    @Override
    public ItemStack createIcon() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public String getTranslationKey() {
        return "item_group." + getTabLabel();
    }
}
