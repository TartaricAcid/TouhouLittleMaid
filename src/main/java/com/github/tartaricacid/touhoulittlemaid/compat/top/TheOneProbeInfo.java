package com.github.tartaricacid.touhoulittlemaid.compat.top;

import com.github.tartaricacid.touhoulittlemaid.compat.top.provider.MaidProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class TheOneProbeInfo implements Function<ITheOneProbe, Void> {
    @Nullable
    @Override
    public Void apply(@Nullable ITheOneProbe probe) {
        if (probe != null) {
            probe.registerEntityProvider(new MaidProvider());
        }
        return null;
    }
}
