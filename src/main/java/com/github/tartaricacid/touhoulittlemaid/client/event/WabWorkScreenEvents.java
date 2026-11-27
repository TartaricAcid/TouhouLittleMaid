package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabWorkScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 暂停菜单左侧的小按钮：一键打开女仆工作设置。 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TouhouLittleMaid.MOD_ID)
public final class WabWorkScreenEvents {
    @SubscribeEvent
    public static void onPauseInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof PauseScreen)) {
            return;
        }
        try {
            event.addListener(Button.builder(Component.translatable("wab.work.pause_button"),
                    b -> WabWorkScreen.open()).bounds(4, 4, 96, 20).build());
        } catch (Exception ignored) {
            // 注入失败不影响暂停菜单本身
        }
    }
}
