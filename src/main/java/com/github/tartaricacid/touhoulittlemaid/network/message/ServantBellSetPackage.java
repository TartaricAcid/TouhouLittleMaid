package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemServantBell;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record ServantBellSetPackage(int id, String tip) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServantBellSetPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("servant_bell_set"));
    public static final StreamCodec<ByteBuf, ServantBellSetPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ServantBellSetPackage::id,
            ByteBufCodecs.STRING_UTF8,
            ServantBellSetPackage::tip,
            ServantBellSetPackage::new
    );

    public static void handle(ServantBellSetPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                if (sender.level.getEntity(message.id) instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    ItemServantBell.recordMaidInfo(sender.getMainHandItem(), maid.getUUID(), message.tip);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
