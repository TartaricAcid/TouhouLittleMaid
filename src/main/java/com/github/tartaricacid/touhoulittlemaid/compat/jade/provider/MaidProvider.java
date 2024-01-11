package com.github.tartaricacid.touhoulittlemaid.compat.jade.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class MaidProvider implements IEntityComponentProvider {
    private static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid");

    @Override
    public void appendTail(List<ITextComponent> iTooltip, IEntityAccessor entityAccessor, IPluginConfig config) {
        if (entityAccessor.getEntity() instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entityAccessor.getEntity();
            if (maid.isTame()) {
                IMaidTask task = maid.getTask();
                iTooltip.add(new TranslationTextComponent("top.touhou_little_maid.entity_maid.task").append(task.getName()));
                iTooltip.add(new TranslationTextComponent("top.touhou_little_maid.entity_maid.schedule").append(getActivityTransText(maid)));
                iTooltip.add(new TranslationTextComponent("top.touhou_little_maid.entity_maid.favorability", maid.getFavorabilityManager().getLevel()));
                iTooltip.add(new TranslationTextComponent("top.touhou_little_maid.entity_maid.nex_favorability_point", maid.getFavorabilityManager().nextLevelPoint()));
                if (maid.getIsInvulnerable()) {
                    iTooltip.add(new TranslationTextComponent("top.touhou_little_maid.entity_maid.invulnerable").withStyle(TextFormatting.DARK_PURPLE));
                }
            }
        }
    }

    private ITextComponent getActivityTransText(EntityMaid maid) {
        return getActivityTransText(maid.getScheduleDetail());
    }

    private ITextComponent getActivityTransText(Activity activity) {
        return new TranslationTextComponent("gui.touhou_little_maid.activity." + activity.getName());
    }
}