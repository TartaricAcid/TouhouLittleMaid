package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CuriosS2CUpdateMessage(int page) {
    public static void encode(CuriosS2CUpdateMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.page());
    }

    public static CuriosS2CUpdateMessage decode(FriendlyByteBuf buf) {
        return new CuriosS2CUpdateMessage(buf.readVarInt());
    }

    public static void handle(CuriosS2CUpdateMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> CuriosCompat.clientUpdatePage(message.page()));
        }
        context.setPacketHandled(true);
    }
}
