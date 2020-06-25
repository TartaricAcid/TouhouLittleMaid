package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemExtinguisher extends Item {
    public ItemExtinguisher() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".extinguisher");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        setMaxDamage(128);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (worldIn.isRemote) {
            playerIn.sendMessage(new TextComponentTranslation("message.touhou_little_maid.extinguisher.player_cannot_use"));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
