package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record OpenMaidAIChatMessage(int entityId) {
    public OpenMaidAIChatMessage(EntityMaid maid) {
        this(maid.getId());
    }

    public static void encode(OpenMaidAIChatMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityId);
    }

    public static OpenMaidAIChatMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        return new OpenMaidAIChatMessage(entityId);
    }

    public static void handle(OpenMaidAIChatMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> handle(message, contextSupplier.get().getSender()));
        }
        context.setPacketHandled(true);
    }

    private static void handle(OpenMaidAIChatMessage message, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        Entity entity = player.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid) {
            // 发送同步信息（包含 Token 用量）
            NetworkHandler.sendToClientPlayer(new SyncMaidAIDataMessage(maid, player), player);
        }
    }
}
