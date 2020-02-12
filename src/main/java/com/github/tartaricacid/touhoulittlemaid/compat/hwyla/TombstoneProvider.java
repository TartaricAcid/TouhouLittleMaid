package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityTombstone;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/10/5 20:57
 **/
public class TombstoneProvider implements IWailaDataProvider {
    static IWailaDataProvider INSTANCE = new TombstoneProvider();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityTombstone) {
            String owner = accessor.getNBTData().getString(TileEntityTombstone.OWNER_TAG_NAME);
            if (!StringUtils.isNotBlank(owner)) {
                tooltip.add(I18n.format("hwyla.touhou_little_maid.tombstone.maid_owner", owner));
            }
        }
        return tooltip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileEntityTombstone) {
            tag.setString(TileEntityTombstone.OWNER_TAG_NAME, te.getTileData().getString(TileEntityTombstone.OWNER_TAG_NAME));
        }
        return tag;
    }
}
