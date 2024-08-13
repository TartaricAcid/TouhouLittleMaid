package com.github.tartaricacid.touhoulittlemaid.extended;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityRendererAccessor {
    void tlm$renderNameTag(LivingEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight);
}
