package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidConfigManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MaidSubConfigMessage {
    private final int id;
    public MaidConfigManager.SyncNetwork syncNetwork;

    public MaidSubConfigMessage(int id, MaidConfigManager.SyncNetwork syncNetwork) {
        this.id = id;
        this.syncNetwork = syncNetwork;
    }

    public static void encode(MaidSubConfigMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.id);
        MaidConfigManager.SyncNetwork.encode(message.syncNetwork, buf);
    }

    public static MaidSubConfigMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        MaidConfigManager.SyncNetwork network = MaidConfigManager.SyncNetwork.decode(buf);
        return new MaidSubConfigMessage(entityId, network);
    }

    public static void handle(MaidSubConfigMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    MaidConfigManager.SyncNetwork.handle(message.syncNetwork, maid);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
