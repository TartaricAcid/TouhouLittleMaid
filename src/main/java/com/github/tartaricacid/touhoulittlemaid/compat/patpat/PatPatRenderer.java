package com.github.tartaricacid.touhoulittlemaid.compat.patpat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.lopymine.patpat.client.render.PatPatClientRenderer;
import net.minecraft.world.entity.LivingEntity;

public class PatPatRenderer {
    static void scaleEntityIfPatted(LivingEntity livingEntity, PoseStack matrixStack, float tickDelta) {
        PatPatClientRenderer.scaleEntityIfPatted(livingEntity, matrixStack, tickDelta);
    }
}
