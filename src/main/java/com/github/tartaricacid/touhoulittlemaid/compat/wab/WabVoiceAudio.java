package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * WAB 合成好的音频，按 512 KiB 一块转到主人客户端，客户端拼完整再播。
 * 不整个塞一帧：WAV 不压缩，长一点的回复会把单包顶到网络层的上限之外。
 */
public class WabVoiceAudio {
    private static final int MAX_BYTES = 1_000_000;

    private final int maid;
    private final int request;
    private final int part;
    private final int parts;
    private final byte[] data;

    public WabVoiceAudio(int maid, int request, int part, int parts, byte[] data) {
        this.maid = maid;
        this.request = request;
        this.part = part;
        this.parts = parts;
        this.data = data == null ? new byte[0] : data;
    }

    public int maid() {
        return maid;
    }

    public int request() {
        return request;
    }

    public int part() {
        return part;
    }

    public int parts() {
        return parts;
    }

    public byte[] data() {
        return data;
    }

    public static void encode(WabVoiceAudio message, FriendlyByteBuf buf) {
        boolean tooBig = message.data.length > MAX_BYTES;
        buf.writeVarInt(message.maid);
        buf.writeVarInt(message.request);
        buf.writeVarInt(message.part);
        buf.writeVarInt(message.parts);
        buf.writeByteArray(tooBig ? new byte[0] : message.data);
    }

    public static WabVoiceAudio decode(FriendlyByteBuf buf) {
        int maid = buf.readVarInt();
        int request = buf.readVarInt();
        int part = buf.readVarInt();
        int parts = buf.readVarInt();
        byte[] data = buf.readByteArray(MAX_BYTES);
        return new WabVoiceAudio(maid, request, part, parts, data);
    }

    public static void handle(WabVoiceAudio message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handleClient(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(WabVoiceAudio message) {
        WabVoiceClient.onAudio(message);
    }
}
