package com.github.tartaricacid.touhoulittlemaid.client.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.AnimationBuilder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationFactory;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.GeckoLibUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class GeckoChairEntity implements IAnimatable {
    private static final ResourceLocation GECKO_DEFAULT_ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "fox_miko");
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this, true);
    private EntityChair chair = null;
    private ChairModelInfo chairInfo;

    @Override
    public void registerControllers(AnimationData data) {
        for (int i = 0; i < 8; i++) {
            String controllerName = String.format("parallel_%d_controller", i);
            String animationName = String.format("parallel%d", i);
            data.addAnimationController(new AnimationController<>(this, controllerName, 0, e -> predicateParallel(e, animationName)));
        }
    }

    public ResourceLocation getModel() {
        if (GeckoLibCache.getInstance().getGeoModels().containsKey(chairInfo.getModelId())) {
            return chairInfo.getModelId();
        }
        return GECKO_DEFAULT_ID;
    }

    public ResourceLocation getTexture() {
        return chairInfo.getTexture();
    }

    public ResourceLocation getAnimation() {
        if (GeckoLibCache.getInstance().getAnimations().containsKey(chairInfo.getModelId())) {
            return chairInfo.getModelId();
        }
        return GECKO_DEFAULT_ID;
    }

    public EntityChair getChair() {
        return chair;
    }

    public void setChair(EntityChair chair, ChairModelInfo chairInfo) {
        this.chair = chair;
        this.chairInfo = chairInfo;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private PlayState predicateParallel(AnimationEvent<GeckoChairEntity> event, String animationName) {
        if (Minecraft.getInstance().isPaused()) {
            return PlayState.STOP;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }
}
