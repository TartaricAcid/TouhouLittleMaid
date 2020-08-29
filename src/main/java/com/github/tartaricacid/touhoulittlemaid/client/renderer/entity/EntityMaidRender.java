package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.api.event.RenderMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.*;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.ModelData;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public class EntityMaidRender extends RenderLiving<EntityMaid> {
    public static final Factory FACTORY = new Factory();
    public static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    public MaidModelInfo mainInfo;
    public List<Object> mainAnimations = Lists.newArrayList();
    public ModelData eventModelData;

    private EntityMaidRender(RenderManager renderManager, ModelBase modelBase, float shadowSize) {
        super(renderManager, modelBase, shadowSize);
        this.addLayer(new LayerMaidArrow(this));
        this.addLayer(new LayerMaidHeldItem(this));
        this.addLayer(new LayerMaidDebugFloor());
        this.addLayer(new LayerMaidDebugBroom());
        this.addLayer(new LayerHataSasimono(this));
        this.addLayer(new LayerMaidCustomHead(this));
        this.addLayer(new LayerMaidBackpack(this));
    }

    @Override
    public void doRender(@Nonnull EntityMaid entity, double x, double y, double z, float entityYaw, float partialTicks) {
        // 读取默认模型，用于清除不存在模型的缓存残留
        CustomResourcesLoader.MAID_MODEL.getModel(DEFAULT_MODEL_ID).ifPresent(model -> this.mainModel = model);
        CustomResourcesLoader.MAID_MODEL.getInfo(DEFAULT_MODEL_ID).ifPresent(info -> this.mainInfo = info);
        CustomResourcesLoader.MAID_MODEL.getAnimation(DEFAULT_MODEL_ID).ifPresent(animations -> this.mainAnimations = animations);

        eventModelData = new ModelData((EntityModelJson) mainModel, mainInfo, mainAnimations);
        if (MinecraftForge.EVENT_BUS.post(new RenderMaidEvent(entity, eventModelData))) {
            this.mainModel = eventModelData.getModel();
            this.mainInfo = eventModelData.getInfo();
            this.mainAnimations = eventModelData.getAnimations();
        } else {
            // 通过模型 id 获取对应数据
            CustomResourcesLoader.MAID_MODEL.getModel(entity.getModelId()).ifPresent(model -> this.mainModel = model);
            CustomResourcesLoader.MAID_MODEL.getInfo(entity.getModelId()).ifPresent(info -> this.mainInfo = info);
            CustomResourcesLoader.MAID_MODEL.getAnimation(entity.getModelId()).ifPresent(animations -> this.mainAnimations = animations);
        }

        // 模型动画设置
        ((EntityModelJson) this.mainModel).setAnimations(this.mainAnimations);

        // 实体渲染
        GlStateManager.pushMatrix();
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        GlStateManager.popMatrix();
    }

    @Override
    protected void preRenderCallback(EntityMaid entityMaid, float partialTickTime) {
        float scale = mainInfo.getRenderEntityScale();
        GlStateManager.scale(scale, scale, scale);
    }

    @Override
    protected boolean canRenderName(EntityMaid entity) {
        boolean hasCompass = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == MaidItems.KAPPA_COMPASS;
        boolean isYourMaid = entity.getOwnerId() != null && entity.getOwnerId().equals(Minecraft.getMinecraft().player.getUniqueID());
        return (hasCompass && isYourMaid) || super.canRenderName(entity);
    }

    @Override
    protected void renderEntityName(@Nonnull EntityMaid entityIn, double x, double y, double z, @Nonnull String name, double distanceSq) {
        boolean hasCompass = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == MaidItems.KAPPA_COMPASS;
        String str;
        y = y + mainInfo.getRenderEntityScale() - 1;
        if (hasCompass) {
            String modeKey = String.format("compass.touhou_little_maid.mode.%s", entityIn.getCompassMode().name().toLowerCase(Locale.US));
            str = I18n.format("tooltips.touhou_little_maid.kappa_compass.mode", I18n.format(modeKey));
            if (!entityIn.isHomeModeEnable()) {
                super.renderEntityName(entityIn, x, y + 0.25, z,
                        I18n.format("compass.touhou_little_maid.mode.home.disable"), 16);
            }
        } else {
            str = name;
        }
        super.renderEntityName(entityIn, x, y, z, str, 16);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityMaid entity) {
        return mainInfo.getTexture();
    }

    public MaidModelInfo getMainInfo() {
        return mainInfo;
    }

    public void setMainModel(EntityModelJson mainModel) {
        this.mainModel = mainModel;
    }

    public static class Factory implements IRenderFactory<EntityMaid> {
        @Override
        public Render<? super EntityMaid> createRenderFor(RenderManager manager) {
            // 加载默认的灵梦模型
            return new EntityMaidRender(manager, CustomResourcesLoader.MAID_MODEL.getModel(DEFAULT_MODEL_ID).orElseThrow(NullPointerException::new), 0.5f);
        }
    }
}
