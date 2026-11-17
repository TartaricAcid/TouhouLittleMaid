package com.github.tartaricacid.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabWorkScreen;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.init.registry.CompatRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = TouhouLittleMaid.MOD_ID, dist = Dist.CLIENT)
public class TouhouLittleMaidClient {
    public TouhouLittleMaidClient(IEventBus modEventBus, ModContainer modContainer) {
        this.registerClientOnly();
        this.registerConfigMenu(modContainer);
        // 暂停页的工作设置按钮：NeoForge 客户端事件走主事件总线。
        // WAB 设置页入口改用 wabConfig() 里的 BooleanToggle，不在这里注入。
        NeoForge.EVENT_BUS.addListener(TouhouLittleMaidClient::onScreenInit);
    }

    /** 暂停页左上角小按钮，打开女仆工作设置面板。 */
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        try {
            if (event.getScreen() instanceof PauseScreen) {
                event.addListener(Button.builder(Component.translatable("wab.work.pause_button"),
                                b -> Minecraft.getInstance().setScreen(new WabWorkScreen()))
                        .bounds(4, 4, 110, 20).build());
            }
        } catch (Throwable ignored) {
            // 入口是装饰性的，任何异常都不能影响原版界面
        }
    }

    private void registerClientOnly() {
        // 这个仅用于客户端，所以不需要在服务端注册
        InitDataAttachment.ATTACHMENT_TYPES.register("gecko_maid", () -> GeckoMaidEntity.TYPE);
    }

    private void registerConfigMenu(ModContainer modContainer) {
        ModFileInfo clothConfigInfo = LoadingModList.get().getModFileById(CompatRegistry.CLOTH_CONFIG);
        if (clothConfigInfo != null) {
            MenuIntegration.registerModsPage(modContainer);
        } else {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }
}
