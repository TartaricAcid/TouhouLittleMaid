package com.github.tartaricacid.touhoulittlemaid.compat.mcmp;

import com.github.tartaricacid.touhoulittlemaid.block.BlockGrid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PartGrid implements IMultipart {

    @Override
    public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
        return EnumFaceSlot.fromFace(facing);
    }

    @Override
    public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
        return EnumFaceSlot.fromFace(state.getValue(BlockGrid.DIRECTION).face);
    }

    @Override
    public IMultipartTile convertToMultipartTile(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityGrid) {
            return new PartTileGrid((TileEntityGrid) tileEntity);
        } else {
            return null;
        }
    }

    @Override
    public Block getBlock() {
        return MaidBlocks.GRID;
    }
}
