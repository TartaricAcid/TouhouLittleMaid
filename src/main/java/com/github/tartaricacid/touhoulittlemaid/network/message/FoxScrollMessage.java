package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.FoxScrollScreen;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FoxScrollMessage {
    private final Map<String, List<FoxScrollData>> data;

    public FoxScrollMessage(Map<String, List<FoxScrollData>> data) {
        this.data = data;
    }

    public static void encode(FoxScrollMessage message, PacketBuffer buf) {
        buf.writeVarInt(message.data.size());
        message.data.forEach((dim, scrollData) -> {
            buf.writeVarInt(scrollData.size());
            buf.writeUtf(dim);
            scrollData.forEach(data -> FoxScrollData.encode(data, buf));
        });
    }

    public static FoxScrollMessage decode(PacketBuffer buf) {
        Map<String, List<FoxScrollData>> data = Maps.newHashMap();
        int dimLength = buf.readVarInt();
        for (int i = 0; i < dimLength; i++) {
            List<FoxScrollData> scrollData = Lists.newArrayList();
            int dataLength = buf.readVarInt();
            String dim = buf.readUtf();
            for (int j = 0; j < dataLength; j++) {
                scrollData.add(FoxScrollData.decode(buf));
            }
            data.put(dim, scrollData);
        }
        return new FoxScrollMessage(data);
    }

    public static void handle(FoxScrollMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(FoxScrollMessage message) {
        Minecraft.getInstance().setScreen(new FoxScrollScreen(message.data));
    }

    public static class FoxScrollData {
        private final BlockPos pos;
        private final ITextComponent name;
        private final long timestamp;

        public FoxScrollData(BlockPos pos, ITextComponent name, long timestamp) {
            this.pos = pos;
            this.name = name;
            this.timestamp = timestamp;
        }

        public static void encode(FoxScrollData data, PacketBuffer buf) {
            buf.writeBlockPos(data.pos);
            buf.writeComponent(data.name);
            buf.writeLong(data.timestamp);
        }

        public static FoxScrollData decode(PacketBuffer buf) {
            return new FoxScrollData(buf.readBlockPos(), buf.readComponent(), buf.readLong());
        }

        public BlockPos getPos() {
            return pos;
        }

        public ITextComponent getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
