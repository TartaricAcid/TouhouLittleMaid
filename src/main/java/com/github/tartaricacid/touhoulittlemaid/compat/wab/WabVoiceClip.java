package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 客户端把一段录音（WAV）交给服务器，由 {@link WabVoice#requestStt} 转成一帧 stt 问 WAB。
 * 客户端不直接连 WAB：桥的 TCP 会话在服务端，专用服务器上客户端也碰不到那个回环地址。
 */
public class WabVoiceClip {
    private static final int MAX_BYTES = 1_500_000;

    private final int maid;
    private final byte[] wav;

    public WabVoiceClip(int maid, byte[] wav) {
        this.maid = maid;
        this.wav = wav == null ? new byte[0] : wav;
    }

    public int maid() {
        return maid;
    }

    public byte[] wav() {
        return wav;
    }

    public static void encode(WabVoiceClip message, FriendlyByteBuf buf) {
        boolean tooBig = message.wav.length > MAX_BYTES;
        buf.writeVarInt(message.maid);
        buf.writeByteArray(tooBig ? new byte[0] : message.wav);
    }

    public static WabVoiceClip decode(FriendlyByteBuf buf) {
        int maid = buf.readVarInt();
        byte[] wav = buf.readByteArray(MAX_BYTES);
        return new WabVoiceClip(maid, wav);
    }

    public static void handle(WabVoiceClip message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) return;
                Entity entity = sender.level.getEntity(message.maid);
                if (!(entity instanceof EntityMaid maid) || !maid.isOwnedBy(sender) || !maid.isAlive()) return;
                if (message.wav.length == 0) {
                    NetworkHandler.sendToClientPlayer(new WabVoiceText(message.maid, false, "", "这段录音是空的。"), sender);
                    return;
                }
                WabBridge bridge = WabBridge.get();
                if (bridge == null) {
                    NetworkHandler.sendToClientPlayer(new WabVoiceText(message.maid, false, "", "WAB 没有连接。"), sender);
                    return;
                }
                WabVoice.requestStt(bridge, maid, sender, message.wav);
            });
        }
        context.setPacketHandled(true);
    }
}
