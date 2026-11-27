package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * 服务器告诉主人客户端：哪些女仆绑了 WAB 角色、WAB 现在接不接听写和配音、桥还挂着没有。
 * 客户端据此决定按 X 时是走 WAB 还是走游戏自己的 STT 站点，也据此决定聊天按键要不要放行，
 * 以及设置页上该把哪几项标成「已由 WarmAsBefore 承担」。
 */
public class WabBindingSync {
    private static final int MAX_MAIDS = 256;

    private final List<UUID> maids;
    private final boolean stt;
    private final boolean tts;
    private final boolean chat;

    public WabBindingSync(List<UUID> maids, boolean stt, boolean tts, boolean chat) {
        this.maids = maids == null ? Collections.emptyList() : List.copyOf(maids);
        this.stt = stt;
        this.tts = tts;
        this.chat = chat;
    }

    public List<UUID> maids() {
        return maids;
    }

    public boolean stt() {
        return stt;
    }

    public boolean tts() {
        return tts;
    }

    public boolean chat() {
        return chat;
    }

    public static void encode(WabBindingSync message, FriendlyByteBuf buf) {
        int count = Math.min(message.maids.size(), MAX_MAIDS);
        buf.writeVarInt(count);
        for (int i = 0; i < count; i++) {
            buf.writeUUID(message.maids.get(i));
        }
        buf.writeBoolean(message.stt);
        buf.writeBoolean(message.tts);
        buf.writeBoolean(message.chat);
    }

    public static WabBindingSync decode(FriendlyByteBuf buf) {
        int count = Math.min(buf.readVarInt(), MAX_MAIDS);
        List<UUID> maids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            maids.add(buf.readUUID());
        }
        return new WabBindingSync(maids, buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(WabBindingSync message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> WabClientState.apply(message));
        }
        context.setPacketHandled(true);
    }
}
