package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.ApiStatus;

/**
 * 女仆近战伤害其他实体时触发此事件
 */
@ApiStatus.AvailableSince("1.4.0")
public abstract class MaidHurtTarget extends LivingEvent {
    private final EntityMaid maid;
    private final Entity target;

    public MaidHurtTarget(EntityMaid maid, Entity target) {
        super(maid);
        this.maid = maid;
        this.target = target;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public Entity getTarget() {
        return target;
    }

    @Cancelable
    public static class Pre extends MaidHurtTarget {
        public Pre(EntityMaid maid, Entity target) {
            super(maid, target);
        }
    }

    public static class Post extends MaidHurtTarget {
        private final boolean isHurt;

        public Post(EntityMaid maid, Entity target, boolean isHurt) {
            super(maid, target);
            this.isHurt = isHurt;
        }

        public boolean isHurt() {
            return isHurt;
        }
    }
}
