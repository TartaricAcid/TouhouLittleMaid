package com.github.tartaricacid.touhoulittlemaid.compat.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.client.CuriosContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.curios.menu.CuriosContainer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.CuriosS2CUpdateMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

public class CuriosCompat {
    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = true;
        MinecraftForge.EVENT_BUS.register(new CuriosEvent());
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static MenuProvider create(EntityMaid maid) {
        if (isLoaded()) {
            return CuriosContainer.create(maid);
        } else {
            return maid.getMaidBackpackType().getGuiProvider(maid.getId());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreen() {
        MenuScreens.register(CuriosContainer.TYPE, CuriosContainerScreen::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(CuriosS2CUpdateMessage message) {
        if (isLoaded()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof CuriosContainerScreen screen) {
                screen.updatePage(message.page());
            }
        }
    }
}
