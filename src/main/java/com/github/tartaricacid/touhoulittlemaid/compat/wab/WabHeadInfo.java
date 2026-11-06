package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 服务器把绑定女仆的紧凑数值（名字行 + 数值行）推给主人的客户端，供头顶渲染用。
 * 只推 WAB 有 stats 的女仆：没数值的画一片空白反而奇怪。
 */
public record WabHeadInfo(List<WabClientHeads.Entry> entries) implements CustomPacketPayload {
    public WabHeadInfo {
        entries = entries == null ? List.of() : entries;
    }

    public static final CustomPacketPayload.Type<WabHeadInfo> TYPE =
            new CustomPacketPayload.Type<>(getResourceLocation("wab_head_info"));

    public static final StreamCodec<FriendlyByteBuf, WabHeadInfo> STREAM_CODEC = StreamCodec.of((buf, message) -> {
        buf.writeVarInt(message.entries().size());
        for (WabClientHeads.Entry entry : message.entries()) {
            buf.writeUUID(entry.maid());
            buf.writeUtf(entry.l1(), 128);
            buf.writeUtf(entry.l2(), 128);
        }
    }, buf -> {
        int count = Math.min(buf.readVarInt(), 64);
        List<WabClientHeads.Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            entries.add(new WabClientHeads.Entry(buf.readUUID(), buf.readUtf(128), buf.readUtf(128)));
        }
        return new WabHeadInfo(entries);
    });

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WabHeadInfo message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> WabClientHeads.update(message.entries()));
        }
    }
}
