package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.*;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EntityMaidRenderer extends MobRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private final GeckoEntityMaidRenderer geckoEntityMaidRenderer;
    private MaidModelInfo mainInfo;
    private List<Object> mainAnimations = Lists.newArrayList();

    public EntityMaidRenderer(EntityRendererManager manager) {
        super(manager, new BedrockModel<>(), 0.5f);
        this.addLayer(new LayerMaidHeldItem(this));
        this.addLayer(new LayerMaidBipedHead(this));
        this.addLayer(new LayerMaidBackpack(this));
        this.addLayer(new LayerMaidBackItem(this));
        this.addLayer(new LayerMaidBanner(this));
        this.geckoEntityMaidRenderer = new GeckoEntityMaidRenderer(manager);
    }

    @Override
    public void render(EntityMaid entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        // 读取默认模型，用于清除不存在模型的缓存残留
        CustomPackLoader.MAID_MODELS.getModel(DEFAULT_MODEL_ID).ifPresent(model -> this.model = model);
        CustomPackLoader.MAID_MODELS.getInfo(DEFAULT_MODEL_ID).ifPresent(info -> this.mainInfo = info);
        CustomPackLoader.MAID_MODELS.getAnimation(DEFAULT_MODEL_ID).ifPresent(animations -> this.mainAnimations = animations);

        MaidModels.ModelData eventModelData = new MaidModels.ModelData(model, mainInfo, mainAnimations);
        if (MinecraftForge.EVENT_BUS.post(new RenderMaidEvent(entity, eventModelData))) {
            BedrockModel<EntityMaid> bedrockModel = eventModelData.getModel();
            if (bedrockModel != null) {
                this.model = bedrockModel;
            }
            this.mainInfo = eventModelData.getInfo();
            this.mainAnimations = eventModelData.getAnimations();
        } else {
            // 通过模型 id 获取对应数据
            CustomPackLoader.MAID_MODELS.getModel(entity.getModelId()).ifPresent(model -> this.model = model);
            CustomPackLoader.MAID_MODELS.getInfo(entity.getModelId()).ifPresent(info -> this.mainInfo = info);
            CustomPackLoader.MAID_MODELS.getAnimation(entity.getModelId()).ifPresent(animations -> this.mainAnimations = animations);
        }

        // 渲染聊天气泡
        if (InGameMaidConfig.INSTANCE.isShowChatBubble()) {
            ChatBubbleRenderer.renderChatBubble(this, entity, matrixStackIn, bufferIn, packedLightIn);
        }

        // GeckoLib 接管渲染
        if (this.mainInfo.isGeckoModel()) {
            this.geckoEntityMaidRenderer.setMainInfo(this.mainInfo);
            this.geckoEntityMaidRenderer.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            return;
        }
        // 模型动画设置
        this.model.setAnimations(this.mainAnimations);
        // 渲染女仆模型本体
        GlWrapper.setMatrixStack(matrixStackIn);
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        GlWrapper.clearMatrixStack();
    }

    @Override
    protected void scale(EntityMaid maid, MatrixStack matrixStackIn, float partialTickTime) {
        float scale = mainInfo.getRenderEntityScale();
        matrixStackIn.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMaid maid) {
        if (mainInfo == null) {
            return DEFAULT_TEXTURE;
        }
        return mainInfo.getTexture();
    }

    public MaidModelInfo getMainInfo() {
        return mainInfo;
    }
}
