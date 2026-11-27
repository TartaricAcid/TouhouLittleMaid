package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * 服务器把绑定女仆的紧凑数值（名字行 + 数值行）推给主人的客户端，供头顶渲染用。
 * 只推 WAB 有 stats 的女仆：没数值的画一片空白反而奇怪。
 */
public class WabHeadInfo {
    private final List<WabClientHeads.Entry> entries;

    public WabHeadInfo(List<WabClientHeads.Entry> entries) {
        this.entries = entries == null ? List.of() : entries;
    }

    public static void encode(WabHeadInfo message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.entries.size());
        for (WabClientHeads.Entry entry : message.entries) {
            buf.writeUUID(entry.maid());
            buf.writeUtf(entry.l1(), 128);
            buf.writeUtf(entry.l2(), 128);
        }
    }

    public static WabHeadInfo decode(FriendlyByteBuf buf) {
        int count = Math.min(buf.readVarInt(), 64);
        List<WabClientHeads.Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            UUID maid = buf.readUUID();
            entries.add(new WabClientHeads.Entry(maid, buf.readUtf(128), buf.readUtf(128)));
        }
        return new WabHeadInfo(entries);
    }

    public static void handle(WabHeadInfo message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> WabClientHeads.update(message.entries));
        }
        context.setPacketHandled(true);
    }
}
