package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 客户端把画面交回服务器，由 {@link WabBridge#onPhoto} 转成一帧 photo 交给 WAB。
 * 字节在这里就钳一次：包超过上限会被 vanilla 直接拒掉，那比拍不到更难看。
 */
public class WabPhotoReply {
    private static final int MAX_BYTES = 1_200_000;

    private final int request;
    private final byte[] png;
    private final int width;
    private final int height;
    private final String error;

    public WabPhotoReply(int request, byte[] png, int width, int height, String error) {
        this.request = request;
        this.png = png == null ? new byte[0] : png;
        this.width = width;
        this.height = height;
        this.error = error == null ? "" : error;
    }

    public static void encode(WabPhotoReply message, FriendlyByteBuf buf) {
        boolean tooBig = message.png.length > MAX_BYTES;
        buf.writeInt(message.request);
        buf.writeInt(tooBig ? 0 : message.width);
        buf.writeInt(tooBig ? 0 : message.height);
        buf.writeUtf(tooBig ? "这张图压完还有 " + (message.png.length / 1024) + " KB，太大了，没往上传。"
                : message.error, 200);
        buf.writeByteArray(tooBig ? new byte[0] : message.png);
    }

    public static WabPhotoReply decode(FriendlyByteBuf buf) {
        int request = buf.readInt();
        int width = buf.readInt();
        int height = buf.readInt();
        String error = buf.readUtf(200);
        byte[] png = buf.readByteArray(1_000_000);
        return new WabPhotoReply(request, png, width, height, error);
    }

    public static void handle(WabPhotoReply message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                WabBridge bridge = WabBridge.get();
                if (bridge != null) {
                    bridge.onPhoto(message.request, message.png, message.width, message.height, message.error);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
