package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/11/15 13:06
 **/
public class ItemSpellCard extends Item {
    public ItemSpellCard() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".spell_card");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
