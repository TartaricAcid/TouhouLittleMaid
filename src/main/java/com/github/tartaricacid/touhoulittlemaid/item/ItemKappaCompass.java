package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKappaCompass extends Item {
    public ItemKappaCompass() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "kappa_compass");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem(hand).getItem() == this) {
            ItemStack stack = player.getHeldItem(hand);
            setPos(stack, pos.offset(facing));
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        BlockPos pos = getPos(stack);
        if (pos != null) {
            tooltip.add(TextFormatting.GOLD + I18n.format("tooltips.touhou_little_maid.kappa_compass.desc", pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    @Nullable
    public static BlockPos getPos(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT.POS.getName(), Constants.NBT.TAG_COMPOUND)) {
            return NBTUtil.getPosFromTag(stack.getTagCompound().getCompoundTag(NBT.POS.getName()));
        }
        return null;
    }

    public static void setPos(ItemStack stack, BlockPos pos) {
        stack.setTagInfo(NBT.POS.getName(), NBTUtil.createPosTag(pos));
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
