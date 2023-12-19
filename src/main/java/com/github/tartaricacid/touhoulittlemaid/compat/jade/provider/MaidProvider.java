package com.github.tartaricacid.touhoulittlemaid.compat.jade.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import mcp.mobius.waila.api.EntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.schedule.Activity;

public class MaidProvider implements IEntityComponentProvider {
    private static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid");

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getEntity() instanceof EntityMaid maid) {
            if (maid.isTame()) {
                IMaidTask task = maid.getTask();
                iTooltip.add(new TranslatableComponent("top.touhou_little_maid.entity_maid.task").append(task.getName()));
                iTooltip.add(new TranslatableComponent("top.touhou_little_maid.entity_maid.schedule").append(getActivityTransText(maid)));
                iTooltip.add(new TranslatableComponent("top.touhou_little_maid.entity_maid.favorability", maid.getFavorabilityManager().getLevel()));
                iTooltip.add(new TranslatableComponent("top.touhou_little_maid.entity_maid.nex_favorability_point", maid.getFavorabilityManager().nextLevelPoint()));
                if (maid.getIsInvulnerable()) {
                    iTooltip.add(new TranslatableComponent("top.touhou_little_maid.entity_maid.invulnerable").withStyle(ChatFormatting.DARK_PURPLE));
                }
            }
        }
    }

    private MutableComponent getActivityTransText(EntityMaid maid) {
        MaidSchedule schedule = maid.getSchedule();
        int time = (int) (maid.level.getDayTime() % 24000L);
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
        return new TranslatableComponent("gui.touhou_little_maid.activity." + activity.getName());
    }
}