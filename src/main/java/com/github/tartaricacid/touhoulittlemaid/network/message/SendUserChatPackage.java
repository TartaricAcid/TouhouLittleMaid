package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.ChatClientInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record SendUserChatPackage(int maidId, String message,
                                  ChatClientInfo clientInfo) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendUserChatPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("send_user_chat"));
    public static final StreamCodec<ByteBuf, SendUserChatPackage> STREAM_CODEC = new StreamCodec<ByteBuf, SendUserChatPackage>() {
        @Override
        public SendUserChatPackage decode(ByteBuf byteBuf) {
            FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
            int maidId = buf.readVarInt();
            String message = buf.readUtf();
            ChatClientInfo clientInfo = ChatClientInfo.decode(buf);
            return new SendUserChatPackage(maidId, message, clientInfo);
        }

        @Override
        public void encode(ByteBuf byteBuf, SendUserChatPackage message) {
            FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
            buf.writeVarInt(message.maidId);
            buf.writeUtf(message.message);
            message.clientInfo.encode(buf);
        }
    };

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SendUserChatPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> onHandle(message, context));
        }
    }

    private static void onHandle(SendUserChatPackage message, IPayloadContext context) {
        ServerPlayer sender = (ServerPlayer) context.player();
        Entity entity = sender.level.getEntity(message.maidId);
        if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender) && maid.isAlive()) {
            maid.getAiChatManager().chat(message.message, message.clientInfo, sender);
        }
    }
}
