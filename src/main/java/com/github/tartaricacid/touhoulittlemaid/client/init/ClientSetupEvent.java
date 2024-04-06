package com.github.tartaricacid.touhoulittlemaid.client.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.AnimationRegister;
import com.github.tartaricacid.touhoulittlemaid.client.event.ShowOptifineScreen;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.BroomTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
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
        event.enqueueWork(AnimationRegister::registerVariables);
        event.enqueueWork(InGameMaidConfig::read);
        event.enqueueWork(MaidTipsOverlay::init);
        event.enqueueWork(ShowOptifineScreen::checkOptifineIsLoaded);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(CROSSHAIR.id(), "tlm_maid_tips", new MaidTipsOverlay());
        event.registerAbove(CROSSHAIR.id(), "tlm_broom_tips", new BroomTipsOverlay());
    }
}