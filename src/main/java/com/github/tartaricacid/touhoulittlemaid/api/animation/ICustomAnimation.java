package com.github.tartaricacid.touhoulittlemaid.api.animation;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.HashMap;

public interface ICustomAnimation<T extends LivingEntity> {
    /**
     * 一个工具类，用来获取指定名称的 BedrockPart
     *
     * @param models   所有的骨骼
     * @param partName 指定名称的骨骼
     * @return 如果找不到，那么返回 null
     */
    @Nullable
    static BedrockPart getPartOrNull(HashMap<String, ? extends IModelRenderer> models, String partName) {
        IModelRenderer renderer = models.get(partName);
        if (renderer == null) {
            return null;
        }
        return renderer.getModelRenderer();
    }

    /**
     * 旧版模型
     * 原版里，全局的旋转是单独独立出来一个方法，比后面的动画部分要早执行
     *
     * @param entity       实体
     * @param poseStack    矩阵，就是改变它来控制全局旋转
     * @param ageInTicks   实体的 tick 时间，从 0 开始一直增大
     * @param rotationYaw  实体的整体的 Y Rot，单位为角度
     * @param partialTicks 插值
     */
    default void setupRotations(T entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
    }

    /**
     * GeckoLib 版模型
     * 原版里，全局的旋转是单独独立出来一个方法，比后面的动画部分要早执行
     *
     * @param entity       实体
     * @param poseStack    矩阵，就是改变它来控制全局旋转
     * @param ageInTicks   实体的 tick 时间，从 0 开始一直增大
     * @param rotationYaw  实体的整体的 Y Rot，单位为角度
     * @param partialTicks 插值
     */
    default void setupGeckoRotations(T entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
    }

    /**
     * 需要添加的动画（旧版模型）
     *
     * @param entity          实体
     * @param models          所有的骨骼
     * @param limbSwing       实体行走的里程，可以理解为汽车的里程表
     * @param limbSwingAmount 实体行走的速度，可以理解为汽车的速度表
     * @param ageInTicks      实体的 tick 时间，从 0 开始一直增大
     * @param netHeadYaw      实体的头部 Y Rot，单位为角度
     * @param headPitch       实体的头部 X Rot，单位为角度
     */
    default void setRotationAngles(T entity, HashMap<String, ? extends IModelRenderer> models,
                                   float limbSwing, float limbSwingAmount, float ageInTicks,
                                   float netHeadYaw, float headPitch) {
    }

    /**
     * 需要添加的动画（GeckoLib 版模型）
     *
     * @param entity          实体
     * @param model           所有的骨骼
     * @param limbSwing       实体行走的里程，可以理解为汽车的里程表
     * @param limbSwingAmount 实体行走的速度，可以理解为汽车的速度表
     * @param ageInTicks      实体的 tick 时间，从 0 开始一直增大
     * @param netHeadYaw      实体的头部 Y Rot，单位为角度
     * @param headPitch       实体的头部 X Rot，单位为角度
     */
    default void setGeckoRotationAngles(T entity, AnimatedGeoModel model,
                                        float limbSwing, float limbSwingAmount, float ageInTicks,
                                        float netHeadYaw, float headPitch) {
    }
}
