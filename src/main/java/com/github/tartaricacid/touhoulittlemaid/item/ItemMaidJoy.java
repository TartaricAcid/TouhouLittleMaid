package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidJoy;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.JoyType;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidJoy;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
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

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.JoyType.JOYS;

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
            for (String joyType : JOYS.keySet()) {
                items.add(setType(new ItemStack(this), joyType));
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
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
        JoyType type = JOYS.get(getType(itemstack));
        BlockPos[] posList = type.getPosList(playerFacing);
        List<BlockPos> absolutePosList = Lists.newArrayList();

        for (BlockPos p : posList) {
            BlockPos posIn = pos.add(p);
            absolutePosList.add(posIn);
            IBlockState state = worldIn.getBlockState(posIn);

            boolean blockIsOkay = state.getBlock().isReplaceable(worldIn, posIn) || worldIn.isAirBlock(posIn);
            boolean playerCanEdit = player.canPlayerEdit(pos, facing, itemstack);
            if (!playerCanEdit || !blockIsOkay) {
                return EnumActionResult.FAIL;
            }
        }

        for (BlockPos p : absolutePosList) {
            IBlockState maidJoy = MaidBlocks.MAID_JOY.getDefaultState()
                    .withProperty(BlockHorizontal.FACING, playerFacing)
                    .withProperty(BlockMaidJoy.CORE, p.equals(pos));
            worldIn.setBlockState(p, maidJoy, 10);
            if (worldIn.getTileEntity(p) instanceof TileEntityMaidJoy) {
                TileEntityMaidJoy te = (TileEntityMaidJoy) (worldIn.getTileEntity(p));
                if (te != null) {
                    te.setForgeData(type.getName(), p.equals(pos), pos, absolutePosList);
                }
            }
        }

        SoundType soundtype = SoundType.WOOD;
        worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
                (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
