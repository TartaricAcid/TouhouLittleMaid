package com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.fml.common.eventhandler.Event;

public class FavorabilityEvent extends Event {
    private EventType type;
    private EntityMaid maid;

    public FavorabilityEvent(EventType type, EntityMaid maid) {
        this.type = type;
        this.maid = maid;
    }

    public EventType getType() {
        return type;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
