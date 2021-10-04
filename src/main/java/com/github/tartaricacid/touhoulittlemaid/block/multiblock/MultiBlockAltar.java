package com.github.tartaricacid.touhoulittlemaid.block.multiblock;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.util.PosListData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

public class MultiBlockAltar implements IMultiBlock {
    private static final ResourceLocation ALTAR_SOUTH = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_south");
    private static final ResourceLocation ALTAR_NORTH = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_north");
    private static final ResourceLocation ALTAR_EAST = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_east");
    private static final ResourceLocation ALTAR_WEST = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar_west");
    private static final BlockPos SOUTH_POS = new BlockPos(-4, -3, 0);
    private static final BlockPos NORTH_POS = new BlockPos(-3, -3, -7);
    private static final BlockPos EAST_POS = new BlockPos(0, -3, -3);
    private static final BlockPos WEST_POS = new BlockPos(-7, -3, -4);

    @Override
    public boolean isCoreBlock(BlockState blockState) {
        return blockState.is(Blocks.RED_WOOL);
    }

    @Override
    public boolean isMatch(World world, BlockPos posStart, Direction direction, Template template) {
        Template.Palette palette = template.palettes.get(0);
        for (Template.BlockInfo blockInfo : palette.blocks()) {
            if (!world.getBlockState(posStart.offset(blockInfo.pos)).equals(blockInfo.state)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void build(World worldIn, BlockPos posStart, Direction direction, Template template) {
        PosListData posList = new PosListData();
        PosListData canPlaceItemPosList = new PosListData();
        Template.Palette palette = template.palettes.get(0);

        for (Template.BlockInfo blockInfo : palette.blocks()) {
            posList.add(posStart.offset(blockInfo.pos));
            if (blockInfo.pos.getY() == 2 && blockInfo.state.is(Blocks.OAK_LOG)) {
                canPlaceItemPosList.add(posStart.offset(blockInfo.pos));
            }
        }

        BlockPos currentCenterPos = posStart.subtract(getCenterPos(direction));
        for (Template.BlockInfo blockInfo : palette.blocks()) {
            BlockPos currentPos = posStart.offset(blockInfo.pos);
            worldIn.setBlock(currentPos, InitBlocks.ALTAR.get().defaultBlockState(), Constants.BlockFlags.DEFAULT);
            TileEntity te = worldIn.getBlockEntity(currentPos);
            if (te instanceof TileEntityAltar) {
                boolean isRender = currentPos.equals(currentCenterPos);
                boolean canPlaceItem = blockInfo.pos.getY() == 2 && blockInfo.state.is(Blocks.OAK_LOG);
                ((TileEntityAltar) te).setForgeData(blockInfo.state, isRender,
                        canPlaceItem, direction, posList, canPlaceItemPosList);
            }
        }
    }

    @Override
    public boolean directionIsSuitable(Direction direction) {
        return direction != Direction.DOWN && direction != Direction.UP;
    }

    @Override
    public BlockPos getCenterPos(Direction facing) {
        switch (facing) {
            case NORTH:
                return SOUTH_POS;
            case EAST:
                return WEST_POS;
            case WEST:
                return EAST_POS;
            case SOUTH:
            default:
                return NORTH_POS;
        }
    }

    @Override
    public Template getTemplate(ServerWorld world, Direction facing) {
        switch (facing) {
            case NORTH:
                return getAltarTemplate(world, ALTAR_SOUTH);
            case EAST:
                return getAltarTemplate(world, ALTAR_WEST);
            case WEST:
                return getAltarTemplate(world, ALTAR_EAST);
            case SOUTH:
            default:
                return getAltarTemplate(world, ALTAR_NORTH);
        }
    }

    private Template getAltarTemplate(ServerWorld world, ResourceLocation location) {
        return world.getStructureManager().getOrCreate(location);
    }
}
