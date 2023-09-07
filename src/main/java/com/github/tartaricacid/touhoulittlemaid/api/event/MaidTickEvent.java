package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.event.entity.living.LivingEvent;

public class MaidTickEvent extends LivingEvent.LivingTickEvent {
    private final EntityMaid maid;

    public MaidTickEvent(EntityMaid maid) {
        super(maid);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
