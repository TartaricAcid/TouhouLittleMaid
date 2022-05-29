package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * 这个事件会在女仆播放音效时进行触发。<br>
 * 这个事件是 {@link Cancelable}，如果取消，那么女仆播放音效会被取消。<br>
 * 该事件在服务端触发。
 */
@Cancelable
public class MaidPlaySoundEvent extends Event {
    private final EntityMaid maid;

    public MaidPlaySoundEvent(EntityMaid maid) {
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
