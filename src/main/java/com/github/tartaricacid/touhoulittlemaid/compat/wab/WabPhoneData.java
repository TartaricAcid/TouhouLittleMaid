package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 逻辑服 → 手机界面的通用回执包：payload 是宿主 phone_reply 里 data 对象的 JSON 字符串。
 * 客户端只在当前屏幕就是 WabPhoneScreen 时原地刷新，否则安全忽略。
 */
public record WabPhoneData(String id, String action, boolean ok, String code, String error, String payload)
        implements CustomPacketPayload {
    public static final int MAX_BYTES = 30000;

    public static final CustomPacketPayload.Type<WabPhoneData> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("wab_phone_data"));

    public static final StreamCodec<FriendlyByteBuf, WabPhoneData> STREAM_CODEC = StreamCodec.of(
            (buf, m) -> {
                buf.writeUtf(m.id, MAX_BYTES);
                buf.writeUtf(m.action, MAX_BYTES);
                buf.writeBoolean(m.ok);
                buf.writeUtf(m.code, MAX_BYTES);
                buf.writeUtf(m.error, MAX_BYTES);
                buf.writeUtf(m.payload, MAX_BYTES);
            },
            buf -> new WabPhoneData(
                    buf.readUtf(MAX_BYTES),
                    buf.readUtf(MAX_BYTES),
                    buf.readBoolean(),
                    buf.readUtf(MAX_BYTES),
                    buf.readUtf(MAX_BYTES),
                    buf.readUtf(MAX_BYTES)));

    public WabPhoneData {
        id = fit(id);
        action = fit(action);
        code = fit(code);
        error = fit(error);
        payload = payload == null ? "" : fit(payload);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WabPhoneData message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> onHandle(message));
        }
    }

    /**
     * 客户端收尾必须待在带 @OnlyIn 的具名方法里：写在 lambda 里的话，
     * 专用服务器做类校验会顺着 WabPhoneScreen / Minecraft 摸到客户端类直接炸。
     */
    @OnlyIn(Dist.CLIENT)
    private static void onHandle(WabPhoneData message) {
        if (net.minecraft.client.Minecraft.getInstance().screen instanceof WabPhoneScreen screen) {
            screen.apply(message.action(), message.ok(), message.code(), message.error(), message.payload());
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
