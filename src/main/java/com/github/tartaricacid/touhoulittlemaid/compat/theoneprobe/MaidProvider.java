package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/7/25 20:55
 **/
public class MaidProvider implements IProbeInfoEntityProvider {
    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            if (maid.isTamed()) {
                IMaidTask task = maid.getTask();
                String text = IProbeInfo.STARTLOC + "top.touhou_little_maid.entity_maid.task" + IProbeInfo.ENDLOC;
                text += IProbeInfo.STARTLOC + task.getTranslationKey() + IProbeInfo.ENDLOC;
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(task.getIcon()).text(text);

                String favText = IProbeInfo.STARTLOC + "top.touhou_little_maid.entity_maid.favorability" + IProbeInfo.ENDLOC;
                favText += maid.getFavorability();
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(favText);
            }
            if (maid.hasSasimono()) {
                String text = IProbeInfo.STARTLOC + "top.touhou_little_maid.entity_maid.has_sasimono" + IProbeInfo.ENDLOC;
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(new ItemStack(MaidItems.HATA_SASIMONO)).text(text);
            }
            if (maid.getIsInvulnerable()) {
                String text = IProbeInfo.STARTLOC + "top.touhou_little_maid.entity_maid.invulnerable" + IProbeInfo.ENDLOC;
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(text);
            }
        }
    }

    @Override
    public String getID() {
        return TouhouLittleMaid.MOD_ID + ":entity_maid";
    }
}
