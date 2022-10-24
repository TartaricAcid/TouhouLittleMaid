package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.LayerMaidBackItem;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.LayerMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.LayerMaidBipedHead;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.LayerMaidHeldItem;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.config.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EntityMaidRenderer extends MobRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private MaidModelInfo mainInfo;
    private List<Object> mainAnimations = Lists.newArrayList();

    public EntityMaidRenderer(EntityRendererManager manager) {
        super(manager, new BedrockModel<>(), 0.5f);
        this.addLayer(new LayerMaidHeldItem(this));
        this.addLayer(new LayerMaidBipedHead(this));
        this.addLayer(new LayerMaidBackpack(this));
        this.addLayer(new LayerMaidBackItem(this));
    }

    @Override
    public void render(EntityMaid entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        renderHomeTips(entity, matrixStackIn, bufferIn, packedLightIn);

        // 读取默认模型，用于清除不存在模型的缓存残留
        CustomPackLoader.MAID_MODELS.getModel(DEFAULT_MODEL_ID).ifPresent(model -> this.model = model);
        CustomPackLoader.MAID_MODELS.getInfo(DEFAULT_MODEL_ID).ifPresent(info -> this.mainInfo = info);
        CustomPackLoader.MAID_MODELS.getAnimation(DEFAULT_MODEL_ID).ifPresent(animations -> this.mainAnimations = animations);

        MaidModels.ModelData eventModelData = new MaidModels.ModelData(model, mainInfo, mainAnimations);
        if (MinecraftForge.EVENT_BUS.post(new RenderMaidEvent(entity, eventModelData))) {
            this.model = eventModelData.getModel();
            this.mainInfo = eventModelData.getInfo();
            this.mainAnimations = eventModelData.getAnimations();
        } else {
            // 通过模型 id 获取对应数据
            CustomPackLoader.MAID_MODELS.getModel(entity.getModelId()).ifPresent(model -> this.model = model);
            CustomPackLoader.MAID_MODELS.getInfo(entity.getModelId()).ifPresent(info -> this.mainInfo = info);
            CustomPackLoader.MAID_MODELS.getAnimation(entity.getModelId()).ifPresent(animations -> this.mainAnimations = animations);
        }

        // 模型动画设置
        this.model.setAnimations(this.mainAnimations);
        // 渲染聊天气泡
        if (InGameMaidConfig.INSTANCE.isShowChatBubble()) {
            ChatBubbleRenderer.renderChatBubble(this, entity, matrixStackIn, bufferIn, packedLightIn);
        }
        // 渲染女仆模型本体
        GlWrapper.setMatrixStack(matrixStackIn);
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        GlWrapper.clearMatrixStack();
    }

    private void renderHomeTips(EntityMaid entity, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (entity.hasRestriction() && player != null && extraCondition(entity, mc, player)) {
            BlockPos restrictCenter = entity.getRestrictCenter();
            Vector3f home = new Vector3f(restrictCenter.getX(), restrictCenter.getY(), restrictCenter.getZ());
            home.add(0.5f, 0.25f, 0.5f);
            home.add((float) -entity.getX(), (float) -entity.getY(), (float) -entity.getZ());

            matrixStackIn.pushPose();
            matrixStackIn.translate(home.x(), home.y() + 0.25, home.z());
            matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
            matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f pose = matrixStackIn.last().pose();
            FontRenderer fontrenderer = this.getFont();
            String text = String.format("[%d, %d, %d]", restrictCenter.getX(), restrictCenter.getY(), restrictCenter.getZ());
            float x = (float) (-fontrenderer.width(text) / 2);
            fontrenderer.drawInBatch(text, x, 0, -1, false, pose, bufferIn, false, 0, packedLightIn);
            matrixStackIn.popPose();


            IVertexBuilder buffer = bufferIn.getBuffer(RenderType.lines());
            Matrix4f matrix4f = matrixStackIn.last().pose();
            buffer.vertex(matrix4f, 0, 0.5f, 0).color(0xff, 0x00, 0x00, 0xff).endVertex();
            buffer.vertex(matrix4f, home.x(), home.y(), home.z()).color(0xff, 0x00, 0x00, 0xff).endVertex();
        }
    }

    private boolean extraCondition(EntityMaid entity, Minecraft mc, PlayerEntity player) {
        if (player.getMainHandItem().getItem() == Items.COMPASS) {
            return entity.equals(mc.crosshairPickEntity) || player.isShiftKeyDown();
        }
        return false;
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
