package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.client.gui.mod.ClothConfigScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.BlackList;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.github.tartaricacid.touhoulittlemaid.compat.patchouli.MultiblockRegistry;
import com.github.tartaricacid.touhoulittlemaid.compat.top.TheOneProbeInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CompatRegistry {
    public static final String TOP = "theoneprobe";
    public static final String PATCHOULI = "patchouli";
    public static final String CLOTH_CONFIG = "cloth_config";
    public static final String CARRY_ON_ID = "carryon";

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> checkModLoad(TOP, () -> InterModComms.sendTo(TOP, "getTheOneProbe", TheOneProbeInfo::new)));
        event.enqueueWork(() -> checkModLoad(PATCHOULI, MultiblockRegistry::init));
        event.enqueueWork(() -> checkModLoad(CLOTH_CONFIG, () -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MenuIntegration::registerModsPage)));
        event.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClothConfigScreen::registerNoClothConfigPage));
        event.enqueueWork(() -> checkModLoad(CARRY_ON_ID, BlackList::addBlackList));
    }

    private static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }
}
