package com.github.tartaricacid.touhoulittlemaid.api.animation;

import org.jetbrains.annotations.ApiStatus;

/**
 * 魔法咏唱状态接口
 * <p>
 * 表示当前帧的魔法咏唱动画状态
 * <p>
 * 注意：本模组不记录任何施法相关数据，所有状态数据由附属模组自行管理
 *
 * @author Gardel &lt;gardel741@outlook.com&gt;
 * @since 2026-01-03
 */
@ApiStatus.AvailableSince("1.4.7")
public interface IMagicCastingState {
    /**
     * 获取当前帧的魔法咏唱阶段
     *
     * @return 咏唱阶段枚举
     */
    CastingPhase getCurrentPhase();

    /**
     * 咏唱是否被取消
     *
     * @return true 表示咏唱已取消
     */
    boolean isCancelled();

    /**
     * 设置咏唱取消状态
     *
     * @param cancelled 是否已取消
     */
    void setCancelled(boolean cancelled);

    /**
     * 魔法咏唱阶段枚举
     */
    enum CastingPhase {
        /**
         * 不在咏唱中
         */
        NONE,

        /**
         * 瞬间施法
         */
        INSTANT,

        /**
         * 开始咏唱
         */
        START,

        /**
         * 持续咏唱
         */
        CASTING,

        /**
         * 咏唱结束
         */
        END
    }
}
