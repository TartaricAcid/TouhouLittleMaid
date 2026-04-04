package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ClearMaidAIDataMessage {
    private static final int ALL_MSG_INDEX = -1;
    private final int entityId;
    private final int msgIndex;

    public ClearMaidAIDataMessage(int entityId, int msgIndex) {
        this.entityId = entityId;
        this.msgIndex = msgIndex;
    }

    public ClearMaidAIDataMessage(int entityId) {
        this(entityId, ALL_MSG_INDEX);
    }

    public static void encode(ClearMaidAIDataMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityId);
        buf.writeVarInt(message.msgIndex);
    }

    public static ClearMaidAIDataMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        int msgIndex = buf.readVarInt();
        return new ClearMaidAIDataMessage(entityId, msgIndex);
    }

    public static void handle(ClearMaidAIDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> handle(message, contextSupplier.get().getSender()));
        }
        context.setPacketHandled(true);
    }

    private static void handle(ClearMaidAIDataMessage message, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        Entity entity = player.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
            if (message.msgIndex == ALL_MSG_INDEX) {
                maid.getAiChatManager().clearAllChatMemory();
            }
        }
    }
}
