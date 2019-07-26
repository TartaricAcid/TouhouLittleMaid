package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.client.resources.I18n;
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
        if (entity instanceof EntityMaid) {
            IMaidTask task = ((EntityMaid) entity).getTask();
            probeInfo.horizontal().item(task.getIcon()).text(I18n.format("hwyla_top.touhou_little_maid.entity_maid.task", task.getTaskI18n()));
        }
    }

    @Override
    public String getID() {
        return TouhouLittleMaid.MOD_ID + ":entity_maid";
    }
}
