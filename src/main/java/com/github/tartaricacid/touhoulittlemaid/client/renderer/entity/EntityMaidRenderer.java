package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.client.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.animation.HardcodedAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer.*;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("rawtypes,unchecked")
public class EntityMaidRenderer extends MobRenderer<Mob, BedrockModel<Mob>> {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    /**
     * YSM 到时候会把渲染器加入其中
     */
    public static @Nullable Function<EntityRendererProvider.Context, IGeoEntityRenderer<Mob>> YSM_ENTITY_MAID_RENDERER;
    /**
     * 女仆模组自带的 GeckoLib 模型渲染
     */
    private final GeckoEntityMaidRenderer geckoEntityMaidRenderer;
    /**
     * YSM 借用的渲染类型，和上述互斥
     */
    private @Nullable IGeoEntityRenderer<Mob> ysmMaidRenderer;
    private MaidModelInfo mainInfo;
    private List<Object> mainAnimations = Lists.newArrayList();

    public EntityMaidRenderer(EntityRendererProvider.Context manager) {
        super(manager, new BedrockModel<>(), 0.5f);
        this.addLayer(new LayerMaidHeldItem(this, manager.getItemInHandRenderer()));
        this.addLayer(new LayerMaidBipedHead(this, manager.getModelSet()));
        this.addLayer(new LayerMaidBackpack(this, manager.getModelSet()));
        this.addLayer(new LayerMaidBackItem(this));
        this.addLayer(new LayerMaidBanner(this, manager.getModelSet()));
        this.addAdditionMaidLayer(manager);
        this.geckoEntityMaidRenderer = new GeckoEntityMaidRenderer<>(manager);
        this.initYsmModelRenderer(manager);
    }

    /**
     * 不能使用事件来初始化 YSM 渲染器
     * <p>
     * 使用事件的话，会受到先后顺序的影响
     */
    private void initYsmModelRenderer(EntityRendererProvider.Context manager) {
        if (!YsmCompat.isInstalled() || YSM_ENTITY_MAID_RENDERER == null) {
            return;
        }
        IGeoEntityRenderer<Mob> geoEntityRenderer = YSM_ENTITY_MAID_RENDERER.apply(manager);
        if (geoEntityRenderer != null) {
            this.ysmMaidRenderer = geoEntityRenderer;
            // 将女仆模组自带的 GeckoLib 模型的 Layer 渲染复制到 YSM 的 Layer 里去
            List<GeoLayerRenderer> layerRenderers = this.geckoEntityMaidRenderer.getLayerRenderers();
            for (GeoLayerRenderer layerRenderer : layerRenderers) {
                this.ysmMaidRenderer.addGeoLayerRenderer(layerRenderer.copy(this.ysmMaidRenderer));
            }
        }
    }

    @Override
    public void render(Mob entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }

        // 读取默认模型，用于清除不存在模型的缓存残留
        CustomPackLoader.MAID_MODELS.getModel(DEFAULT_MODEL_ID).ifPresent(model -> this.model = model);
        CustomPackLoader.MAID_MODELS.getInfo(DEFAULT_MODEL_ID).ifPresent(info -> this.mainInfo = info);
        CustomPackLoader.MAID_MODELS.getAnimation(DEFAULT_MODEL_ID).ifPresent(animations -> this.mainAnimations = animations);

        MaidModels.ModelData eventModelData = new MaidModels.ModelData(model, mainInfo, mainAnimations);
        if (MinecraftForge.EVENT_BUS.post(new RenderMaidEvent(maid, eventModelData))) {
            BedrockModel<Mob> bedrockModel = eventModelData.getModel();
            if (bedrockModel != null) {
                this.model = bedrockModel;
            }
            this.mainInfo = eventModelData.getInfo();
            this.mainAnimations = eventModelData.getAnimations();
        } else {
            // 通过模型 id 获取对应数据
            CustomPackLoader.MAID_MODELS.getModel(maid.getModelId()).ifPresent(model -> this.model = model);
            CustomPackLoader.MAID_MODELS.getInfo(maid.getModelId()).ifPresent(info -> this.mainInfo = info);
            CustomPackLoader.MAID_MODELS.getAnimation(maid.getModelId()).ifPresent(animations -> this.mainAnimations = animations);
        }

        // 渲染聊天气泡
        EntityMaid maidEntity = maid.asStrictMaid();
        // 暂定只能女仆显示
        if (maidEntity != null && maidEntity.getConfigManager().isChatBubbleShow()) {
            ChatBubbleRenderer.renderChatBubble(this, maidEntity, poseStack, bufferIn, packedLightIn);
        }

        // YSM 接管渲染
        if (maid.isYsmModel() && this.ysmMaidRenderer != null) {
            this.ysmMaidRenderer.getGeoEntity(entity).setYsmModel(maid.getYsmModelId(), maid.getYsmModelTexture());
            this.ysmMaidRenderer.geoRender(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
            return;
        }

        // GeckoLib 接管渲染
        if (this.mainInfo.isGeckoModel()) {
            this.geckoEntityMaidRenderer.getAnimatableEntity(entity).setMaidInfo(this.mainInfo);
            this.geckoEntityMaidRenderer.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
            return;
        }

        // 模型动画设置
        this.model.setAnimations(this.mainAnimations);
        // 渲染女仆模型本体
        GlWrapper.setPoseStack(poseStack);
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        GlWrapper.clearPoseStack();
    }

    @Override
    protected void scale(Mob maid, PoseStack poseStack, float partialTickTime) {
        float scale = mainInfo.getRenderEntityScale();
        poseStack.scale(scale, scale, scale);
    }

    @Override
    protected void setupRotations(Mob mob, PoseStack poseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(mob, poseStack, pAgeInTicks, pRotationYaw, pPartialTicks);

        // 抱起女仆时的旋转
        if (mob.getVehicle() instanceof Player && !this.mainInfo.isGeckoModel()) {
            poseStack.translate(-0.375, 0.8325, 0.375);
            poseStack.mulPose(Axis.ZN.rotationDegrees(65));
            poseStack.mulPose(Axis.YN.rotationDegrees(-80));
        }

        // 其他时候的旋转
        HardcodedAnimationManger.setupRotations(mob, poseStack, pAgeInTicks, pRotationYaw, pPartialTicks, this.mainInfo.isGeckoModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Mob maid) {
        if (mainInfo == null) {
            return DEFAULT_TEXTURE;
        }
        return mainInfo.getTexture();
    }

    public MaidModelInfo getMainInfo() {
        return mainInfo;
    }

    public EntityRenderDispatcher getDispatcher() {
        return this.entityRenderDispatcher;
    }

    private void addAdditionMaidLayer(EntityRendererProvider.Context renderManager) {
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addAdditionMaidLayer(this, renderManager);
        }
    }
}
