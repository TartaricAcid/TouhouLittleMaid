package com.github.tartaricacid.touhoulittlemaid.compat.top;

import com.github.tartaricacid.touhoulittlemaid.compat.top.provider.MaidProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import javax.annotation.Nullable;
import java.util.function.Function;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TheOneProbeInfo implements Function<ITheOneProbe, Void> {
    private static final String TOP_ID = "theoneprobe";

    @SubscribeEvent
    public static void onEnqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(TOP_ID, "getTheOneProbe", TheOneProbeInfo::new);
    }

    @Nullable
    @Override
    public Void apply(@Nullable ITheOneProbe probe) {
        if (probe != null) {
            probe.registerEntityProvider(new MaidProvider());
        }
        return null;
    }
}
