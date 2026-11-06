package com.github.tartaricacid.touhoulittlemaid.client.input;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.STTCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabClientState;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabVoiceClient;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class STTChatKey {
    public static final KeyMapping STT_CHAT_KEY = new KeyMapping("key.touhou_little_maid.stt_chat.desc",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.category.touhou_little_maid");

    @SubscribeEvent
    public static void onSttChatPress(InputEvent.Key event) {
        if (!keyIsMatch(event)) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator()) {
            return;
        }
        if (!isInGame()) {
            return;
        }
        EntityMaid maid = nearestMaid(player);
        // 绑了 WAB 角色的女仆：这句听写交给插件，模组自己的大模型/语音识别开关不该再拦按键。
        boolean viaWab = WabClientState.sttFor(maid);
        if (!viaWab && (!AIConfig.LLM_ENABLED.get() || !AIConfig.STT_ENABLED.get())) {
            return;
        }
        STT_CHAT_KEY.consumeClick();
        if (event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(InitSounds.RECORDING_START.get(), 1f));
            if (maid != null) {
                sttStart(maid);
            } else {
                player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.stt.no_maid_found",
                        AIConfig.MAID_CAN_CHAT_DISTANCE.get()));
            }
            return;
        }
        if (event.getAction() == GLFW.GLFW_RELEASE) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(InitSounds.RECORDING_END.get(), 1f));
            if (maid != null) {
                sttStop(maid);
            }
        }
    }

    @SuppressWarnings("removal")
    private static boolean keyIsMatch(InputEvent.Key event) {
        return STT_CHAT_KEY.matches(event.getKey(), event.getScanCode())
               && STT_CHAT_KEY.getKeyModifier().equals(KeyModifier.getActiveModifier());
    }

    @Nullable
    private static EntityMaid nearestMaid(LocalPlayer player) {
        Level level = player.level();
        int range = AIConfig.MAID_CAN_CHAT_DISTANCE.get();
        AABB aabb = player.getBoundingBox().inflate(range);
        List<EntityMaid> maids = level.getEntitiesOfClass(EntityMaid.class, aabb,
                maid -> maid.isOwnedBy(player) && maid.isAlive());
        maids.sort(Comparator.comparingDouble(maid -> maid.distanceToSqr(player)));
        return maids.isEmpty() ? null : maids.get(0);
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

    private static void sttStart(EntityMaid maid) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        // 绑了 WAB 角色的女仆：录音仍走这台机器的麦克风，听写交给 WAB（经服务器转发）。
        if (WabClientState.sttFor(maid)) {
            WabVoiceClient.start(maid, new STTCallback(player, maid));
            return;
        }
        STTSite sttSite = AvailableSites.getSTTSite(AIConfig.STT_TYPE.get().getName());
        if (!sttSite.enabled()) {
            player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.stt.empty"));
            return;
        }
        tryToStart(maid, player, sttSite);
    }

    private static void tryToStart(EntityMaid maid, LocalPlayer player, STTSite sttSite) {
        STTConfig config = new STTConfig();
        STTCallback callback = new STTCallback(player, maid);
        sttSite.client().startRecord(config, callback);
    }

    private static void sttStop(EntityMaid maid) {
        if (WabVoiceClient.isRecordingFor(maid)) {
            WabVoiceClient.stop();
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        STTSite sttSite = AvailableSites.getSTTSite(AIConfig.STT_TYPE.get().getName());
        if (sttSite.enabled()) {
            STTConfig config = new STTConfig();
            STTCallback callback = new STTCallback(player, maid);
            sttSite.client().stopRecord(config, callback);
        }
    }
}
