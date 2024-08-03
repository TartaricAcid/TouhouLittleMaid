package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class MaidTickEvent extends LivingEvent {
    private final EntityMaid maid;

    public MaidTickEvent(EntityMaid maid) {
        super(maid);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
