package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.HardcodedAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationRegister;
import com.github.tartaricacid.touhoulittlemaid.client.event.ShowOptifineScreen;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.BroomTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.ImmersiveMelodiesCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.oculus.OculusCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.simplehats.SimpleHatsCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sodium.SodiumCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.YsmCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.CROSSHAIR;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = TouhouLittleMaid.MOD_ID)
public class ClientSetupEvent {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(AnimationRegister::registerAnimationState);
        event.enqueueWork(MaidTipsOverlay::init);
        event.enqueueWork(ShowOptifineScreen::checkOptifineIsLoaded);
        event.enqueueWork(HardcodedAnimationManger::init);

        // 客户端兼容
        SimpleHatsCompat.init();
        ImmersiveMelodiesCompat.init();
        OculusCompat.init();
        SodiumCompat.init();
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(CROSSHAIR.id(), "tlm_maid_tips", new MaidTipsOverlay());
        event.registerAbove(CROSSHAIR.id(), "tlm_broom_tips", new BroomTipsOverlay());
    }
}