package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

/**
 * @author TartaricAcid
 * @date 2019/9/4 23:21
 **/
public class AltarProvider implements IBlockDisplayOverride {
    @Override
    public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof TileEntityAltar) {
            TileEntityAltar altar = (TileEntityAltar) te;
            Block block = altar.getBlockState().getBlock();
            ItemStack stack = new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(altar.getBlockState()));
            if (Tools.show(mode, Config.getRealConfig().getShowModName())) {
                probeInfo.horizontal().item(stack).vertical().itemLabel(stack)
                        .text(MODNAME + Tools.getModName(block));
            } else {
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(stack).itemLabel(stack);
            }
            return true;
        }
        return false;
    }
}
