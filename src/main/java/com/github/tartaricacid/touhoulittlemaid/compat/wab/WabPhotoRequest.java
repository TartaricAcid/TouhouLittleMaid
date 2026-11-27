package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 「让她拍一张」：画面只有正在渲染这个世界的客户端才有，服务器上没有帧缓冲。
 * 所以这一帧从服务器发给主人客户端，客户端截屏、缩小、编码成 PNG，再用 {@link WabPhotoReply} 送回服务器。
 * 全程只在 WAB 主动要求时发生，桥上没连着的时候这条代码路径根本不会被走到。
 */
public class WabPhotoRequest {
    /** 再大就只是浪费：给多模态模型看的图不需要原生分辨率，而一帧桥上的报文有几 MB 就很危险了。 */
    private static final int MAX_WIDTH = 960;
    private static final int MIN_WIDTH = 160;

    private final int request;
    private final int width;

    public WabPhotoRequest(int request, int width) {
        this.request = request;
        this.width = Math.max(MIN_WIDTH, Math.min(MAX_WIDTH, width));
    }

    public static void encode(WabPhotoRequest message, FriendlyByteBuf buf) {
        buf.writeInt(message.request);
        buf.writeInt(message.width);
    }

    public static WabPhotoRequest decode(FriendlyByteBuf buf) {
        return new WabPhotoRequest(buf.readInt(), buf.readInt());
    }

    public static void handle(WabPhotoRequest message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            // 截帧缓冲必须在渲染线程上，enqueueWork 正好把它排到客户端主线程；裸起一个线程读 GL 会直接崩。
            context.enqueueWork(() -> capture(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void capture(WabPhotoRequest message) {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) {
                send(message.request, null, 0, 0, "客户端此刻不在这个世界里，拍不到画面。");
                return;
            }
            NativeImage full = Screenshot.takeScreenshot(mc.getMainRenderTarget());
            NativeImage small;
            try {
                small = shrink(full, message.width);
            } finally {
                full.close();
            }
            try {
                byte[] png = small.asByteArray();
                send(message.request, png, small.getWidth(), small.getHeight(), null);
            } finally {
                small.close();
            }
        } catch (Throwable t) {
            // 别的模组改了帧缓冲、或者驱动不配合：说清楚，别让 WAB 一直等一个永不会来的回执。
            send(message.request, null, 0, 0, "客户端截屏失败：" + t.getClass().getSimpleName());
        }
    }

    /**
     * 只按最近邻缩到目标宽度。1.20.1 的 NativeImage 没有 resize，
     * 而逐点取像素比把整屏拷进 int[] 再算跨步稳妥——格式不同的图跨步会错位。
     */
    @OnlyIn(Dist.CLIENT)
    private static NativeImage shrink(NativeImage src, int targetWidth) {
        double scale = Math.min(1.0, (double) targetWidth / Math.max(1, src.getWidth()));
        int width = Math.max(1, (int) Math.round(src.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(src.getHeight() * scale));
        NativeImage out = new NativeImage(width, height, false);
        if (width == src.getWidth() && height == src.getHeight()) {
            out.copyFrom(src);
            return out;
        }
        for (int y = 0; y < height; y++) {
            int sy = Math.min(src.getHeight() - 1, (int) ((double) y / scale));
            for (int x = 0; x < width; x++) {
                int sx = Math.min(src.getWidth() - 1, (int) ((double) x / scale));
                out.setPixelRGBA(x, y, src.getPixelRGBA(sx, sy));
            }
        }
        return out;
    }

    private static void send(int request, byte[] png, int width, int height, String error) {
        com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler.CHANNEL
                .sendToServer(new WabPhotoReply(request, png, width, height, error));
    }
}
