package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/1 14:33
 **/
public class TileEntityGridProvider implements IWailaDataProvider {
    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityGrid) {
            TileEntityGrid grid = (TileEntityGrid) accessor.getTileEntity();
            tooltip.add(I18n.format(String.format("message.%s.grid.blacklist.%s", TouhouLittleMaid.MOD_ID, grid.blacklist)));
            tooltip.add(I18n.format(String.format("message.%s.grid.input.%s", TouhouLittleMaid.MOD_ID, grid.input)));
        }
        return tooltip;
    }
}
