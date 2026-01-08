package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.magic;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.animation.IMagicCastingAnimationProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Comparator;
import java.util.List;

/**
 * 魔法咏唱动画管理器
 * <p>
 * 用于注册和管理魔法咏唱动画提供器
 * <p>
 * 仅在客户端使用
 *
 * @author Gardel &lt;gardel741@outlook.com&gt;
 * @since 2026-01-03
 */
@OnlyIn(Dist.CLIENT)
public class MagicCastingAnimationManager {
    private static List<IMagicCastingAnimationProvider> PROVIDERS = Lists.newArrayList();

    /**
     * 初始化魔法咏唱动画管理器
     * <p>
     * 在 ClientSetupEvent 中调用
     */
    public static void init() {
        MagicCastingAnimationManager manager = new MagicCastingAnimationManager();

        // 遍历所有附属模组，让它们注册自己的魔法咏唱动画提供器
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerMagicCastingAnimation(manager);
        }

        PROVIDERS = ImmutableList.copyOf(PROVIDERS);
    }

    /**
     * 注册魔法咏唱动画提供器
     *
     * @param provider 动画提供器
     */
    public void register(IMagicCastingAnimationProvider provider) {
        PROVIDERS.add(provider);
        // 按优先级排序，数字越大优先级越高
        PROVIDERS.sort(Comparator.comparingInt(IMagicCastingAnimationProvider::getPriority).reversed());
    }

    /**
     * 获取所有已注册的提供器
     * <p>
     * 仅供内部使用
     *
     * @return 提供器列表，按优先级排序
     */
    public static List<IMagicCastingAnimationProvider> getProviders() {
        return PROVIDERS;
    }

    /**
     * 清空所有注册的提供器
     * <p>
     * 在资源重载或模组初始化时调用
     */
    public static void clear() {
        PROVIDERS.clear();
    }
}
