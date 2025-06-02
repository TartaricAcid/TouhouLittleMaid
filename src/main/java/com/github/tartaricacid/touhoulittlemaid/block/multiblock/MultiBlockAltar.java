package com.github.tartaricacid.touhoulittlemaid.block.multiblock;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.block.IMultiBlock;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.util.PosListData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class MultiBlockAltar implements IMultiBlock {
    private static final ResourceLocation ALTAR_SOUTH = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar_south");
    private static final ResourceLocation ALTAR_NORTH = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar_north");
    private static final ResourceLocation ALTAR_EAST = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar_east");
    private static final ResourceLocation ALTAR_WEST = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar_west");
    private static final BlockPos SOUTH_POS = new BlockPos(-4, -3, 0);
    private static final BlockPos NORTH_POS = new BlockPos(-3, -3, -7);
    private static final BlockPos EAST_POS = new BlockPos(0, -3, -3);
    private static final BlockPos WEST_POS = new BlockPos(-7, -3, -4);

    @Override
    public boolean isCoreBlock(BlockState blockState) {
        return blockState.is(TagBlock.ALTAR_TORII);
    }

    @Override
    public boolean isMatch(Level world, BlockPos posStart, Direction direction, StructureTemplate template) {
        StructureTemplate.Palette palette = template.palettes.getFirst();
        for (StructureTemplate.StructureBlockInfo blockInfo : palette.blocks()) {
            BlockState worldState = world.getBlockState(posStart.offset(blockInfo.pos()));
            BlockState infoState = blockInfo.state();
            // 橡木部分 -> 御柱
            if (infoState.is(Blocks.OAK_LOG)) {
                if (!worldState.is(TagBlock.ALTAR_PILLAR)) {
                    return false;
                }
            }
            // 羊毛部分 -> 鸟居
            else if (infoState.is(Blocks.RED_WOOL)) {
                if (!worldState.is(TagBlock.ALTAR_TORII)) {
                    return false;
                }
            }
            // 其他情况照常检查
            else if (!worldState.equals(infoState)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void build(Level worldIn, BlockPos posStart, Direction direction, StructureTemplate template) {
        PosListData posList = new PosListData();
        PosListData canPlaceItemPosList = new PosListData();
        StructureTemplate.Palette palette = template.palettes.getFirst();

        for (StructureTemplate.StructureBlockInfo blockInfo : palette.blocks()) {
            posList.add(posStart.offset(blockInfo.pos()));
            if (blockInfo.pos().getY() == 2 && blockInfo.state().is(Blocks.OAK_LOG)) {
                canPlaceItemPosList.add(posStart.offset(blockInfo.pos()));
            }
        }

        BlockPos currentCenterPos = posStart.subtract(getCenterPos(direction));
        for (StructureTemplate.StructureBlockInfo blockInfo : palette.blocks()) {
            BlockPos currentPos = posStart.offset(blockInfo.pos());
            BlockState currentState = worldIn.getBlockState(currentPos);
            worldIn.setBlock(currentPos, InitBlocks.ALTAR.get().defaultBlockState(), Block.UPDATE_ALL);
            BlockEntity te = worldIn.getBlockEntity(currentPos);
            if (te instanceof TileEntityAltar tileEntityAltar) {
                // 设置核心数据，核心方块渲染
                boolean isRender = currentPos.equals(currentCenterPos);
                boolean canPlaceItem = blockInfo.pos().getY() == 2 && blockInfo.state().is(BlockTags.LOGS);
                if (isRender) {
                    tileEntityAltar.setCoreData(currentState,
                            canPlaceItem, direction, posList, canPlaceItemPosList);
                } else {
                    // 设置外围数据，外围方块不渲染
                    tileEntityAltar.setOuterData(currentState, canPlaceItem, currentCenterPos);
                }
            }
        }
    }

    @Override
    public boolean directionIsSuitable(Direction direction) {
        return direction != Direction.DOWN && direction != Direction.UP;
    }

    @Override
    public BlockPos getCenterPos(Direction facing) {
        return switch (facing) {
            case NORTH -> SOUTH_POS;
            case EAST -> WEST_POS;
            case WEST -> EAST_POS;
            default -> NORTH_POS;
        };
    }

    @Override
    public StructureTemplate getTemplate(ServerLevel world, Direction facing) {
        return switch (facing) {
            case NORTH -> getAltarTemplate(world, ALTAR_SOUTH);
            case EAST -> getAltarTemplate(world, ALTAR_WEST);
            case WEST -> getAltarTemplate(world, ALTAR_EAST);
            default -> getAltarTemplate(world, ALTAR_NORTH);
        };
    }

    private StructureTemplate getAltarTemplate(ServerLevel world, ResourceLocation location) {
        return world.getStructureManager().getOrCreate(location);
    }
}
