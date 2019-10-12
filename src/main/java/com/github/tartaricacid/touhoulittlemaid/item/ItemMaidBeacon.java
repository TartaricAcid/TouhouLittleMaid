package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/10/8 15:48
 **/
public class ItemMaidBeacon extends Item {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final String STORAGE_DATA_TAG = "StorageData";
    private Block block;

    public ItemMaidBeacon(Block block) {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".maid_beacon");
        setCreativeTab(MaidItems.TABS);
        this.block = block;
    }

    @SuppressWarnings("all")
    public static ItemStack tileEntityToItemStack(TileEntityMaidBeacon beacon) {
        ItemStack stack = new ItemStack(MaidItems.MAID_BEACON);
        NBTTagCompound storageTag = new NBTTagCompound();
        NBTTagCompound stackTag;
        if (stack.hasTagCompound()) {
            stackTag = stack.getTagCompound();
        } else {
            stackTag = new NBTTagCompound();
        }
        stackTag.setTag(STORAGE_DATA_TAG, beacon.writeBeaconNBT(storageTag));
        stack.setTagCompound(stackTag);
        return stack;
    }

    @SuppressWarnings("all")
    private static void itemStackToTileEntity(ItemStack stack, TileEntityMaidBeacon beacon) {
        if (stack.hasTagCompound()) {
            NBTTagCompound stackTag = stack.getTagCompound();
            if (stackTag.hasKey(STORAGE_DATA_TAG)) {
                NBTTagCompound storageTag = stackTag.getCompoundTag(STORAGE_DATA_TAG);
                beacon.readBeaconNBT(storageTag);
            }
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (!block.isReplaceable(worldIn, pos)) {
                pos = pos.offset(facing);
            }

            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(pos, facing, itemstack) && this.block.canPlaceBlockAt(worldIn, pos)) {
                EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
                placeBeacon(worldIn, pos, enumfacing, this.block, itemstack);
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    private static void placeBeacon(World worldIn, BlockPos pos, EnumFacing facing, Block beacon, ItemStack stack) {
        BlockPos posUp = pos.up();
        IBlockState stateUp;
        if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
            stateUp = beacon.getDefaultState().withProperty(BlockMaidBeacon.POSITION, BlockMaidBeacon.Position.UP_N_S);
        } else {
            stateUp = beacon.getDefaultState().withProperty(BlockMaidBeacon.POSITION, BlockMaidBeacon.Position.UP_W_E);
        }
        worldIn.setBlockState(pos, beacon.getDefaultState(), 2);
        worldIn.setBlockState(posUp, stateUp, 2);
        worldIn.notifyNeighborsOfStateChange(pos, beacon, false);
        worldIn.notifyNeighborsOfStateChange(posUp, beacon, false);

        TileEntity te = worldIn.getTileEntity(posUp);
        if (te instanceof TileEntityMaidBeacon) {
            TileEntityMaidBeacon tileEntityMaidBeacon = (TileEntityMaidBeacon) te;
            itemStackToTileEntity(stack, tileEntityMaidBeacon);
            tileEntityMaidBeacon.refresh();
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        float numPower = 0f;
        if (stack.hasTagCompound()) {
            NBTTagCompound stackTag = stack.getTagCompound();
            if (stackTag.hasKey(STORAGE_DATA_TAG)) {
                NBTTagCompound storageTag = stackTag.getCompoundTag(STORAGE_DATA_TAG);
                numPower = storageTag.getFloat(TileEntityMaidBeacon.STORAGE_POWER_TAG);
            }
        }
        tooltip.add(I18n.format("tooltips.touhou_little_maid.maid_beacon.desc", DECIMAL_FORMAT.format(numPower)));
    }
}
