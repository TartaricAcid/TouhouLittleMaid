package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 详细信息的整包回执：payload 就是 {@link WabDetailsRequest} 拼好的 JsonObject，
 * 客户端原样交给 {@link WabDetailsScreen} 分页展示。
 */
public class WabDetailsReply {
    private static final int MAX_CHARS = 65536;

    private final JsonObject data;

    public WabDetailsReply(JsonObject data) {
        this.data = data == null ? new JsonObject() : data;
    }

    public static void encode(WabDetailsReply message, FriendlyByteBuf buf) {
        buf.writeUtf(message.data.toString(), MAX_CHARS);
    }

    public static WabDetailsReply decode(FriendlyByteBuf buf) {
        String json = buf.readUtf(MAX_CHARS);
        JsonObject data = new JsonObject();
        try {
            data = JsonParser.parseString(json).getAsJsonObject();
        } catch (Exception ignored) {
        }
        return new WabDetailsReply(data);
    }

    public static void handle(WabDetailsReply message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    /**
     * 客户端收尾必须待在带 @OnlyIn 的具名方法里：写在 lambda 里的话，合成方法剥不掉，
     * 专用服务器做类校验时会顺着 new WabDetailsScreen 摸到 Screen 直接炸。
     */
    @OnlyIn(Dist.CLIENT)
    private static void onHandle(WabDetailsReply message) {
        net.minecraft.client.Minecraft.getInstance().setScreen(new WabDetailsScreen(message.data));
    }
}
