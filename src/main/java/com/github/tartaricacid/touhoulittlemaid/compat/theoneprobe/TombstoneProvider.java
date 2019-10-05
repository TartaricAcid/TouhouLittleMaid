package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityTombstone;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/10/5 21:02
 **/
public class TombstoneProvider implements IProbeInfoProvider {
    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileEntityTombstone) {
            TileEntityTombstone tombstone = (TileEntityTombstone) te;
            if (!tombstone.getOwnerName().isEmpty()) {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .text(I18n.format("top.touhou_little_maid.tombstone.maid_owner", tombstone.getOwnerName()));
            }
        }
    }

    @Override
    public String getID() {
        return TouhouLittleMaid.MOD_ID + ":tombstone";
    }
}
