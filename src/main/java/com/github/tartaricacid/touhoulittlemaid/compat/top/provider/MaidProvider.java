package com.github.tartaricacid.touhoulittlemaid.compat.top.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcjty.theoneprobe.api.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;

public class MaidProvider implements IProbeInfoEntityProvider {
    private static final String ID = (ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "maid")).toString();

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player playerEntity, Level world, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (entity instanceof EntityMaid maid) {
            if (maid.isTame()) {
                IMaidTask task = maid.getTask();
                MutableComponent taskTitle = Component.translatable("top.touhou_little_maid.entity_maid.task").append(task.getName());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(taskTitle);

                MutableComponent scheduleTitle = Component.translatable("top.touhou_little_maid.entity_maid.schedule").append(getActivityTransText(maid));
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(scheduleTitle);

                MutableComponent favorabilityTitle = Component.translatable("top.touhou_little_maid.entity_maid.favorability", maid.getFavorabilityManager().getLevel());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(favorabilityTitle);

                MutableComponent nextFavorabilityPointTitle = Component.translatable("top.touhou_little_maid.entity_maid.nex_favorability_point", maid.getFavorabilityManager().nextLevelPoint());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(nextFavorabilityPointTitle);
            }
            if (maid.getIsInvulnerable()) {
                MutableComponent text = Component.translatable("top.touhou_little_maid.entity_maid.invulnerable").withStyle(ChatFormatting.DARK_PURPLE);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(text);
            }
        }
    }

    @Override
    public String getID() {
        return ID;
    }

    private MutableComponent getActivityTransText(EntityMaid maid) {
        return getActivityTransText(maid.getScheduleDetail());
    }

    private MutableComponent getActivityTransText(Activity activity) {
        return Component.translatable("gui.touhou_little_maid.activity." + activity.getName());
    }
}
