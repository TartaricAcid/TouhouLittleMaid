package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityBoxModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.IntStream;

public class EntityBoxRender extends EntityRenderer<EntityBox> {
    private final List<ResourceLocation> texturesGroup = Lists.newArrayList();
    private final EntityModel<EntityBox> boxModel;

    public EntityBoxRender(EntityRendererProvider.Context manager) {
        super(manager);
        boxModel = new EntityBoxModel(manager.bakeLayer(EntityBoxModel.LAYER));
        IntStream.range(0, EntityBox.MAX_TEXTURE_SIZE).forEach(this::addBoxTexture);
    }

    @Override
    public void render(EntityBox entityBox, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0, -1.501, 0.0);
        boxModel.setupAnim(entityBox, 0, 0, -0.1f, 0, 0);
        RenderType renderType = RenderType.entityCutoutNoCull(getTextureLocation(entityBox));
        VertexConsumer buffer = bufferIn.getBuffer(renderType);
        boxModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBox entity) {
        return texturesGroup.get(entity.getTextureIndex());
    }

    private void addBoxTexture(int index) {
        String fileName = String.format("textures/entity/box/cake_box_%s.png", index);
        texturesGroup.add(new ResourceLocation(TouhouLittleMaid.MOD_ID, fileName));
    }
}
