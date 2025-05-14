package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatSerializable;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SaveMaidAIDataMessage {
    private final int entityId;
    private final MaidAIChatSerializable data;

    public SaveMaidAIDataMessage(int entityId, MaidAIChatSerializable data) {
        this.entityId = entityId;
        this.data = data;
    }

    public static void encode(SaveMaidAIDataMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        message.data.encode(buf);
    }

    public static SaveMaidAIDataMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        MaidAIChatSerializable data = new MaidAIChatSerializable();
        data.decode(buf);
        return new SaveMaidAIDataMessage(entityId, data);
    }

    public static void handle(SaveMaidAIDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> handle(message, contextSupplier.get().getSender()));
        }
        context.setPacketHandled(true);
    }

    private static void handle(SaveMaidAIDataMessage message, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        Entity entity = player.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
            maid.getAiChatManager().copyFrom(message.data);
        }
    }
}
