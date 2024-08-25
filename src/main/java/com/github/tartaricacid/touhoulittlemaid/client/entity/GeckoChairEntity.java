package com.github.tartaricacid.touhoulittlemaid.client.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.AnimatableEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.PlayState;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.AnimationBuilder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.event.predicate.AnimationEvent;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class GeckoChairEntity extends AnimatableEntity<EntityChair> {
    private static final ResourceLocation GECKO_DEFAULT_ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "fox_miko");
    private static final ResourceLocation GECKO_DEFAULT_TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/empty.png");
    private static final int FPS = 30;

    private ChairModelInfo chairInfo;

    public GeckoChairEntity(EntityChair entity) {
        super(entity, FPS);
        registerControllers();
    }

    public void registerControllers() {
        for (int i = 0; i < 8; i++) {
            String controllerName = String.format("parallel_%d_controller", i);
            String animationName = String.format("parallel%d", i);
            addAnimationController(new AnimationController<>(this, controllerName, 0, e -> predicateParallel(e, animationName)));
        }
    }

    @Override
    public ResourceLocation getModelLocation() {
        if (this.chairInfo != null && GeckoLibCache.getInstance().getGeoModels().containsKey(this.chairInfo.getModelId())) {
            return this.chairInfo.getModelId();
        }
        return GECKO_DEFAULT_ID;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return this.chairInfo != null ? chairInfo.getTexture() : GECKO_DEFAULT_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation() {
        if (this.chairInfo != null && GeckoLibCache.getInstance().getAnimations().containsKey(this.chairInfo.getModelId())) {
            return this.chairInfo.getModelId();
        }
        return GECKO_DEFAULT_ID;
    }

    public ChairModelInfo getChairInfo() {
        return chairInfo;
    }

    public void setChair(ChairModelInfo chairInfo) {
        this.chairInfo = chairInfo;
    }

    private PlayState predicateParallel(AnimationEvent<GeckoChairEntity> event, String animationName) {
        if (Minecraft.getInstance().isPaused()) {
            return PlayState.STOP;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }
}
