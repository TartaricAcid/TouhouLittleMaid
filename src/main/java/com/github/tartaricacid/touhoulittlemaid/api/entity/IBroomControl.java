package com.github.tartaricacid.touhoulittlemaid.api.entity;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.mixin.accessor.LivingEntityAccessor;
import net.minecraft.world.entity.player.Player;

/**
 * 扫帚控制器的接口，用来管控如何控制扫帚
 * <p>
 * 扫帚控制器会按照优先级从高到低依次检查，调用 inControl 判断；
 * 首个符合条件的控制器会被调用，执行 travel 和 tickRot 方法。
 */
public interface IBroomControl {
    /**
     * 工具方法，检查玩家是否按下了跳跃键（双端可用）
     */
    static boolean keyJump(Player player) {
        if (player instanceof LivingEntityAccessor accessor) {
            return accessor.tlmIsJumping();
        }
        return false;
    }

    /**
     * 获取控制器的优先级，数字越大优先级越高
     */
    int getPriority();

    /**
     * 当前情况是否能够控制扫帚，如果返回 false，那么会继续检索低优先级的控制器进行控制
     * <p>
     * 这个方法在骑乘扫帚时每 tick 都会被调用，注意性能问题！
     */
    boolean inControl(Player player, EntityMaid maid);

    /**
     * 扫帚的控制方法，可以参考 PlayerBroomControl 的实现
     *
     * @param player 当前控制扫帚的玩家
     * @param maid   骑乘在扫帚上的女仆
     */
    void travel(Player player, EntityMaid maid);

    /**
     * 每 tick 调用一次，可用于处理扫帚的旋转等逻辑
     *
     * @param player 当前控制扫帚的玩家
     * @param maid   骑乘在扫帚上的女仆
     */
    void tickRot(Player player, EntityMaid maid);

    interface Factory {
        /**
         * 工厂方法，用于创建 IBroomControl 实例
         *
         * @param broom 扫帚实体
         * @return 当每个 EntityBroom 创建时都会调用此方法来创建 IBroomControl 实例
         */
        IBroomControl create(EntityBroom broom);
    }
}