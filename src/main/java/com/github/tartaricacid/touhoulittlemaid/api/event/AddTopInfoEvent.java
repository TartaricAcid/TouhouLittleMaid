package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraftforge.eventbus.api.Event;

public class AddTopInfoEvent extends Event {
    private final EntityMaid maid;
    private final ProbeMode probeMode;
    private final IProbeInfo probeInfo;
    private final IProbeHitEntityData hitEntityData;

    public AddTopInfoEvent(EntityMaid maid, ProbeMode probeMode, IProbeInfo probeInfo, IProbeHitEntityData hitEntityData) {
        this.maid = maid;
        this.probeMode = probeMode;
        this.probeInfo = probeInfo;
        this.hitEntityData = hitEntityData;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ProbeMode getProbeMode() {
        return probeMode;
    }

    public IProbeInfo getProbeInfo() {
        return probeInfo;
    }

    public IProbeHitEntityData getHitEntityData() {
        return hitEntityData;
    }
}
