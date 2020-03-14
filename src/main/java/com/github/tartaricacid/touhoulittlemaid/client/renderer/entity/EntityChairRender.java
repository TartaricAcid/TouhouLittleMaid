package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;


import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerChairDebugCharacter;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers.LayerChairDebugFloor;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/8 13:35
 **/

@SideOnly(Side.CLIENT)
public class EntityChairRender extends RenderLivingBase<EntityChair> {
    public static final EntityChairRender.Factory FACTORY = new EntityChairRender.Factory();
    private static final String DEFAULT_CHAIR_ID = "touhou_little_maid:cushion";
    private ChairModelInfo mainInfo;
    private List<Object> mainAnimations;

    private EntityChairRender(RenderManager renderManager, EntityModelJson mainModel) {
        super(renderManager, mainModel, 0f);
        addLayer(new LayerChairDebugFloor());
        addLayer(new LayerChairDebugCharacter());
    }

    @Override
    public void doRender(@Nonnull EntityChair chair, double x, double y, double z, float entityYaw, float partialTicks) {
        // 尝试读取模
        CustomResourcesLoader.CHAIR_MODEL.getModel(DEFAULT_CHAIR_ID).ifPresent(model -> this.mainModel = model);
        CustomResourcesLoader.CHAIR_MODEL.getInfo(DEFAULT_CHAIR_ID).ifPresent(info -> this.mainInfo = info);
        this.mainAnimations = null;

        // 通过模型 id 获取对应数据
        CustomResourcesLoader.CHAIR_MODEL.getModel(chair.getModelId()).ifPresent(model -> this.mainModel = model);
        CustomResourcesLoader.CHAIR_MODEL.getInfo(chair.getModelId()).ifPresent(info -> this.mainInfo = info);
        CustomResourcesLoader.CHAIR_MODEL.getAnimation(chair.getModelId()).ifPresent(animations -> this.mainAnimations = animations);

        // 模型动画设置
        ((EntityModelJson) this.mainModel).setAnimations(this.mainAnimations);

        // 实体渲染
        GlStateManager.pushMatrix();
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        super.doRender(chair, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityChair chair) {
        return mainInfo.getTexture();
    }

    @Override
    protected void applyRotations(EntityChair chair, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.rotate(180 - chair.rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected boolean canRenderName(EntityChair entity) {
        return entity.getAlwaysRenderNameTag();
    }

    public static class Factory implements IRenderFactory<EntityChair> {
        @Override
        public Render<? super EntityChair> createRenderFor(RenderManager manager) {
            return new EntityChairRender(manager, CustomResourcesLoader.CHAIR_MODEL.getModel(DEFAULT_CHAIR_ID).orElseThrow(NullPointerException::new));
        }
    }
}
