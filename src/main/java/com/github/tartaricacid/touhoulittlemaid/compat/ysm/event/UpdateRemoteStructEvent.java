package com.github.tartaricacid.touhoulittlemaid.compat.ysm.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraftforge.eventbus.api.Event;

public class UpdateRemoteStructEvent extends Event {
    private final EntityMaid maid;
    private final Object2FloatOpenHashMap<String> roamingVars;

    public UpdateRemoteStructEvent(EntityMaid maid, Object2FloatOpenHashMap<String> roamingVars) {
        this.maid = maid;
        this.roamingVars = roamingVars;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public Object2FloatOpenHashMap<String> getRoamingVars() {
        return roamingVars;
    }
}
