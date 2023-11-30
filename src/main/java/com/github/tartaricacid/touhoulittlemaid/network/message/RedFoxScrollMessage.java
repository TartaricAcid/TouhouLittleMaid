package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.RedFoxScrollScreen;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RedFoxScrollMessage {
    private final Map<String, List<BlockPos>> data;

    public RedFoxScrollMessage(Map<String, List<BlockPos>> data) {
        this.data = data;
    }

    public static void encode(RedFoxScrollMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.data.size());
        message.data.forEach((dim, posList) -> {
            buf.writeVarInt(posList.size());
            buf.writeUtf(dim);
            posList.forEach(buf::writeBlockPos);
        });
    }

    public static RedFoxScrollMessage decode(FriendlyByteBuf buf) {
        Map<String, List<BlockPos>> data = Maps.newHashMap();
        int dimLength = buf.readVarInt();
        for (int i = 0; i < dimLength; i++) {
            List<BlockPos> posList = Lists.newArrayList();
            int posLength = buf.readVarInt();
            String dim = buf.readUtf();
            for (int j = 0; j < posLength; j++) {
                posList.add(buf.readBlockPos());
            }
            data.put(dim, posList);
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
}
