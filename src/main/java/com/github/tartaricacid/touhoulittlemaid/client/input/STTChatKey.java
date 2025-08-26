package com.github.tartaricacid.touhoulittlemaid.client.input;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.Player2AppCheck;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.STTCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.DefaultLLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
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
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

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
        if (keyIsMatch(event)) {
            if (!AIConfig.LLM_ENABLED.get()) {
                return;
            }
            if (!AIConfig.STT_ENABLED.get()) {
                return;
            }
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null || player.isSpectator()) {
                return;
            }
            if (!isInGame()) {
                return;
            }
            STT_CHAT_KEY.consumeClick();
            if (event.getAction() == GLFW.GLFW_PRESS) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(InitSounds.RECORDING_START.get(), 1f));
                getNearestMaid(player, STTChatKey::sttStart, true);
                return;
            }
            if (event.getAction() == GLFW.GLFW_RELEASE) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(InitSounds.RECORDING_END.get(), 1f));
                getNearestMaid(player, STTChatKey::sttStop, false);
            }
        }
    }

    @SuppressWarnings("removal")
    private static boolean keyIsMatch(InputEvent.Key event) {
        return STT_CHAT_KEY.matches(event.getKey(), event.getScanCode())
               && STT_CHAT_KEY.getKeyModifier().equals(KeyModifier.getActiveModifier());
    }

    private static void getNearestMaid(LocalPlayer player, Consumer<EntityMaid> consumer, boolean isStart) {
        Level level = player.level;
        int range = AIConfig.MAID_CAN_CHAT_DISTANCE.get();
        AABB aabb = player.getBoundingBox().inflate(range);
        List<EntityMaid> maids = level.getEntitiesOfClass(EntityMaid.class, aabb, maid -> maid.isOwnedBy(player) && maid.isAlive());
        maids.sort(Comparator.comparingDouble(maid -> maid.distanceToSqr(player)));
        if (!maids.isEmpty()) {
            consumer.accept(maids.get(0));
            return;
        }
        if (isStart) {
            player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.stt.no_maid_found", range));
        }
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
        STTSite sttSite = AvailableSites.getSTTSite(AIConfig.STT_TYPE.get().getName());
        if (!sttSite.enabled()) {
            player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.stt.empty"));
            return;
        }
        if (sttSite.id().equals(DefaultLLMSite.PLAYER2.id())) {
            Player2AppCheck.checkPlayer2AppInStt(player, () -> tryToStart(maid, player, sttSite));
        } else {
            tryToStart(maid, player, sttSite);
        }
    }

    private static void tryToStart(EntityMaid maid, LocalPlayer player, STTSite sttSite) {
        STTConfig config = new STTConfig();
        STTCallback callback = new STTCallback(player, maid);
        sttSite.client().startRecord(config, callback);
    }

    private static void sttStop(EntityMaid maid) {
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
