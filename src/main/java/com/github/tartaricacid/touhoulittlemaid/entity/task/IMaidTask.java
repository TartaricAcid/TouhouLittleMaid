package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

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
    SoundEvent getAmbientSound(EntityMaid maid);

    /**
     * 该模式下调用的 AI
     *
     * @param maid 女仆对象
     * @return 如果什么都不做，可以返回 null
     */
    @Nullable
    Goal createGoal(EntityMaid maid);

    /**
     * 当前 Task 是否可使用
     *
     * @param maid 女仆对象
     * @return 当前 Task 是否可以用
     */
    default boolean isEnable(EntityMaid maid) {
        return true;
    }

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
    default void onRangedAttack(EntityMaid maid, LivingEntity target, float distanceFactor) {
    }

    /**
     * 获取当前模式的国际化 key
     *
     * @return 国际化 key
     */
    default String getTranslationKey() {
        return "task." + getUid().getNamespace() + "." + getUid().getPath();
    }
}
