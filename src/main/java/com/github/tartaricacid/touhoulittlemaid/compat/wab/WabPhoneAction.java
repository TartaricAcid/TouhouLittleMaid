package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 手机界面 → 逻辑服的通用动作包：contacts / messages / send / add_friend / mark_read /
 * remove_friend / whoami 全走这一对包，游戏侧只转发，不碰任何业务逻辑。
 * params 是 JSON 字符串；服务端把它还原成 JsonObject 交给 WabBridge 发往宿主。
 */
public class WabPhoneAction {
    /** writeUtf 的字节上限留一点余量，别让超长文本把整条通道打挂。 */
    public static final int MAX_BYTES = WabPhoneData.MAX_BYTES;
    private static final AtomicInteger SEQ = new AtomicInteger();

    private final String id;
    private final String action;
    private final String params;

    public WabPhoneAction(String id, String action, String params) {
        this.id = WabPhoneData.fit(id);
        this.action = WabPhoneData.fit(action);
        this.params = params == null ? "{}" : WabPhoneData.fit(params);
    }

    /** 客户端给每个新请求发一个递增编号，宿主在 phone_reply 里原样送回。 */
    public static String nextId() {
        return "p-" + SEQ.incrementAndGet();
    }

    public String id() {
        return id;
    }

    public String action() {
        return action;
    }

    public String params() {
        return params;
    }

    public static void encode(WabPhoneAction message, FriendlyByteBuf buf) {
        buf.writeUtf(message.id, MAX_BYTES);
        buf.writeUtf(message.action, MAX_BYTES);
        buf.writeUtf(message.params, MAX_BYTES);
    }

    public static WabPhoneAction decode(FriendlyByteBuf buf) {
        return new WabPhoneAction(buf.readUtf(MAX_BYTES), buf.readUtf(MAX_BYTES), buf.readUtf(MAX_BYTES));
    }

    public static void handle(WabPhoneAction message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                WabBridge bridge = WabBridge.get();
                if (bridge == null) {
                    // WAB 没启用：也回一个 not_connected，界面不会一直转圈。
                    NetworkHandler.sendToClientPlayer(new WabPhoneData(message.id, message.action,
                            false, "not_connected", "WAB 未连接", ""), sender);
                    return;
                }
                JsonObject params;
                try {
                    params = JsonParser.parseString(message.params).getAsJsonObject();
                } catch (Exception ignored) {
                    params = new JsonObject();
                }
                bridge.sendPhoneRequest(sender, message.id, message.action, params);
            });
        }
        context.setPacketHandled(true);
    }
}
