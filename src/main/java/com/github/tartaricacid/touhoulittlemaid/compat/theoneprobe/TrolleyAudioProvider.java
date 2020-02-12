package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTrolleyAudio;
import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author TartaricAcid
 * @date 2020/2/3 0:12
 **/
public class TrolleyAudioProvider implements IProbeInfoEntityProvider {
    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityTrolleyAudio) {
            EntityTrolleyAudio trolleyAudio = (EntityTrolleyAudio) entity;
            IItemHandler handler = trolleyAudio.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (handler != null) {
                ItemStack stack = handler.getStackInSlot(0);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemRecord) {
                    ItemRecord record = (ItemRecord) stack.getItem();
                    String text = IProbeInfo.STARTLOC + record.displayName + IProbeInfo.ENDLOC;
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(stack).text(text);
                }
            }
        }
    }

    @Override
    public String getID() {
        return TouhouLittleMaid.MOD_ID + ":trolley_audio";
    }
}
