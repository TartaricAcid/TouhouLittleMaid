package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 手机界面 → 逻辑服的通用动作包：contacts / messages / send / add_friend / mark_read /
 * remove_friend / whoami 全走这一对包，游戏侧只转发，不碰任何业务逻辑。
 * params 是 JSON 字符串；服务端把它还原成 JsonObject 交给 WabBridge 发往宿主。
 */
public record WabPhoneAction(String id, String action, String params) implements CustomPacketPayload {
    /** writeUtf 的字节上限留一点余量，别让超长文本把整条通道打挂。 */
    public static final int MAX_BYTES = WabPhoneData.MAX_BYTES;
    private static final AtomicInteger SEQ = new AtomicInteger();

    public static final CustomPacketPayload.Type<WabPhoneAction> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("wab_phone_action"));

    public static final StreamCodec<FriendlyByteBuf, WabPhoneAction> STREAM_CODEC = StreamCodec.of(
            (buf, m) -> {
                buf.writeUtf(m.id, MAX_BYTES);
                buf.writeUtf(m.action, MAX_BYTES);
                buf.writeUtf(m.params, MAX_BYTES);
            },
            buf -> new WabPhoneAction(buf.readUtf(MAX_BYTES), buf.readUtf(MAX_BYTES), buf.readUtf(MAX_BYTES)));

    public WabPhoneAction {
        id = WabPhoneData.fit(id);
        action = WabPhoneData.fit(action);
        params = params == null ? "{}" : WabPhoneData.fit(params);
    }

    /** 客户端给每个新请求发一个递增编号，宿主在 phone_reply 里原样送回。 */
    public static String nextId() {
        return "p-" + SEQ.incrementAndGet();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WabPhoneAction message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                if (!(context.player() instanceof ServerPlayer sender)) {
                    return;
                }
                WabBridge bridge = WabBridge.get();
                if (bridge == null) {
                    // WAB 没启用：也回一个 not_connected，界面不会一直转圈。
                    NetworkHandler.sendToClientPlayer(new WabPhoneData(message.id(), message.action(),
                            false, "not_connected", "WAB 未连接", ""), sender);
                    return;
                }
                JsonObject params;
                try {
                    params = JsonParser.parseString(message.params()).getAsJsonObject();
                } catch (Exception ignored) {
                    params = new JsonObject();
                }
                bridge.sendPhoneRequest(sender, message.id(), message.action(), params);
            });
        }
    }
}
