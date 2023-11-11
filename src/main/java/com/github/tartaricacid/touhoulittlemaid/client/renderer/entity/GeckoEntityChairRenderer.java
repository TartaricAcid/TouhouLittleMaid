package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;


import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoChairEntity;
import com.github.tartaricacid.touhoulittlemaid.client.model.GeckoChairModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoReplacedEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class GeckoEntityChairRenderer extends GeoReplacedEntityRenderer<GeckoChairEntity> {
    private ChairModelInfo mainInfo;
    private GeoModel geoModel;

    public GeckoEntityChairRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GeckoChairModel(), new GeckoChairEntity());
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTick, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int packedLight) {
        if (this.animatable != null && entity instanceof EntityChair) {
            EntityChair chair = (EntityChair) entity;
            this.animatable.setChair(chair, mainInfo);
        }
        ResourceLocation location = this.modelProvider.getModelLocation(animatable);
        GeoModel geoModel = GeckoLibCache.getInstance().getGeoModels().get(location);
        if (geoModel != null) {
            this.geoModel = geoModel;
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    public ChairModelInfo getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(ChairModelInfo mainInfo) {
        this.mainInfo = mainInfo;
    }

    @Override
    public float getHeightScale(Object entity) {
        return mainInfo.getRenderEntityScale();
    }

    @Override
    public float getWidthScale(Object animatable) {
        return mainInfo.getRenderEntityScale();
    }

    @Override
    public int getInstanceId(Object animatable) {
        if (animatable instanceof EntityChair) {
            EntityChair chair = (EntityChair) animatable;
            return chair.getId();
        }
        return super.getInstanceId(22943);
    }

    @Nullable
    @Override
    public GeoModel getGeoModel() {
        return geoModel;
    }
}
