package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 绑了 WAB 角色的女仆：合成与听写都借 WAB 的嗓子/耳朵，模组只负责在桥上收发。
 * <p>
 * 音色、引擎、参考音全部由 WAB 按角色自己的设置决定，这里不传任何音色参数——
 * 请求里就算带了也会被宿主忽略。绑定判断用 {@link WabBridge#isBound}，
 * 没绑角色的女仆一次都不会走到这里，仍用游戏自己那套 TTS/STT。
 * <p>
 * 这个类只在服务端线程上被调用（帧路由、载荷回调、tick），音频分块原样转给主人客户端拼装。
 */
public final class WabVoice {
    /** 一句话最多合成多少字：WAV 不压缩，太长的一帧连客户端都播不完。 */
    private static final int MAX_TTS_TEXT = 500;
    private static final long TTS_TIMEOUT_MS = 60_000L;
    private static final long STT_TIMEOUT_MS = 30_000L;

    private static final Map<Integer, PendingTts> TTS = new ConcurrentHashMap<>();
    private static final Map<Integer, PendingStt> STT = new ConcurrentHashMap<>();
    private static int sequence = 1;

    private WabVoice() {
    }

    private static final class PendingTts {
        /** 客户端靠实体 id 认领是哪具身体在说话，跟 STT 的回执用同一个键。 */
        final int maid;
        final UUID player;
        final long at = System.currentTimeMillis();
        int parts = -1;

        PendingTts(int maid, UUID player) {
            this.maid = maid;
            this.player = player;
        }
    }

    private static final class PendingStt {
        final int maid;
        final UUID player;
        final long at = System.currentTimeMillis();

        PendingStt(int maid, UUID player) {
            this.maid = maid;
            this.player = player;
        }
    }

    /** 女仆说一句话：请 WAB 合成，音频回来之后由 {@link #onTtsAudio} 转给主人的客户端。 */
    static void speak(WabBridge bridge, EntityMaid maid, String text) {
        WabSession session = bridge.session();
        if (session == null || session.isClosed() || !bridge.voiceTts() || !bridge.isBound(maid)) return;
        ServerPlayer player = bridge.currentOwner();
        if (player == null) return;
        String spoken = text == null ? "" : text.trim();
        if (spoken.isEmpty()) return;
        if (spoken.length() > MAX_TTS_TEXT) {
            spoken = spoken.substring(0, MAX_TTS_TEXT);
        }
        int request = nextRequest();
        TTS.put(request, new PendingTts(maid.getId(), player.getUUID()));
        JsonObject frame = frame("tts", request);
        frame.addProperty("maid", maid.getUUID().toString());
        frame.addProperty("text", spoken);
        session.send(frame);
    }

    static void onTtsReply(WabBridge bridge, JsonObject frame) {
        int request = requestId(frame);
        PendingTts pending = request == 0 ? null : TTS.get(request);
        if (pending == null) return;
        if (!WabJson.bool(frame, "ok", false)) {
            TTS.remove(request);
            TouhouLittleMaid.LOGGER.info("[WAB] tts refused: {} {}", WabJson.str(frame, "code"), WabJson.str(frame, "error"));
            return;
        }
        pending.parts = Math.max(0, WabJson.integer(frame, "parts", 0));
        if (pending.parts == 0) TTS.remove(request);
    }

    static void onTtsAudio(WabBridge bridge, JsonObject frame) {
        int request = requestId(frame);
        PendingTts pending = request == 0 ? null : TTS.get(request);
        if (pending == null || pending.parts <= 0) return;
        int part = WabJson.integer(frame, "part", -1);
        if (part < 0 || part >= pending.parts) return;

        byte[] chunk;
        try {
            chunk = Base64.getDecoder().decode(WabJson.str(frame, "data"));
        } catch (IllegalArgumentException e) {
            TTS.remove(request);
            return;
        }
        if (chunk.length == 0) return;

        ServerPlayer player = bridge.server().getPlayerList().getPlayer(pending.player);
        if (player != null) {
            NetworkHandler.sendToClientPlayer(new WabVoiceAudio(pending.maid, request, part, pending.parts, chunk), player);
        }
        if (part == pending.parts - 1) TTS.remove(request);
    }

    /**
     * 主人对着女仆说了一段话，录音由客户端传上来。这里只负责把整段 WAV 交给 WAB 听写，
     * 识别结果回到客户端后仍走 STT 原来的回调（也就是照常发进聊天/对话链路）。
     */
    static void requestStt(WabBridge bridge, EntityMaid maid, ServerPlayer player, byte[] wav) {
        WabSession session = bridge.session();
        if (session == null || session.isClosed() || !bridge.voiceStt() || !bridge.isBound(maid)
                || wav == null || wav.length == 0) {
            NetworkHandler.sendToClientPlayer(new WabVoiceText(maid.getId(), false, "", "WAB 没有接手语音识别，先用游戏自己的设置。"), player);
            return;
        }
        int request = nextRequest();
        STT.put(request, new PendingStt(maid.getId(), player.getUUID()));
        JsonObject frame = frame("stt", request);
        frame.addProperty("maid", maid.getUUID().toString());
        frame.addProperty("lang", AIConfig.TTS_LANGUAGE.get());
        frame.addProperty("data", Base64.getEncoder().encodeToString(wav));
        session.send(frame);
    }

    static void onSttReply(WabBridge bridge, JsonObject frame) {
        int request = requestId(frame);
        PendingStt pending = request == 0 ? null : STT.remove(request);
        if (pending == null) return;
        ServerPlayer player = bridge.server().getPlayerList().getPlayer(pending.player);
        if (player == null) return;
        boolean ok = WabJson.bool(frame, "ok", false);
        String text = WabJson.str(frame, "text");
        String error = WabJson.str(frame, "error", "没听清这句。");
        NetworkHandler.sendToClientPlayer(new WabVoiceText(pending.maid, ok, text, error), player);
    }

    static void tick(WabBridge bridge) {
        if (TTS.isEmpty() && STT.isEmpty()) return;
        long now = System.currentTimeMillis();
        TTS.entrySet().removeIf(entry -> now - entry.getValue().at > TTS_TIMEOUT_MS);
        Iterator<Map.Entry<Integer, PendingStt>> iterator = STT.entrySet().iterator();
        while (iterator.hasNext()) {
            PendingStt pending = iterator.next().getValue();
            if (now - pending.at <= STT_TIMEOUT_MS) continue;
            iterator.remove();
            ServerPlayer player = bridge.server().getPlayerList().getPlayer(pending.player);
            if (player != null) {
                NetworkHandler.sendToClientPlayer(new WabVoiceText(pending.maid, false, "", "她那边花了太久才回话，这句没听清。"), player);
            }
        }
    }

    /** 会话断开：所有等着回执的请求都不再有结果，客户端要立刻回落到游戏自己的语音。 */
    static void reset(WabBridge bridge) {
        TTS.clear();
        for (PendingStt pending : STT.values()) {
            ServerPlayer player = bridge.server().getPlayerList().getPlayer(pending.player);
            if (player != null) {
                NetworkHandler.sendToClientPlayer(new WabVoiceText(pending.maid, false, "", "WAB 断开了，这句没听清。"), player);
            }
        }
        STT.clear();
        pushClientState(bridge);
    }

    /**
     * 把「哪些女仆绑了角色、WAB 现在能不能听写」同步给主人客户端。
     * 客户端只有在拿到这份名单之后才会把按键录音路线改到 WAB，否则仍用游戏自己的 STT。
     */
    static void pushClientState(WabBridge bridge) {
        ServerPlayer player = bridge.currentOwner();
        if (player == null) return;
        List<UUID> maids = new ArrayList<>(bridge.boundMaidIds());
        NetworkHandler.sendToClientPlayer(new WabBindingSync(maids, bridge.voiceStt(), bridge.voiceTts(), bridge.connected()), player);
    }

    private static JsonObject frame(String type, int request) {
        JsonObject frame = new JsonObject();
        frame.addProperty("v", 1);
        frame.addProperty("type", type);
        frame.addProperty("id", "v" + request);
        return frame;
    }

    private static int nextRequest() {
        return sequence++;
    }

    /** 宿主原样回带请求号（"v12"）；认不出来就当没这回事，不去猜是哪一个请求。 */
    private static int requestId(JsonObject frame) {
        String raw = WabJson.str(frame, "id");
        if (raw.length() < 2 || raw.charAt(0) != 'v') return 0;
        try {
            return Integer.parseInt(raw.substring(1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
