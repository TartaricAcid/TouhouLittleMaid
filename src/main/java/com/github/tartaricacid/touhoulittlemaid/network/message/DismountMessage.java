package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DismountMessage {
    public static final int DISMOUNT_BROOM = 1;

    private final int action;

    public DismountMessage(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public static DismountMessage decode(FriendlyByteBuf buf) {
        int action = buf.readInt();
        return new DismountMessage(action);
    }

    public static void encode(DismountMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.action);
    }

    public static void handle(DismountMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            ServerPlayer sender = context.getSender();
            if (sender == null) {
                return;
            }
            context.enqueueWork(() -> onHandle(message, sender));
        }
        context.setPacketHandled(true);
    }

    private static void onHandle(DismountMessage message, ServerPlayer sender) {
        // 处理卸载扫帚的逻辑
        if (message.getAction() == DISMOUNT_BROOM && sender.getVehicle() instanceof EntityBroom) {
            sender.stopRiding();
        }
    }
}
