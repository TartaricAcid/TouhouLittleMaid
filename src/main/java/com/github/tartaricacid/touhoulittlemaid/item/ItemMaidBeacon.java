package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/10/8 15:48
 **/
public class ItemMaidBeacon extends Item {
    private Block block;

    public ItemMaidBeacon(Block block) {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".maid_beacon");
        setCreativeTab(MaidItems.TABS);
        this.block = block;
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
                placeBeacon(worldIn, pos, enumfacing, this.block);
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    private static void placeBeacon(World worldIn, BlockPos pos, EnumFacing facing, Block beacon) {
        BlockPos posUp = pos.up();
        IBlockState state;
        if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
            state = beacon.getDefaultState().withProperty(BlockMaidBeacon.POSITION, BlockMaidBeacon.Position.UP_N_S);
        } else {
            state = beacon.getDefaultState().withProperty(BlockMaidBeacon.POSITION, BlockMaidBeacon.Position.UP_W_E);
        }
        worldIn.setBlockState(pos, beacon.getDefaultState(), 2);
        worldIn.setBlockState(posUp, state, 2);
        worldIn.notifyNeighborsOfStateChange(pos, beacon, false);
        worldIn.notifyNeighborsOfStateChange(posUp, beacon, false);
    }
}
