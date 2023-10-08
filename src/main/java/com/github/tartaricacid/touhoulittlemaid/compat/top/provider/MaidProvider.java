package com.github.tartaricacid.touhoulittlemaid.compat.top.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
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
    private static final String ID = (new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid")).toString();

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player playerEntity, Level world, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (entity instanceof EntityMaid maid) {
            if (maid.isTame()) {
                IMaidTask task = maid.getTask();
                MutableComponent taskTitle = Component.translatable("top.touhou_little_maid.entity_maid.task").append(task.getName());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(taskTitle);

                MutableComponent scheduleTitle = Component.translatable("top.touhou_little_maid.entity_maid.schedule").append(getActivityTransText(maid));
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(scheduleTitle);
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
        MaidSchedule schedule = maid.getSchedule();
        int time = (int) (maid.level().getDayTime() % 24000L);
        switch (schedule) {
            case ALL -> {
                return getActivityTransText(Activity.WORK);
            }
            case NIGHT -> {
                return getActivityTransText(InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get().getActivityAt(time));
            }
            default -> {
                return getActivityTransText(InitEntities.MAID_DAY_SHIFT_SCHEDULES.get().getActivityAt(time));
            }
        }
    }

    private MutableComponent getActivityTransText(Activity activity) {
        return Component.translatable("gui.touhou_little_maid.activity." + activity.getName());
    }
}
