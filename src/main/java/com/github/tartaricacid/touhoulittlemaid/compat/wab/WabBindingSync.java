package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 服务器告诉主人客户端：哪些女仆绑了 WAB 角色、WAB 现在接不接听写和配音、桥还挂着没有。
 * 客户端据此决定按 X 时是走 WAB 还是走游戏自己的 STT 站点，也据此决定聊天按键要不要放行，
 * 以及设置页上该把哪几项标成「已由 WarmAsBefore 承担」。
 */
public record WabBindingSync(List<UUID> maids, boolean stt, boolean tts, boolean chat) implements CustomPacketPayload {
    private static final int MAX_MAIDS = 256;

    public static final CustomPacketPayload.Type<WabBindingSync> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("wab_binding_sync"));

    public static final StreamCodec<FriendlyByteBuf, WabBindingSync> STREAM_CODEC = StreamCodec.of((buf, message) -> {
        List<UUID> maids = message.maids();
        int count = Math.min(maids.size(), MAX_MAIDS);
        buf.writeVarInt(count);
        for (int i = 0; i < count; i++) {
            buf.writeUUID(maids.get(i));
        }
        buf.writeBoolean(message.stt());
        buf.writeBoolean(message.tts());
        buf.writeBoolean(message.chat());
    }, buf -> {
        int count = Math.min(buf.readVarInt(), MAX_MAIDS);
        List<UUID> maids = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            maids.add(buf.readUUID());
        }
        return new WabBindingSync(maids, buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    });

    public WabBindingSync {
        maids = maids == null ? Collections.emptyList() : List.copyOf(maids);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WabBindingSync message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> WabClientState.apply(message));
        }
    }
}
