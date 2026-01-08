package com.github.tartaricacid.touhoulittlemaid.api.animation;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.AnimationBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * 魔法咏唱动画提供器接口
 * <p>
 * 附属模组可以实现此接口来提供自定义的魔法咏唱动画逻辑
 * <p>
 * 注意：
 * <ul>
 *     <li>附属需要自行管理咏唱状态、tick 计数等所有数据</li>
 *     <li>本模组不会记录或缓存任何施法相关数据</li>
 *     <li>本模组不提供默认动画，附属必须提供自己的 AnimationBuilder</li>
 * </ul>
 *
 * @author Gardel &lt;gardel741@outlook.com&gt;
 * @since 2026-01-03
 */
@ApiStatus.AvailableSince("1.4.7")
public interface IMagicCastingAnimationProvider {
    /**
     * 判断女仆是否应该播放魔法咏唱动画
     * <p>
     * 此方法在每一帧都会被调用，附属需要自行管理所有状态数据
     *
     * @param maid 女仆实体
     * @return 如果应该播放魔法动画则返回状态对象，否则返回 null
     */
    @Nullable
    IMagicCastingState getMagicCastingState(IMaid maid);

    /**
     * 根据当前咏唱状态获取对应的动画构建器
     * <p>
     * 本模组不提供默认动画，此方法必须返回有效的 AnimationBuilder
     *
     * @param maid  女仆实体
     * @param state 当前咏唱状态（保证非 null 且 phase 不为 NONE）
     * @return 动画构建器，如果返回 null 则跳过此动画
     */
    @Nullable
    AnimationBuilder getAnimationBuilder(IMaid maid, IMagicCastingState state);

    /**
     * 获取此 provider 的优先级
     * <p>
     * 数字越大优先级越高。<br>
     * 当多个 provider 同时返回非 null 状态时，只使用优先级最高的那个。<br>
     * 相同优先级按注册顺序。
     *
     * @return 优先级值，默认为 100
     */
    default int getPriority() {
        return 100;
    }
}
