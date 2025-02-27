package com.github.tartaricacid.touhoulittlemaid.compat.ysm.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.eventbus.api.Event;

public class YsmMaidClientTickEvent extends Event {
    private final EntityMaid maid;

    public YsmMaidClientTickEvent(EntityMaid maid) {
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
