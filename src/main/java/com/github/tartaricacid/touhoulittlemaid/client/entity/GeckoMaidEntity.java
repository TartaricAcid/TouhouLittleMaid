package com.github.tartaricacid.touhoulittlemaid.client.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationManager;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationFactory;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.GeckoLibUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class GeckoMaidEntity implements IAnimatable {
    private static final ResourceLocation GECKO_DEFAULT_ID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "fox_miko");
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this, true);
    private IMaid maid = null;
    private MaidModelInfo mainInfo;

    @Override
    public void registerControllers(AnimationData data) {
        AnimationManager manager = AnimationManager.getInstance();
        for (int i = 0; i < 8; i++) {
            String controllerName = String.format("pre_parallel_%d_controller", i);
            String animationName = String.format("pre_parallel%d", i);
            data.addAnimationController(new AnimationController<>(this, controllerName, 0, e -> manager.predicateParallel(e, animationName)));
        }
        data.addAnimationController(new AnimationController<>(this, "main", 2, manager::predicateMain));
        data.addAnimationController(new AnimationController<>(this, "hold_offhand", 0, manager::predicateOffhandHold));
        data.addAnimationController(new AnimationController<>(this, "hold_mainhand", 0, manager::predicateMainhandHold));
        data.addAnimationController(new AnimationController<>(this, "swing", 2, manager::predicateSwing));
        data.addAnimationController(new AnimationController<>(this, "use", 2, manager::predicateUse));
        data.addAnimationController(new AnimationController<>(this, "beg", 2, manager::predicateBeg));
        data.addAnimationController(new AnimationController<>(this, "passenger", 2, manager::predicatePassengerAnimation));
        for (int i = 0; i < 8; i++) {
            String controllerName = String.format("parallel_%d_controller", i);
            String animationName = String.format("parallel%d", i);
            data.addAnimationController(new AnimationController<>(this, controllerName, 0, e -> manager.predicateParallel(e, animationName)));
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
                String controllerName = String.format("%s_controller", slot.getName());
                data.addAnimationController(new AnimationController<>(this, controllerName, 0, e -> manager.predicateArmor(e, slot)));
            }
        }
    }

    public ResourceLocation getModel() {
        if (GeckoLibCache.getInstance().getGeoModels().containsKey(mainInfo.getModelId())) {
            return mainInfo.getModelId();
        }
        return GECKO_DEFAULT_ID;
    }

    public ResourceLocation getTexture() {
        return mainInfo.getTexture();
    }

    public ResourceLocation getAnimation() {
        if (GeckoLibCache.getInstance().getAnimations().containsKey(mainInfo.getModelId())) {
            return mainInfo.getModelId();
        }
        return GECKO_DEFAULT_ID;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public IMaid getMaid() {
        return maid;
    }

    public void setMaid(IMaid maid, MaidModelInfo info) {
        this.maid = maid;
        this.mainInfo = info;
    }
}
