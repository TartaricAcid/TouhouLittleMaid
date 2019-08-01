package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/8/1 14:40
 **/
public class TileEntityGridProvider implements IProbeInfoProvider {
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileEntityGrid) {
            TileEntityGrid grid = (TileEntityGrid) te;
            probeInfo.text(String.format("%smessage.%s.grid.blacklist.%s%s", IProbeInfo.STARTLOC, TouhouLittleMaid.MOD_ID, grid.blacklist, IProbeInfo.ENDLOC));
            probeInfo.text(String.format("%smessage.%s.grid.input.%s%s", IProbeInfo.STARTLOC, TouhouLittleMaid.MOD_ID, grid.input, IProbeInfo.ENDLOC));
        }
    }

    @Override
    public String getID() {
        return TouhouLittleMaid.MOD_ID + ":block_grid";
    }
}
