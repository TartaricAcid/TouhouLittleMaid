package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.OptifineScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TouhouLittleMaid.MOD_ID)
public final class ShowOptifineScreen {
    private static boolean optifinePresent = false;
    private static boolean firstTitleScreenShown = false;

    @SubscribeEvent
    public static void showOptifineWarning(ScreenEvent.Init.Post event) {
        if (firstTitleScreenShown || !(event.getScreen() instanceof TitleScreen)) {
            return;
        }
        if (optifinePresent) {
            Minecraft.getInstance().setScreen(new OptifineScreen(event.getScreen()));
        }
        firstTitleScreenShown = true;
    }

    public static void checkOptifineIsLoaded() {
        try {
            Class.forName("net.optifine.Config");
            optifinePresent = true;
        } catch (ClassNotFoundException e) {
            optifinePresent = false;
        }
    }
}
