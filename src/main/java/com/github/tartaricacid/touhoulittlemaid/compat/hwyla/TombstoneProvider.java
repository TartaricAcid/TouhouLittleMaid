package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityTombstone;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/10/5 20:57
 **/
public class TombstoneProvider implements IWailaDataProvider {
    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityTombstone) {
            TileEntityTombstone tombstone = (TileEntityTombstone) accessor.getTileEntity();
            if (!tombstone.getOwnerName().isEmpty()) {
                tooltip.add(I18n.format("hwyla.touhou_little_maid.tombstone.maid_owner", tombstone.getOwnerName()));
            }
        }
        return tooltip;
    }
}
