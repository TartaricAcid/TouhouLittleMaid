package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 详细信息的整包回执：payload 就是 {@link WabDetailsRequest} 拼好的 JsonObject，
 * 客户端原样交给 {@link WabDetailsScreen} 分页展示。
 */
public record WabDetailsReply(JsonObject data) implements CustomPacketPayload {
    private static final int MAX_CHARS = 65536;

    public WabDetailsReply {
        data = data == null ? new JsonObject() : data;
    }

    public static final CustomPacketPayload.Type<WabDetailsReply> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("wab_details_reply"));

    public static final StreamCodec<ByteBuf, WabDetailsReply> STREAM_CODEC = StreamCodec.of(
            (buf, message) -> buf.writeUtf(message.data().toString(), MAX_CHARS),
            buf -> {
                JsonObject data = new JsonObject();
                try {
                    data = JsonParser.parseString(buf.readUtf(MAX_CHARS)).getAsJsonObject();
                } catch (Exception ignored) {
                }
                return new WabDetailsReply(data);
            });

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WabDetailsReply message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> open(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void open(WabDetailsReply message) {
        Minecraft.getInstance().setScreen(new WabDetailsScreen(message.data()));
    }
}
