package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.client.model.GeckoMaidModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.GeckoLayerMaidBackItem;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.GeckoLayerMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.GeckoLayerMaidBipedHead;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.GeckoLayerMaidHeld;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoReplacedEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class GeckoEntityMaidRenderer extends GeoReplacedEntityRenderer<GeckoMaidEntity> {
    private MaidModelInfo mainInfo;
    private GeoModel geoModel;

    public GeckoEntityMaidRenderer(EntityRendererManager renderManager) {
        super(renderManager, new GeckoMaidModel(), new GeckoMaidEntity());
        addLayer(new GeckoLayerMaidHeld<>(this));
        addLayer(new GeckoLayerMaidBipedHead<>(this));
        addLayer(new GeckoLayerMaidBackpack<>(this));
        addLayer(new GeckoLayerMaidBackItem<>(this));
    }

    @Override
    public void render(Entity entity, float entityYaw, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer bufferSource, int packedLight) {
        if (this.animatable != null && entity instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entity;
            this.animatable.setMaid(maid, mainInfo);
        }
        ResourceLocation location = this.modelProvider.getModelLocation(animatable);
        GeoModel geoModel = GeckoLibCache.getInstance().getGeoModels().get(location);
        if (geoModel != null) {
            this.geoModel = geoModel;
            super.render(entity, entityYaw, partialTick, matrixStack, bufferSource, packedLight);
        }
    }

    public MaidModelInfo getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(MaidModelInfo mainInfo) {
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
        if (animatable instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) animatable;
            return maid.getId();
        }
        return super.getInstanceId(22943);
    }

    @Nullable
    @Override
    public GeoModel getGeoModel() {
        return geoModel;
    }
}
