package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationRegister;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.processor.IBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.model.provider.data.EntityModelData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class GeckoMaidModel extends AnimatedGeoModel {
    private static final ResourceLocation GECKO_DEFAULT_ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "fox_miko");
    private static final ResourceLocation GECKO_DEFAULT_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");

    @Override
    public ResourceLocation getModelLocation(Object object) {
        if (object instanceof GeckoMaidEntity geckoMaid) {
            return geckoMaid.getModel();
        }
        return GECKO_DEFAULT_ID;
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        if (object instanceof GeckoMaidEntity geckoMaid) {
            return geckoMaid.getTexture();
        }
        return GECKO_DEFAULT_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object object) {
        if (object instanceof GeckoMaidEntity geckoMaid) {
            return geckoMaid.getAnimation();
        }
        return GECKO_DEFAULT_ID;
    }

    @Override
    public void setMolangQueries(IAnimatable animatable, double seekTime) {
    }

    @Override
    @SuppressWarnings("all")
    public void setCustomAnimations(IAnimatable animatable, int instanceId, AnimationEvent animationEvent) {
        List extraData = animationEvent.getExtraData();
        MolangParser parser = GeckoLibCache.getInstance().parser;
        if (!Minecraft.getInstance().isPaused() && extraData.size() == 1 && extraData.get(0) instanceof EntityModelData data
                && animatable instanceof GeckoMaidEntity geckoMaidEntity && geckoMaidEntity.getMaid() != null) {
			IMaid maid = geckoMaidEntity.getMaid();
            AnimationRegister.setParserValue(animationEvent, parser, data, maid);
            super.setCustomAnimations(animatable, instanceId, animationEvent);
            IBone head = getCurrentModel().head;
            if (head != null) {
                head.setRotationX(head.getRotationX() + (float) Math.toRadians(data.headPitch));
                head.setRotationY(head.getRotationY() + (float) Math.toRadians(data.netHeadYaw));
            }
        } else {
            super.setCustomAnimations(animatable, instanceId, animationEvent);
        }
    }
}