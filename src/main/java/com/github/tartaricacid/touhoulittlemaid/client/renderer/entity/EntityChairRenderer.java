package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class EntityChairRenderer extends LivingEntityRenderer<EntityChair, BedrockModel<EntityChair>> {
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final String DEFAULT_CHAIR_ID = "touhou_little_maid:cushion";
    public static boolean renderHitBox = true;
    private ChairModelInfo chairInfo;
    private List<Object> chairAnimations;
    private GeckoEntityChairRenderer geckoEntityChairRenderer;

    public EntityChairRenderer(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new BedrockModel<>(), 0);
        this.geckoEntityChairRenderer = new GeckoEntityChairRenderer(rendererManager);
    }

    @Override
    public void render(EntityChair chair, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (canShowHitBox(player) && renderHitBox) {
            renderHitBox(chair, poseStack, bufferIn);
        } else {
            renderChair(chair, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        }
    }

    private boolean canShowHitBox(@Nullable Player player) {
        if (player != null && player.isShiftKeyDown()) {
            return player.getMainHandItem().getItem() == InitItems.CHAIR_SHOW.get();
        }
        return false;
    }

    private void renderHitBox(EntityChair chair, PoseStack poseStack, MultiBufferSource bufferIn) {
        AABB aabb = chair.getBoundingBox().move(-chair.getX(), -chair.getY(), -chair.getZ());
        LevelRenderer.renderLineBox(poseStack, bufferIn.getBuffer(RenderType.lines()), aabb, 1.0F, 0, 0, 1.0F);
    }

    private void renderChair(EntityChair chair, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        // 读取默认模型，用于清除不存在模型的缓存残留
        CustomPackLoader.CHAIR_MODELS.getModel(DEFAULT_CHAIR_ID).ifPresent(model -> this.model = model);
        CustomPackLoader.CHAIR_MODELS.getInfo(DEFAULT_CHAIR_ID).ifPresent(info -> this.chairInfo = info);
        this.chairAnimations = null;

        // 通过模型 id 获取对应数据
        CustomPackLoader.CHAIR_MODELS.getModel(chair.getModelId()).ifPresent(model -> this.model = model);
        CustomPackLoader.CHAIR_MODELS.getInfo(chair.getModelId()).ifPresent(info -> this.chairInfo = info);
        CustomPackLoader.CHAIR_MODELS.getAnimation(chair.getModelId()).ifPresent(animations -> this.chairAnimations = animations);

        // GeckoLib 接管渲染
        if (this.chairInfo.isGeckoModel()) {
            this.geckoEntityChairRenderer.setMainInfo(this.chairInfo);
            this.geckoEntityChairRenderer.render(chair, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
            return;
        }

        // 模型动画设置
        this.model.setAnimations(this.chairAnimations);

        GlWrapper.setPoseStack(poseStack);
        super.render(chair, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        GlWrapper.clearPoseStack();
    }

    @Override
    protected void scale(EntityChair chair, PoseStack poseStack, float partialTickTime) {
        float scale = chairInfo.getRenderEntityScale();
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityChair entity) {
        if (chairInfo == null) {
            return DEFAULT_TEXTURE;
        }
        return chairInfo.getTexture();
    }

    @Override
    protected void setupRotations(EntityChair chair, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - rotationYaw));
    }

    @Override
    protected boolean shouldShowName(EntityChair entity) {
        return entity.shouldShowName();
    }
}
