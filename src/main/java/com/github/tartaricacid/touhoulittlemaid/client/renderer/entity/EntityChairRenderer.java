package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import java.util.List;

public class EntityChairRenderer extends LivingRenderer<EntityChair, BedrockModel<EntityChair>> {
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final String DEFAULT_CHAIR_ID = "touhou_little_maid:cushion";
    private ChairModelInfo mainInfo;
    private List<Object> mainAnimations;
    ;

    public EntityChairRenderer(EntityRendererManager rendererManager, BedrockModel<EntityChair> entityModelIn, float shadowSizeIn) {
        super(rendererManager, entityModelIn, shadowSizeIn);
    }

    @Override
    public void render(EntityChair chair, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        // 读取默认模型，用于清除不存在模型的缓存残留
        CustomPackLoader.CHAIR_MODEL.getModel(DEFAULT_CHAIR_ID).ifPresent(model -> this.model = model);
        CustomPackLoader.CHAIR_MODEL.getInfo(DEFAULT_CHAIR_ID).ifPresent(info -> this.mainInfo = info);
        this.mainAnimations = null;

        // 通过模型 id 获取对应数据
        CustomPackLoader.CHAIR_MODEL.getModel(chair.getModelId()).ifPresent(model -> this.model = model);
        CustomPackLoader.CHAIR_MODEL.getInfo(chair.getModelId()).ifPresent(info -> this.mainInfo = info);
        CustomPackLoader.CHAIR_MODEL.getAnimation(chair.getModelId()).ifPresent(animations -> this.mainAnimations = animations);

        // 模型动画设置
        this.model.setAnimations(this.mainAnimations);

        GlWrapper.setMatrixStack(matrixStackIn);
        super.render(chair, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        GlWrapper.clearMatrixStack();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityChair entity) {
        if (mainInfo == null) {
            return DEFAULT_TEXTURE;
        }
        return mainInfo.getTexture();
    }

    @Override
    protected void setupRotations(EntityChair chair, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStackIn.mulPose(Vector3f.YP.rotation(180 - chair.yRot));
    }

    @Override
    protected boolean shouldShowName(EntityChair entity) {
        return entity.shouldShowName();
    }
}
