package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractModel<E extends Entity> extends EntityModel<E> {
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
        this.renderToBuffer(poseStack, buffer, light, overlay);
    }
}
