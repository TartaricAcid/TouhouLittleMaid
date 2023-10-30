package com.github.tartaricacid.touhoulittlemaid.client.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationManager;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.controller.AnimationController;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationData;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationFactory;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.GeckoLibUtil;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

public class GeckoMaidEntity implements IAnimatable {
    private static final ResourceLocation GECKO_DEFAULT_ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "fox_miko");
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this, true);
    private EntityMaid maid = null;
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
        for (int i = 0; i < 8; i++) {
            String controllerName = String.format("parallel_%d_controller", i);
            String animationName = String.format("parallel%d", i);
            data.addAnimationController(new AnimationController<>(this, controllerName, 0, e -> manager.predicateParallel(e, animationName)));
        }
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType() == EquipmentSlotType.Group.ARMOR) {
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

    public EntityMaid getMaid() {
        return maid;
    }

    public void setMaid(EntityMaid maid, MaidModelInfo info) {
        this.maid = maid;
        this.mainInfo = info;
    }
}
