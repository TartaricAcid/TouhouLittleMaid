package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;


import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoChairEntity;
import com.github.tartaricacid.touhoulittlemaid.client.model.GeckoChairModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoReplacedEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class GeckoEntityChairRenderer extends GeoReplacedEntityRenderer<GeckoChairEntity> {
    private ChairModelInfo mainInfo;
    private GeoModel geoModel;

    public GeckoEntityChairRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeckoChairModel(), new GeckoChairEntity());
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (this.animatable != null && entity instanceof EntityChair chair) {
            this.animatable.setChair(chair, mainInfo);
        }
        ResourceLocation location = this.modelProvider.getModelLocation(animatable);
        GeoModel geoModel = GeckoLibCache.getInstance().getGeoModels().get(location);
        if (geoModel != null) {
            this.geoModel = geoModel;
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    public void setMainInfo(ChairModelInfo mainInfo) {
        this.mainInfo = mainInfo;
    }

    public ChairModelInfo getMainInfo() {
        return mainInfo;
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
        if (animatable instanceof EntityChair chair) {
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
