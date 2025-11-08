package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * 女仆好感度等级变化事件
 * <p>
 * 虽然女仆设定上是只能升级不会降级，但是仍然有创造模式道具可以强制降级。<br>
 * 所以也需要考虑上 oldLevel 和 newLevel 的大小
 */
@ApiStatus.AvailableSince("1.4.2")
public class MaidFavorabilityLevelChangeEvent extends LivingEvent {
    private final EntityMaid maid;
    private final int oldLevel;
    private final int newLevel;

    public MaidFavorabilityLevelChangeEvent(EntityMaid maid, int oldLevel, int newLevel) {
        super(maid);
        this.maid = maid;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }
}
