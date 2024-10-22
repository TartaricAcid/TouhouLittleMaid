package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidConfigManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record MaidSubConfigPackage(int id, MaidConfigManager.SyncNetwork syncNetwork) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MaidSubConfigPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("maid_sub_config"));
    public static final StreamCodec<ByteBuf, MaidSubConfigPackage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(ByteBuf buffer, MaidSubConfigPackage message) {
            FriendlyByteBuf buf = new FriendlyByteBuf(buffer);
            buf.writeVarInt(message.id);
            MaidConfigManager.SyncNetwork.encode(message.syncNetwork, buf);
        }

        @Override
        public MaidSubConfigPackage decode(ByteBuf buffer) {
            FriendlyByteBuf buf = new FriendlyByteBuf(buffer);
            int entityId = buf.readVarInt();
            MaidConfigManager.SyncNetwork network = MaidConfigManager.SyncNetwork.decode(buf);
            return new MaidSubConfigPackage(entityId, network);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MaidSubConfigPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                if (!(context.player() instanceof ServerPlayer sender)) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    MaidConfigManager.SyncNetwork.handle(message.syncNetwork, maid);
                }
            });
        }
    }
}
