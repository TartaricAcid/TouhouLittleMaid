package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemChisel extends Item {
    public ItemChisel() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".chisel");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        setMaxDamage(128);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos,
                                      @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getBlockState(pos).getBlock() == Blocks.CLAY && hand == EnumHand.MAIN_HAND
                && player.getHeldItemOffhand().getItem() == MaidItems.PHOTO) {
            TileEntityStatue.Size[] sizes = TileEntityStatue.Size.values();
            String modeId = ItemPhoto.getMaidNbtData(player.getHeldItemOffhand()).getString("ModelId");
            for (int i = sizes.length - 1; i >= 0; i--) {
                TileEntityStatue.Size size = sizes[i];
                Vec3i dimension = size.getDimension();
                BlockPos[] posList = checkBlocks(worldIn, pos, dimension, facing);
                if (posList != null) {
                    for (BlockPos posIn : posList) {
                        worldIn.setBlockState(posIn, MaidBlocks.STATUE.getDefaultState());
                        TileEntity te = worldIn.getTileEntity(posIn);
                        if (te instanceof TileEntityStatue) {
                            TileEntityStatue statue = (TileEntityStatue) te;
                            statue.setForgeData(size, posIn == pos, pos, facing, Lists.newArrayList(posList),
                                    modeId);
                            player.getHeldItemMainhand().damageItem(size.ordinal() + 1, player);
                        }
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Nullable
    private BlockPos[] checkBlocks(@Nonnull World worldIn, BlockPos origin, Vec3i dimension, EnumFacing facing) {
        BlockPos[] posList = new BlockPos[dimension.getX() * dimension.getY() * dimension.getZ()];
        int index = 0;
        for (int x = 0; x < dimension.getX(); x++) {
            for (int y = 0; y < dimension.getY(); y++) {
                for (int z = 0; z < dimension.getZ(); z++) {
                    BlockPos pos;
                    switch (facing) {
                        case WEST:
                            pos = origin.add(new Vec3i(x, y, z));
                            break;
                        case SOUTH:
                            pos = origin.add(new Vec3i(x, y, -z));
                            break;
                        default:
                        case UP:
                        case DOWN:
                        case NORTH:
                            pos = origin.add(new Vec3i(-x, y, z));
                            break;
                        case EAST:
                            pos = origin.add(new Vec3i(-x, y, -z));
                            break;
                    }
                    posList[index] = pos;
                    index++;
                    if (worldIn.getBlockState(pos).getBlock() != Blocks.CLAY) {
                        return null;
                    }
                }
            }
        }
        return posList;
    }
}
