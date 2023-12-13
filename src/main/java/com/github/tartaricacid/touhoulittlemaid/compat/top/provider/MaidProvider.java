package com.github.tartaricacid.touhoulittlemaid.compat.top.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class MaidProvider implements IProbeInfoEntityProvider {
    private static final String ID = (new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid")).toString();

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity playerEntity, World world, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            if (maid.isTame()) {
                IMaidTask task = maid.getTask();
                ITextComponent taskTitle = new TranslationTextComponent("top.touhou_little_maid.entity_maid.task").append(task.getName());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(taskTitle);

                ITextComponent scheduleTitle = new TranslationTextComponent("top.touhou_little_maid.entity_maid.schedule").append(getActivityTransText(maid));
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(scheduleTitle);

                ITextComponent favorabilityTitle = new TranslationTextComponent("top.touhou_little_maid.entity_maid.favorability", maid.getFavorabilityManager().getLevel());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(favorabilityTitle);

                ITextComponent nextFavorabilityPointTitle = new TranslationTextComponent("top.touhou_little_maid.entity_maid.nex_favorability_point", maid.getFavorabilityManager().nextLevelPoint());
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(nextFavorabilityPointTitle);
            }
            if (maid.getIsInvulnerable()) {
                ITextComponent text = new TranslationTextComponent("top.touhou_little_maid.entity_maid.invulnerable").withStyle(TextFormatting.DARK_PURPLE);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).text(text);
            }
        }
    }

    @Override
    public String getID() {
        return ID;
    }

    private ITextComponent getActivityTransText(EntityMaid maid) {
        MaidSchedule schedule = maid.getSchedule();
        int time = (int) (maid.level.getDayTime() % 24000L);
        switch (schedule) {
            case ALL:
                return getActivityTransText(Activity.WORK);
            case NIGHT:
                return getActivityTransText(InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get().getActivityAt(time));
            default:
                return getActivityTransText(InitEntities.MAID_DAY_SHIFT_SCHEDULES.get().getActivityAt(time));
        }
    }

    private ITextComponent getActivityTransText(Activity activity) {
        return new TranslationTextComponent("gui.touhou_little_maid.activity." + activity.getName());
    }
}