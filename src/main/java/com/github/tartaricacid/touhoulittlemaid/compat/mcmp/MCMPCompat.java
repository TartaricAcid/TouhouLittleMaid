package com.github.tartaricacid.touhoulittlemaid.compat.mcmp;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.addon.MCMPAddon;
import mcmultipart.api.container.IMultipartContainerBlock;
import mcmultipart.api.multipart.IMultipartRegistry;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumFaceSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

@MCMPAddon
public class MCMPCompat implements IMCMPAddon {
    public static void getPartTiles(World world, BlockPos pos, IBlockState state, List<TileEntityGrid> grids) {
        if (state.getBlock() instanceof IMultipartContainerBlock) {
            for (EnumFaceSlot slot : EnumFaceSlot.VALUES) {
                Optional<IMultipartTile> result = MultipartHelper.getPartTile(world, pos, slot);
                if (result.isPresent() && result.get().getTileEntity() instanceof TileEntityGrid) {
                    grids.add((TileEntityGrid) result.get().getTileEntity());
                }
            }
        }
    }

    @Override
    public void registerParts(IMultipartRegistry registry) {
        TouhouLittleMaid.MCMPCompat = true;
        registry.registerPartWrapper(MaidBlocks.GRID, new PartGrid());
        registry.registerStackWrapper(MaidBlocks.GRID);
    }
}
