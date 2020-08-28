package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.JoyType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidJoy;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ItemMaidJoy extends Item {
    private static final String TYPE_TAG_NAME = "MaidJoyType";

    public ItemMaidJoy() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".maid_joy");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @SuppressWarnings("all")
    public static ItemStack setType(ItemStack stack, String type) {
        NBTTagCompound tagCompound;
        if (stack.hasTagCompound()) {
            tagCompound = stack.getTagCompound();
        } else {
            tagCompound = new NBTTagCompound();
        }
        tagCompound.setString(TYPE_TAG_NAME, type);
        stack.setTagCompound(tagCompound);
        return stack;
    }

    public static String getType(ItemStack stack) {
        NBTTagCompound tagCompound;
        if (stack.hasTagCompound()) {
            tagCompound = stack.getTagCompound();
            if (tagCompound.hasKey(TYPE_TAG_NAME, Constants.NBT.TAG_STRING)) {
                return tagCompound.getString(TYPE_TAG_NAME);
            }
        }
        return "reading";
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String key = String.format("item.touhou_little_maid.maid_joy.%s.name", getType(stack));
        return I18n.translateToLocal(key);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (String joyType : JoyType.JOYS.keySet()) {
                items.add(setType(new ItemStack(this), joyType));
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        if (worldIn.isRemote) {
//            return EnumActionResult.SUCCESS;
//        }

        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }

        IBlockState posBlockState = worldIn.getBlockState(pos);
        Block block = posBlockState.getBlock();
        boolean replaceable = block.isReplaceable(worldIn, pos);

        if (!replaceable) {
            pos = pos.up();
        }

        int index = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EnumFacing playerFacing = EnumFacing.byHorizontalIndex(index);
        ItemStack itemstack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos, facing, itemstack)) {
            boolean posBlockStateIsOkay = replaceable || worldIn.isAirBlock(pos);
            if (posBlockStateIsOkay && worldIn.getBlockState(pos.down()).isTopSolid()) {
                IBlockState maidJoy = MaidBlocks.MAID_JOY.getDefaultState().withProperty(BlockMaidBed.FACING, playerFacing);
                worldIn.setBlockState(pos, maidJoy, 10);
                SoundType soundtype = maidJoy.getBlock().getSoundType(maidJoy, worldIn, pos, player);
                worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (worldIn.getTileEntity(pos) instanceof TileEntityMaidJoy) {
                    String type = getType(itemstack);
                    TileEntityMaidJoy te = (TileEntityMaidJoy) (worldIn.getTileEntity(pos));
                    te.setType(type);
                }
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}
