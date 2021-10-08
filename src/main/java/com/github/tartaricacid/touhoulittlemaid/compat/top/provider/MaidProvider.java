package com.github.tartaricacid.touhoulittlemaid.compat.top.provider;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
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
                TranslationTextComponent taskTitle = new TranslationTextComponent("top.touhou_little_maid.entity_maid.task");
                TranslationTextComponent taskName = task.getName();
                taskTitle.append(taskName);
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .item(task.getIcon()).text(taskTitle);
            }
            if (maid.getIsInvulnerable()) {
                TranslationTextComponent text = new TranslationTextComponent("top.touhou_little_maid.entity_maid.invulnerable");
                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                        .text(text.withStyle(TextFormatting.DARK_PURPLE));
            }
        }
    }

    @Override
    public String getID() {
        return ID;
    }
}
