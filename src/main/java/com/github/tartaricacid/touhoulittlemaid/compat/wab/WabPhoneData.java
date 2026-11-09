package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

/**
 * 逻辑服 → 手机界面的通用回执包：payload 是宿主 phone_reply 里 data 对象的 JSON 字符串。
 * 客户端只在当前屏幕就是 WabPhoneScreen 时原地刷新，否则安全忽略。
 */
public class WabPhoneData {
    public static final int MAX_BYTES = 30000;

    private final String id;
    private final String action;
    private final boolean ok;
    private final String code;
    private final String error;
    private final String payload;

    public WabPhoneData(String id, String action, boolean ok, String code, String error, String payload) {
        this.id = fit(id);
        this.action = fit(action);
        this.ok = ok;
        this.code = fit(code);
        this.error = fit(error);
        this.payload = payload == null ? "" : fit(payload);
    }

    public String id() {
        return id;
    }

    public String action() {
        return action;
    }

    public boolean ok() {
        return ok;
    }

    public String code() {
        return code;
    }

    public String error() {
        return error;
    }

    public String payload() {
        return payload;
    }

    public static void encode(WabPhoneData message, FriendlyByteBuf buf) {
        buf.writeUtf(message.id, MAX_BYTES);
        buf.writeUtf(message.action, MAX_BYTES);
        buf.writeBoolean(message.ok);
        buf.writeUtf(message.code, MAX_BYTES);
        buf.writeUtf(message.error, MAX_BYTES);
        buf.writeUtf(message.payload, MAX_BYTES);
    }

    public static WabPhoneData decode(FriendlyByteBuf buf) {
        return new WabPhoneData(
                buf.readUtf(MAX_BYTES),
                buf.readUtf(MAX_BYTES),
                buf.readBoolean(),
                buf.readUtf(MAX_BYTES),
                buf.readUtf(MAX_BYTES),
                buf.readUtf(MAX_BYTES));
    }

    public static void handle(WabPhoneData message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    /**
     * 客户端收尾必须待在带 @OnlyIn 的具名方法里：写在 lambda 里的话，
     * 专用服务器做类校验会顺着 WabPhoneScreen / Minecraft 摸到客户端类直接炸。
     */
    @OnlyIn(Dist.CLIENT)
    private static void onHandle(WabPhoneData message) {
        if (net.minecraft.client.Minecraft.getInstance().screen instanceof WabPhoneScreen screen) {
            screen.apply(message.action, message.ok, message.code, message.error, message.payload);
        }
    }

    /** 按 UTF-8 字节砍到 writeUtf 的上限内，且不切在半个多字节字符上。两个包类共用。 */
    static String fit(String text) {
        if (text == null) {
            return "";
        }
        if (text.getBytes(StandardCharsets.UTF_8).length <= MAX_BYTES) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        int used = 0;
        for (int i = 0; i < text.length(); ) {
            char[] chars = Character.toChars(text.codePointAt(i));
            int bytes = new String(chars).getBytes(StandardCharsets.UTF_8).length;
            if (used + bytes > MAX_BYTES) {
                break;
            }
            sb.append(chars);
            used += bytes;
            i += chars.length;
        }
        return sb.toString();
    }
}
