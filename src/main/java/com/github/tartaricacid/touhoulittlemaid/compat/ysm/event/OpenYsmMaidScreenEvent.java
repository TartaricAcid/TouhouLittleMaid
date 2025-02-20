package com.github.tartaricacid.touhoulittlemaid.compat.ysm.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class OpenYsmMaidScreenEvent extends Event {
    private final EntityMaid maid;

    public OpenYsmMaidScreenEvent(EntityMaid maid) {
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
