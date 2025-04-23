package com.github.tartaricacid.touhoulittlemaid.client.model.bedrock;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockModel;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.animation.HardcodedAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.animation.inner.IAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.EntityChairWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.EntityMaidWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.script.Invocable;
import java.util.HashMap;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BedrockModel<T extends LivingEntity> extends AbstractBedrockModel<T> {
    /**
     * 用于自定义动画的变量
     */
    private final EntityMaidWrapper entityMaidWrapper = new EntityMaidWrapper();
    private final EntityChairWrapper entityChairWrapper = new EntityChairWrapper();
    protected final HashMap<String, ModelRendererWrapper> modelMapWrapper = Maps.newHashMap();
    private List<Object> animations = Lists.newArrayList();

    public BedrockModel() {
        super();
    }

    public BedrockModel(BedrockModelPOJO pojo, BedrockVersion version) {
        super(pojo, version);
        this.modelMap.forEach((key, model) -> modelMapWrapper.put(key, new ModelRendererWrapper(model)));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (animations != null) {
            Invocable invocable = (Invocable) CustomJsAnimationManger.NASHORN;
            if (entityIn instanceof Mob mob) {
                IMaid maid = IMaid.convert(mob);
                if (maid != null) {
                    setupMaidAnim(maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, invocable);
                    // 硬编码动画
                    HardcodedAnimationManger.playMaidAnimation(maid, modelMapWrapper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                }
                return;
            }
            if (entityIn instanceof EntityChair) {
                setupChairAnim((EntityChair) entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, invocable);
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        for (BedrockPart model : shouldRender) {
            model.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }


    @SuppressWarnings("unchecked")
    private void setupMaidAnim(IMaid entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Invocable invocable) {
        try {
            for (Object animation : animations) {
                if (animation instanceof IAnimation) {
                    ((IAnimation<Mob>) animation).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0, entityIn.asEntity(), modelMapWrapper);
                } else {
                    entityMaidWrapper.setData(entityIn, attackTime, riding);
                    invocable.invokeMethod(animation, "animation", entityMaidWrapper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625f, modelMapWrapper);
                    entityMaidWrapper.clearData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomPackLoader.MAID_MODELS.removeAnimation(entityIn.getModelId());
        }
    }

    @SuppressWarnings("unchecked")
    private void setupChairAnim(EntityChair entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Invocable invocable) {
        try {
            for (Object animation : animations) {
                if (animation instanceof IAnimation) {
                    ((IAnimation<EntityChair>) animation).setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0, entityIn, modelMapWrapper);
                } else {
                    entityChairWrapper.setData(entityIn);
                    invocable.invokeMethod(animation, "animation", entityChairWrapper, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625f, modelMapWrapper);
                    entityChairWrapper.clearData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomPackLoader.CHAIR_MODELS.removeAnimation(entityIn.getModelId());
        }
    }

    public void translateToHand(HumanoidArm sideIn, PoseStack poseStack) {
        BedrockPart arm = getArm(sideIn);
        if (arm != null) {
            arm.translateAndRotate(poseStack);
        }
    }

    public boolean hasBackpackPositioningModel() {
        return modelMap.get("backpackPositioningBone") != null;
    }

    public BedrockPart getBackpackPositioningModel() {
        return modelMap.get("backpackPositioningBone");
    }

    @Nullable
    private BedrockPart getArm(HumanoidArm sideIn) {
        return sideIn == HumanoidArm.LEFT ? modelMap.get("armLeft") : modelMap.get("armRight");
    }

    public boolean hasHead() {
        return modelMap.containsKey("head");
    }

    public BedrockPart getHead() {
        return modelMap.get("head");
    }

    public boolean hasLeftArm() {
        return modelMap.containsKey("armLeft");
    }

    public boolean hasRightArm() {
        return modelMap.containsKey("armRight");
    }

    public boolean hasArmPositioningModel(HumanoidArm side) {
        BedrockPart arm = (side == HumanoidArm.LEFT ? modelMap.get("armLeftPositioningBone") : modelMap.get("armRightPositioningBone"));
        return arm != null;
    }

    public void translateToPositioningHand(HumanoidArm sideIn, PoseStack poseStack) {
        BedrockPart arm = (sideIn == HumanoidArm.LEFT ? modelMap.get("armLeftPositioningBone") : modelMap.get("armRightPositioningBone"));
        if (arm != null) {
            arm.translateAndRotate(poseStack);
        }
    }

    public boolean hasWaistPositioningModel(HumanoidArm side) {
        BedrockPart waist = (side == HumanoidArm.LEFT ? modelMap.get("waistLeftPositioningBone") : modelMap.get("waistRightPositioningBone"));
        return waist != null;
    }

    public void translateToPositioningWaist(HumanoidArm sideIn, PoseStack poseStack) {
        BedrockPart waist = (sideIn == HumanoidArm.LEFT ? modelMap.get("waistLeftPositioningBone") : modelMap.get("waistRightPositioningBone"));
        if (waist != null) {
            waist.translateAndRotate(poseStack);
        }
    }

    public void setAnimations(@Nullable List<Object> animations) {
        this.animations = animations;
    }
}
