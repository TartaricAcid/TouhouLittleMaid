package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.server.ImmersiveMelodiesServerCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.patchouli.PatchouliCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sable.SableCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.SBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.tbackpack.TBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.top.TheOneProbeInfo;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class CompatRegistry {
    public static final String TOP = "theoneprobe";
    public static final String PATCHOULI = "patchouli";
    public static final String CLOTH_CONFIG = "cloth_config";
    public static final String SBACKPACK = "sophisticatedbackpacks";
    public static final String TBACKPACK = "travelersbackpack";
    public static final String CURIOS = "curios";
    public static final String IMMERSIVE_MELODIES = "immersive_melodies";
    public static final String SABLE = "sable";

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> checkModLoad(TOP, () -> InterModComms.sendTo(TOP, "getTheOneProbe", TheOneProbeInfo::new)));
        event.enqueueWork(() -> checkModLoad(PATCHOULI, PatchouliCompat::init));
        event.enqueueWork(() -> checkModLoad(CURIOS, CuriosCompat::init));
        event.enqueueWork(() -> checkModLoad(SBACKPACK, SBackpackCompat::init));
        event.enqueueWork(() -> checkModLoad(TBACKPACK, TBackpackCompat::init));
        event.enqueueWork(() -> checkModLoad(IMMERSIVE_MELODIES, ImmersiveMelodiesServerCompat::init));
        event.enqueueWork(() -> checkModLoad(SABLE, SableCompat::init));
    }

    private static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }
}
