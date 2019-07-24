package com.github.tartaricacid.touhoulittlemaid.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

/**
 * 女仆任务模式接口
 *
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public interface IMaidTask {
    /**
     * 模式 ID，用于后续模式的判断，也用于本地化的 key
     *
     * @return 用 ResourceLocation 类描述的模式 ID
     */
    ResourceLocation getUid();

    /**
     * 该模式的图标
     *
     * @return 用 ItemStack 类描述的图标
     */
    ItemStack getIcon();

    /**
     * 该模式的环境音效
     *
     * @param maid 女仆对象
     * @return 对应的环境音效
     */
    @Nullable
    SoundEvent getAmbientSound(AbstractEntityMaid maid);

    /**
     * 该模式下调用的 AI
     *
     * @param maid 女仆对象
     * @return 如果什么都不做，可以返回 null
     */
    @Nullable
    EntityAIBase createAI(AbstractEntityMaid maid);

    /**
     * 主要应用于女仆是否会慌乱跑这样的 AI 触发上（我都处于战斗模式了，怎么能怯战呢）
     *
     * @return 是否处于战斗状态
     */
    default boolean isAttack() {
        return false;
    }

    /**
     * 远程攻击的逻辑
     *
     * @param maid           女仆对象
     * @param target         攻击目标
     * @param distanceFactor 距离因子，会依据蓄力进行数值传入
     */
    default void onRangedAttack(AbstractEntityMaid maid, EntityLivingBase target, float distanceFactor) {
    }
}
