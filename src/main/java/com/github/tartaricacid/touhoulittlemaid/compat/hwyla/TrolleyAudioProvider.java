package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTrolleyAudio;
import mcp.mobius.waila.addons.minecraft.HUDHandlerVillager;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2020/2/3 0:06
 **/
public class TrolleyAudioProvider implements IWailaEntityProvider {
    static IWailaEntityProvider INSTANCE = new TrolleyAudioProvider();

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (entity instanceof EntityTrolleyAudio) {
            String name = accessor.getNBTData().getString("RecordName");
            if (StringUtils.isNotBlank(name)) {
                currenttip.add(I18n.format(name));
            }
        }
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        if (ent instanceof EntityTrolleyAudio) {
            EntityTrolleyAudio trolleyAudio = (EntityTrolleyAudio) ent;
            IItemHandler handler = trolleyAudio.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (handler != null) {
                ItemStack stack = handler.getStackInSlot(0);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemRecord) {
                    ItemRecord record = (ItemRecord) stack.getItem();
                    tag.setString("RecordName", record.displayName);
                }
            }
        }
        return tag;
    }
}
