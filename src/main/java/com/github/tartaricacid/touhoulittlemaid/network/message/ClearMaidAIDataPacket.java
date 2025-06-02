package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Nullable;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record ClearMaidAIDataPacket(int entityId, int msgIndex) implements CustomPacketPayload {
    private static final int ALL_MSG_INDEX = -1;
    public static final CustomPacketPayload.Type<ClearMaidAIDataPacket> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("clear_maid_ai_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClearMaidAIDataPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClearMaidAIDataPacket::entityId,
            ByteBufCodecs.VAR_INT, ClearMaidAIDataPacket::msgIndex,
            ClearMaidAIDataPacket::new
    );

    public ClearMaidAIDataPacket(int entityId) {
        this(entityId, ALL_MSG_INDEX);
    }

    public static void handle(ClearMaidAIDataPacket message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> handle(message, (ServerPlayer) context.player()));
        }
    }

    private static void handle(ClearMaidAIDataPacket message, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        Entity entity = player.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
            if (message.msgIndex == ALL_MSG_INDEX) {
                maid.getAiChatManager().getHistory().getDeque().clear();
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
