package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationRegister;
import com.github.tartaricacid.touhoulittlemaid.client.event.ShowOptifineScreen;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.BroomTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.ShowPowerOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;
import static net.neoforged.neoforge.client.gui.VanillaGuiLayers.CROSSHAIR;
import static net.neoforged.neoforge.client.gui.VanillaGuiLayers.HOTBAR;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = TouhouLittleMaid.MOD_ID)
public class ClientSetupEvent {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(AnimationRegister::registerAnimationState);
        event.enqueueWork(AnimationRegister::registerVariables);
        event.enqueueWork(MaidTipsOverlay::init);
        event.enqueueWork(ShowOptifineScreen::checkOptifineIsLoaded);
    }

    @SubscribeEvent
    public static void RegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(CROSSHAIR, getResourceLocation("tlm_maid_tips"), new MaidTipsOverlay());
        event.registerAbove(CROSSHAIR, getResourceLocation("tlm_broom_tips"), new BroomTipsOverlay());
        event.registerAbove(HOTBAR, getResourceLocation("tlm_show_power"), new ShowPowerOverlay());
    }
}