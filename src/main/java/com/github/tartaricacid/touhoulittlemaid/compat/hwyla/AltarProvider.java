package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/9/4 23:09
 **/
public class AltarProvider implements IWailaDataProvider {
    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityAltar) {
            TileEntityAltar altar = (TileEntityAltar) accessor.getTileEntity();
            Block block = altar.getBlockState().getBlock();
            return new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(altar.getBlockState()));
        }
        return ItemStack.EMPTY;
    }
}
