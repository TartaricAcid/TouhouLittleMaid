package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class EntitySitRenderer extends EntityRenderer<EntitySit> {
    private static final ResourceLocation EMPTY = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");

    public EntitySitRenderer(EntityRendererManager context) {
        super(context);
    }

    @Override
    public void render(EntitySit entitySit, float entityYaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySit entitySit) {
        return EMPTY;
    }
}
