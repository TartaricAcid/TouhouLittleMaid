package com.github.tartaricacid.touhoulittlemaid.client.input;

import com.github.tartaricacid.touhoulittlemaid.network.message.DismountPackage;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class DismountBroomKey {
    public static final KeyMapping DISMOUNT_KEY = new KeyMapping("key.touhou_little_maid.dismount.desc",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.category.touhou_little_maid");

    @SubscribeEvent
    public static void onDismountPress(InputEvent.Key event) {
        if (keyIsMatch(event)) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null || player.isSpectator()) {
                return;
            }
            if (!isInGame()) {
                return;
            }
            DISMOUNT_KEY.consumeClick();
            if (event.getAction() == GLFW.GLFW_RELEASE) {
                PacketDistributor.sendToServer(new DismountPackage(DismountPackage.DISMOUNT_BROOM));
            }
        }
    }

    @SuppressWarnings("removal")
    private static boolean keyIsMatch(InputEvent.Key event) {
        return DISMOUNT_KEY.matches(event.getKey(), event.getScanCode())
                && DISMOUNT_KEY.getKeyModifier().equals(KeyModifier.getActiveModifier());
    }

    private static boolean isInGame() {
        Minecraft mc = Minecraft.getInstance();
        // 不能是加载界面
        if (mc.getOverlay() != null) {
            return false;
        }
        // 不能打开任何 GUI
        if (mc.screen != null) {
            return false;
        }
        // 当前窗口捕获鼠标操作
        if (!mc.mouseHandler.isMouseGrabbed()) {
            return false;
        }
        // 选择了当前窗口
        return mc.isWindowActive();
    }
}
