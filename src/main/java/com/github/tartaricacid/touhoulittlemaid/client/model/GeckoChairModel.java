package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoChairEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.AnimatedGeoModel;
import net.minecraft.util.ResourceLocation;

public class GeckoChairModel extends AnimatedGeoModel {
    private static final ResourceLocation GECKO_DEFAULT_ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "fox_miko");
    private static final ResourceLocation GECKO_DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");

    @Override
    public ResourceLocation getModelLocation(Object object) {
        if (object instanceof GeckoChairEntity) {
            GeckoChairEntity geckoChair = (GeckoChairEntity) object;
            return geckoChair.getModel();
        }
        return GECKO_DEFAULT_ID;
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        if (object instanceof GeckoChairEntity) {
            GeckoChairEntity geckoChair = (GeckoChairEntity) object;
            return geckoChair.getTexture();
        }
        return GECKO_DEFAULT_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object object) {
        if (object instanceof GeckoChairEntity) {
            GeckoChairEntity geckoChair = (GeckoChairEntity) object;
            return geckoChair.getAnimation();
        }
        return GECKO_DEFAULT_ID;
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double seekTime) {
    }
}
