package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
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
        setRegistryName("kappa_compass");
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "kappa_compass");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() == this) {
            ItemStack stack = player.getHeldItem(hand);
            int[] i = {pos.getX(), pos.getY() + 1, pos.getZ()};
            setPos(stack, i);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int[] i = getPos(stack);
        if (i != null) {
            tooltip.add(TextFormatting.GOLD + I18n.format("tooltips.touhou_little_maid.kappa_compass.desc", i[0], i[1], i[2]));
        }
    }

    @Nullable
    public int[] getPos(ItemStack stack) {
        if (stack.getItem() == this && stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT.POS.getName())) {
            return stack.getTagCompound().getIntArray(NBT.POS.getName());
        }
        return null;
    }

    public void setPos(ItemStack stack, int[] pos) {
        if (stack.getItem() == this) {
            if (!stack.hasTagCompound()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setIntArray(NBT.POS.getName(), new int[]{pos[0], pos[1], pos[2]});
                stack.setTagCompound(compound);
            } else {
                stack.getTagCompound().setIntArray(NBT.POS.getName(), new int[]{pos[0], pos[1], pos[2]});
            }
        }
    }

    /**
     * NBT 数据的枚举
     */
    private enum NBT {
        // 记录的坐标
        POS("Pos");

        String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
