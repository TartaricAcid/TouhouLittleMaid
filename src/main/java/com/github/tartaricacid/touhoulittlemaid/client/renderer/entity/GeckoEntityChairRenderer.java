package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoChairEntity;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoReplacedEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GeckoEntityChairRenderer extends GeoReplacedEntityRenderer<EntityChair, GeckoChairEntity> {
    private ChairModelInfo mainInfo;

    public GeckoEntityChairRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Override
    public GeckoChairEntity getAnimatableEntity(EntityChair entity) {
        return entity.getAnimatableEntity();
    }

    public ChairModelInfo getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(ChairModelInfo mainInfo) {
        this.mainInfo = mainInfo;
    }

    @Override
    public float getHeightScale(EntityChair entity) {
        return mainInfo.getRenderEntityScale();
    }

    @Override
    public float getWidthScale(EntityChair animatable) {
        return mainInfo.getRenderEntityScale();
    }
}
