package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class MaidTickEvent extends LivingEvent implements ICancellableEvent {
    private final EntityMaid maid;

    public MaidTickEvent(EntityMaid maid) {
        super(maid);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
