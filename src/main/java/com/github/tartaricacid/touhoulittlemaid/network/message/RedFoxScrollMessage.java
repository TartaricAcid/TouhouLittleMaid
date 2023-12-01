package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.RedFoxScrollScreen;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RedFoxScrollMessage {
    private final Map<String, List<RedFoxScrollData>> data;

    public RedFoxScrollMessage(Map<String, List<RedFoxScrollData>> data) {
        this.data = data;
    }

    public static void encode(RedFoxScrollMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.data.size());
        message.data.forEach((dim, scrollData) -> {
            buf.writeVarInt(scrollData.size());
            buf.writeUtf(dim);
            scrollData.forEach(data -> RedFoxScrollData.encode(data, buf));
        });
    }

    public static RedFoxScrollMessage decode(FriendlyByteBuf buf) {
        Map<String, List<RedFoxScrollData>> data = Maps.newHashMap();
        int dimLength = buf.readVarInt();
        for (int i = 0; i < dimLength; i++) {
            List<RedFoxScrollData> scrollData = Lists.newArrayList();
            int dataLength = buf.readVarInt();
            String dim = buf.readUtf();
            for (int j = 0; j < dataLength; j++) {
                scrollData.add(RedFoxScrollData.decode(buf));
            }
            data.put(dim, scrollData);
        }
        return new RedFoxScrollMessage(data);
    }

    public static void handle(RedFoxScrollMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(RedFoxScrollMessage message) {
        Minecraft.getInstance().setScreen(new RedFoxScrollScreen(message.data));
    }

    public static class RedFoxScrollData {
        private final BlockPos pos;
        private final Component name;
        private final long timestamp;

        public RedFoxScrollData(BlockPos pos, Component name, long timestamp) {
            this.pos = pos;
            this.name = name;
            this.timestamp = timestamp;
        }

        public static void encode(RedFoxScrollData data, FriendlyByteBuf buf) {
            buf.writeBlockPos(data.pos);
            buf.writeComponent(data.name);
            buf.writeLong(data.timestamp);
        }

        public static RedFoxScrollData decode(FriendlyByteBuf buf) {
            return new RedFoxScrollData(buf.readBlockPos(), buf.readComponent(), buf.readLong());
        }

        public BlockPos getPos() {
            return pos;
        }

        public Component getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
