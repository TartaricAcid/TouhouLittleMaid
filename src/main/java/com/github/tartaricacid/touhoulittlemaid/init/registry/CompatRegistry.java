package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.BlackList;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.server.ImmersiveMelodiesServerCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.patchouli.PatchouliCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.SBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.tbackpack.TBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.top.TheOneProbeInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CompatRegistry {
    public static final String TOP = "theoneprobe";
    public static final String PATCHOULI = "patchouli";
    public static final String CLOTH_CONFIG = "cloth_config";
    public static final String CARRY_ON = "carryon";
    public static final String SBACKPACK = "sophisticatedbackpacks";
    public static final String TBACKPACK = "travelersbackpack";
    public static final String CURIOS = "curios";
    public static final String IMMERSIVE_MELODIES = "immersive_melodies";

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> checkModLoad(TOP, () -> InterModComms.sendTo(TOP, "getTheOneProbe", TheOneProbeInfo::new)));
        event.enqueueWork(() -> checkModLoad(PATCHOULI, PatchouliCompat::init));
        event.enqueueWork(() -> checkModLoad(CLOTH_CONFIG, () -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                MenuIntegration.registerModsPage();
            }
        }));
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClothConfigScreen.registerNoClothConfigPage();
            }
        });
        event.enqueueWork(() -> checkModLoad(CARRY_ON, BlackList::addBlackList));
        event.enqueueWork(() -> checkModLoad(CURIOS, CuriosCompat::init));
        event.enqueueWork(() -> checkModLoad(SBACKPACK, SBackpackCompat::init));
        event.enqueueWork(() -> checkModLoad(TBACKPACK, TBackpackCompat::init));
        event.enqueueWork(() -> checkModLoad(IMMERSIVE_MELODIES, ImmersiveMelodiesServerCompat::init));
    }

    private static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }
}
