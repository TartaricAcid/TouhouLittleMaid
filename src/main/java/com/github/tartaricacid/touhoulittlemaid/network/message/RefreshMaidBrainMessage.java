package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RefreshMaidBrainMessage(int entityId) {
    public static void encode(RefreshMaidBrainMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
    }

    public static RefreshMaidBrainMessage decode(FriendlyByteBuf buf) {
        return new RefreshMaidBrainMessage(buf.readInt());
    }

    public static void handle(RefreshMaidBrainMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    maid.refreshBrain((ServerLevel) sender.level);
                }
            });
        }
        context.setPacketHandled(true);
    }
}