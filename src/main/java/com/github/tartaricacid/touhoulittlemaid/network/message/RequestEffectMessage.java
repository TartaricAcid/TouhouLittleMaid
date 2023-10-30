package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestEffectMessage {
    private final int id;

    public RequestEffectMessage(int id) {
        this.id = id;
    }

    public static void encode(RequestEffectMessage message, PacketBuffer buf) {
        buf.writeInt(message.id);
    }

    public static RequestEffectMessage decode(PacketBuffer buf) {
        return new RequestEffectMessage(buf.readInt());
    }

    public static void handle(RequestEffectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid && ((EntityMaid) entity).isOwnedBy(sender)) {
                    EntityMaid maid = (EntityMaid) entity;
                    SendEffectMessage sendEffectMessage = new SendEffectMessage(message.id, maid.getActiveEffects());
                    NetworkHandler.sendToClientPlayer(sendEffectMessage, sender);
                }
            });
        }
        context.setPacketHandled(true);
    }
}