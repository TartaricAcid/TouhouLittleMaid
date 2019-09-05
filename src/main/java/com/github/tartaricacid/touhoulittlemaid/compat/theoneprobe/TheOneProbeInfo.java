package com.github.tartaricacid.touhoulittlemaid.compat.theoneprobe;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/7/25 20:49
 **/
public class TheOneProbeInfo implements Function<ITheOneProbe, Void> {
    @Nullable
    @Override
    public Void apply(@Nullable ITheOneProbe probe) {
        if (probe != null) {
            probe.registerEntityProvider(new MaidProvider());
            probe.registerProvider(new GridProvider());
            probe.registerBlockDisplayOverride(new AltarProvider());
        }
        return null;
    }
}
