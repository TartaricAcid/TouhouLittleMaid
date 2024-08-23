package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record SendNameTagPackage(int id, String name, boolean alwaysShow) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendNameTagPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("send_name_tag"));
    public static final StreamCodec<ByteBuf, SendNameTagPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SendNameTagPackage::id,
            ByteBufCodecs.STRING_UTF8,
            SendNameTagPackage::name,
            ByteBufCodecs.BOOL,
            SendNameTagPackage::alwaysShow,
            SendNameTagPackage::new
    );

    public static void handle(SendNameTagPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                Entity entity = sender.level.getEntity(message.id);
                if (entity instanceof EntityMaid) {
                    setMaidNameTag(message, sender, (EntityMaid) entity);
                }
            });
        }
    }

    private static void setMaidNameTag(SendNameTagPackage message, ServerPlayer player, EntityMaid maid) {
        String name = message.name.substring(0, Math.min(32, message.name.length()));
        if (player.equals(maid.getOwner()) && player.getMainHandItem().getItem() == Items.NAME_TAG) {
            maid.setCustomName(Component.literal(name));
            maid.setCustomNameVisible(message.alwaysShow);
            maid.setPersistenceRequired();
            if (!player.isCreative()) {
                player.getMainHandItem().shrink(1);
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
