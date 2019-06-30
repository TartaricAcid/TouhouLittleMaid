package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKappaCompass extends Item {
    public ItemKappaCompass() {
        this.setRegistryName("kappa_compass");
        this.setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "kappa_compass");
        this.setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() == this) {
            ItemStack stack = player.getHeldItem(hand);
            int[] i = {pos.getX(), pos.getY() + 1, pos.getZ()};
            if (!stack.hasTagCompound()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setIntArray("Pos", i);
                stack.setTagCompound(compound);
            } else {
                NBTTagCompound compound = stack.getTagCompound();
                compound.setIntArray("Pos", i);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Pos")) {
            int[] i = stack.getTagCompound().getIntArray("Pos");
            tooltip.add(TextFormatting.GOLD + I18n.format("tooltips.touhou_little_maid.kappa_compass.desc", i[0], i[1], i[2]));
        }
    }
}
