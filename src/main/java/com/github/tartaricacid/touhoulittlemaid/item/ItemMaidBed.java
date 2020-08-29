package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemMaidBed extends Item {
    public ItemMaidBed() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".maid_bed");
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }

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
        BlockPos offsetPos = pos.offset(playerFacing);
        ItemStack itemstack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos, facing, itemstack) && player.canPlayerEdit(offsetPos, facing, itemstack)) {
            IBlockState offsetBlockState = worldIn.getBlockState(offsetPos);
            boolean posBlockStateIsOkay = replaceable || worldIn.isAirBlock(pos);
            boolean offsetBlockStateIsOkay = offsetBlockState.getBlock().isReplaceable(worldIn, offsetPos) || worldIn.isAirBlock(offsetPos);
            if (posBlockStateIsOkay && offsetBlockStateIsOkay && worldIn.getBlockState(pos.down()).isTopSolid() && worldIn.getBlockState(offsetPos.down()).isTopSolid()) {
                IBlockState maidBedFoot = MaidBlocks.MAID_BED.getDefaultState().withProperty(BlockMaidBed.FACING, playerFacing).withProperty(BlockMaidBed.PART, BlockMaidBed.EnumPartType.FOOT);
                worldIn.setBlockState(pos, maidBedFoot, 10);
                worldIn.setBlockState(offsetPos, maidBedFoot.withProperty(BlockMaidBed.PART, BlockMaidBed.EnumPartType.HEAD), 10);
                SoundType soundtype = maidBedFoot.getBlock().getSoundType(maidBedFoot, worldIn, pos, player);
                worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                worldIn.notifyNeighborsRespectDebug(pos, block, false);
                worldIn.notifyNeighborsRespectDebug(offsetPos, offsetBlockState.getBlock(), false);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}
