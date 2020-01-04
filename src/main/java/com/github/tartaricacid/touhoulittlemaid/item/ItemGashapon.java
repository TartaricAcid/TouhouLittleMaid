package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.util.DrawCalculation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/12/31 20:37
 **/
public class ItemGashapon extends Item {
    public ItemGashapon() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".gashapon");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MODEL_COUPON_TABS);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 1; i <= 5; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (!worldIn.isRemote) {
            int meta = playerIn.getHeldItem(handIn).getMetadata();
            int times = meta > 2 ? meta - 1 : 1;
            for (int i = 0; i < times; i++) {
                ItemStack itemStack = new ItemStack(MaidItems.MAID_MODEL_COUPON, 1, meta);
                ItemMaidModelCoupon.setModelData(itemStack, DrawCalculation.getCouponModelId(meta));
                EntityItem itemEntity = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, itemStack);
                worldIn.spawnEntity(itemEntity);
            }
        }
        playerIn.getHeldItem(handIn).shrink(1);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.getMetadata() > 3) {
            return true;
        }
        return super.hasEffect(stack);
    }
}
