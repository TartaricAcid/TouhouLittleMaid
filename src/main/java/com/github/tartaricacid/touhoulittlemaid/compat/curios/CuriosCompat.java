package com.github.tartaricacid.touhoulittlemaid.compat.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.client.CuriosContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.curios.menu.CuriosContainer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
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

    public static boolean isLoadedOrEnable() {
        return isLoaded() && MaidConfig.ENABLE_MAID_CURIOS.get();
    }

    public static MenuProvider create(EntityMaid maid) {
        if (isLoadedOrEnable()) {
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
    public static void clientUpdatePage(int page) {
        if (isLoadedOrEnable()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof CuriosContainerScreen screen) {
                screen.updatePage(page);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientResetPage() {
        if (isLoadedOrEnable()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof CuriosContainerScreen screen) {
                screen.updatePage(screen.getPage());
            }
        }
    }
}
