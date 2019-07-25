package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/7/25 20:55
 **/
public class EntityMaidProvider implements IProbeInfoEntityProvider {
    @Override
    public void addProbeEntityInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        if (entity instanceof EntityMaid && ((EntityMaid) entity).isTamed()) {
            IMaidTask task = ((EntityMaid) entity).getTask();
            // 因为 McJty 先生有点智力障碍，所以无法支持向左阅读的语言
            String text = IProbeInfo.STARTLOC + "top.touhou_little_maid.entity_maid.task" + IProbeInfo.ENDLOC;
            text += IProbeInfo.STARTLOC + task.getTranslationKey() + IProbeInfo.ENDLOC;
            probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(task.getIcon()).text(text);
        }
    }

    @Override
    public String getID() {
        return TouhouLittleMaid.MOD_ID + ":entity_maid";
    }
}
