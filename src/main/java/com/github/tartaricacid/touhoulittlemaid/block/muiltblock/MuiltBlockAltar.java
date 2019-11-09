package com.github.tartaricacid.touhoulittlemaid.block.muiltblock;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/11/9 12:40
 **/
public class MuiltBlockAltar implements IMultiBlock {
    private static final ResourceLocation ALTAR_SOUTH = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_south");
    private static final ResourceLocation ALTAR_NORTH = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_north");
    private static final ResourceLocation ALTAR_EAST = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_east");
    private static final ResourceLocation ALTAR_WEST = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_west");
    private static final BlockPos SOUTH_POS = new BlockPos(-4, -3, 0);
    private static final BlockPos NORTH_POS = new BlockPos(-3, -3, -7);
    private static final BlockPos EAST_POS = new BlockPos(0, -3, -3);
    private static final BlockPos WEST_POS = new BlockPos(-7, -3, -4);

    @Override
    public boolean blockIsSuitable(IBlockState blockState) {
        return blockState.getBlock() == Blocks.WOOL && Blocks.WOOL.getMetaFromState(blockState) == EnumDyeColor.RED.getMetadata();
    }

    @Override
    public boolean isMatch(World worldIn, BlockPos posStart, EnumFacing facing, Template template) {
        for (Template.BlockInfo blockInfo : template.blocks) {
            if (!worldIn.getBlockState(posStart.add(blockInfo.pos)).equals(blockInfo.blockState)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void build(World worldIn, BlockPos posStart, EnumFacing facing, Template template) {
        List<BlockPos> posList = Lists.newArrayList();
        List<BlockPos> canPlaceItemPosList = Lists.newArrayList();
        for (Template.BlockInfo blockInfo : template.blocks) {
            posList.add(posStart.add(blockInfo.pos));
            if (blockInfo.pos.getY() == 2 && blockInfo.blockState.equals(Blocks.LOG.getDefaultState())) {
                canPlaceItemPosList.add(posStart.add(blockInfo.pos));
            }
        }
        for (Template.BlockInfo blockInfo : template.blocks) {
            BlockPos currentPos = posStart.add(blockInfo.pos);
            worldIn.setBlockState(currentPos, MaidBlocks.ALTAR.getDefaultState());
            TileEntity altarTileEntity = worldIn.getTileEntity(currentPos);
            if (altarTileEntity instanceof TileEntityAltar) {
                if (currentPos.equals(posStart.subtract(getCenterPos(facing)))) {
                    ((TileEntityAltar) altarTileEntity).setForgeData(blockInfo.blockState, true,
                            false, facing, posList, canPlaceItemPosList);
                } else if (blockInfo.pos.getY() == 2 && blockInfo.blockState.equals(Blocks.LOG.getDefaultState())) {
                    ((TileEntityAltar) altarTileEntity).setForgeData(blockInfo.blockState, false,
                            true, facing, posList, canPlaceItemPosList);
                } else {
                    ((TileEntityAltar) altarTileEntity).setForgeData(blockInfo.blockState, false,
                            false, facing, posList, canPlaceItemPosList);
                }
            }
        }
    }

    @Override
    public boolean facingIsSuitable(EnumFacing facing) {
        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
    }

    @Override
    public BlockPos getCenterPos(EnumFacing facing) {
        switch (facing) {
            case SOUTH:
                return NORTH_POS;
            case NORTH:
                return SOUTH_POS;
            case EAST:
                return WEST_POS;
            case WEST:
                return EAST_POS;
            default:
                return NORTH_POS;
        }
    }

    @Override
    public Template getTemplate(World worldIn, EnumFacing facing) {
        switch (facing) {
            case SOUTH:
                return getAltarTemplate(worldIn, ALTAR_NORTH);
            case NORTH:
                return getAltarTemplate(worldIn, ALTAR_SOUTH);
            case EAST:
                return getAltarTemplate(worldIn, ALTAR_WEST);
            case WEST:
                return getAltarTemplate(worldIn, ALTAR_EAST);
            default:
                return getAltarTemplate(worldIn, ALTAR_NORTH);
        }
    }

    private Template getAltarTemplate(World worldIn, ResourceLocation resourceLocation) {
        return worldIn.getSaveHandler().getStructureTemplateManager().getTemplate(worldIn.getMinecraftServer(), resourceLocation);
    }
}
