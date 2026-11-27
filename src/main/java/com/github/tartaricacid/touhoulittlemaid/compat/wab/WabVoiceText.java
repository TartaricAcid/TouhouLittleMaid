package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** WAB 对一段录音的听写结果。失败也回一帧，客户端才不会一直等着那句「正在识别」。 */
public class WabVoiceText {
    private final int maid;
    private final boolean ok;
    private final String text;
    private final String error;

    public WabVoiceText(int maid, boolean ok, String text, String error) {
        this.maid = maid;
        this.ok = ok;
        this.text = text == null ? "" : text;
        this.error = error == null ? "" : error;
    }

    public int maid() {
        return maid;
    }

    public boolean ok() {
        return ok;
    }

    public String text() {
        return text;
    }

    public String error() {
        return error;
    }

    public static void encode(WabVoiceText message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.maid);
        buf.writeBoolean(message.ok);
        buf.writeUtf(message.text, 2000);
        buf.writeUtf(message.error, 400);
    }

    public static WabVoiceText decode(FriendlyByteBuf buf) {
        return new WabVoiceText(buf.readVarInt(), buf.readBoolean(), buf.readUtf(2000), buf.readUtf(400));
    }

    public static void handle(WabVoiceText message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handleClient(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(WabVoiceText message) {
        WabVoiceClient.onText(message);
    }
}
